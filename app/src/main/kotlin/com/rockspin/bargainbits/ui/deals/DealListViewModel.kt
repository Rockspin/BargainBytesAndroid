package com.rockspin.bargainbits.ui.deals

import android.arch.lifecycle.ViewModel
import android.support.annotation.IdRes
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.data.models.DealSortType
import com.rockspin.bargainbits.data.models.GroupedGameDeal
import com.rockspin.bargainbits.data.repository.deals.GameDealRepository
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.util.ResourceProvider
import com.rockspin.bargainbits.util.format.PriceFormatter
import com.rockspin.bargainbits.utils.NetworkUtils
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.merge
import io.reactivex.rxkotlin.ofType
import java.text.SimpleDateFormat
import java.util.*

class DealListViewModel(
    private val repository: GameDealRepository,
    private val storeFilter: StoreFilter,
    private val storeRepository: StoreRepository,
    private val resourceProvider: ResourceProvider,
    private val priceFormatter: PriceFormatter,
    private val networkUtils: NetworkUtils)
    : ViewModel() {

    sealed class Result {
        data class InternetState(val isOn: Boolean): Result()
        sealed class DealLoad: Result() {
            data class Success(val deals: List<GroupedGameDeal>, val sortType: DealSortType): DealLoad()
            object InProgress: DealLoad()
            object Failed: DealLoad()
        }
        data class Navigation(val navigation: DealListUiState.Navigation): Result()
        object NavigationComplete: Result()
    }

    companion object {
        val SCREEN_SPINNER_SORT_ORDER = arrayOf(
            DealSortType.RATING, DealSortType.STEAM_REVIEWS, DealSortType.METACRITIC, DealSortType.RECENT,
            DealSortType.RELEASE, DealSortType.SAVINGS, DealSortType.PRICE )
    }

    private lateinit var activeSortType: DealSortType

    val eventToUiState = ObservableTransformer<DealListEvent, DealListUiState> {
        it.compose(eventToAction).compose(actionToResult).compose(resultToUiState)
    }

    private val eventToAction = ObservableTransformer<DealListEvent, DealListAction> {
        it.map {
            when (it) {
                is DealListEvent.SortingChanged -> DealListAction.LoadDealsWithSortType(SCREEN_SPINNER_SORT_ORDER[it.index])
                is DealListEvent.MenuItemClicked -> actionForMenuOption(it.menuId)
                is DealListEvent.StoreFilterClosed -> DealListAction.CheckForFilterChanges
            }
        }
    }

    private val actionToResult = ObservableTransformer<DealListAction, Result> {
        it.publish { shared ->
            listOf(
                shared.ofType<DealListAction.LoadDealsWithSortType>().map { it.sortType}.compose(loadDealsActionToDealLoadResult),
                shared.ofType<DealListAction.CheckForFilterChanges>().map { activeSortType }.compose(loadDealsActionToDealLoadResult),
                shared.ofType<DealListAction.PerformNavigation>().map { Result.Navigation(it.navigation) },
                networkUtils.onNetworkChanged().skip(1).map { Result.InternetState(it) })
                .merge()
        }
    }

    private val resultToUiState = ObservableTransformer<Result, DealListUiState> {
        it.scan(DealListUiState(), { oldState, result ->
            val state = oldState.copy(navigation = null) // clear out old navigation from state
            when (result) {
                is Result.InternetState -> state.copy(showInternetOffMessage = !result.isOn)
                is Result.DealLoad.Success -> state.copy(dealViewEntries = dealLoadResultSuccessToDealViewEntries(result),
                    inProgress = false, hasError = false)
                is Result.DealLoad.InProgress -> state.copy(inProgress = true, hasError = false)
                is Result.DealLoad.Failed -> state.copy(inProgress = false, hasError = true)
                is Result.Navigation -> state.copy(navigation = result.navigation)
                is Result.NavigationComplete -> state.copy(navigation = null)
            }
        })
    }

    private val loadDealsActionToDealLoadResult = ObservableTransformer<DealSortType, Result.DealLoad> {
        it.doOnNext { activeSortType = it }
            .flatMap { sortType ->
                storeFilter.activeStoreIds
                    .flatMap { activeStoreIds -> repository.getDeals(sortType, activeStoreIds) }
                    .toObservable()
                    .map<Result.DealLoad> { Result.DealLoad.Success(it, sortType) }
                    .onErrorReturnItem(Result.DealLoad.Failed)
                    .startWith(Result.DealLoad.InProgress)
            }
    }

    private val dateFormatter = SimpleDateFormat.getDateInstance()

    private fun dealLoadResultSuccessToDealViewEntries(result: Result.DealLoad.Success): List<DealViewEntry> {
        return result.deals.map { groupedGameDeal ->
            DealViewEntry(
                title = groupedGameDeal.title,
                subtitle = getSubtitleForDealAndSortType(groupedGameDeal, result.sortType),
                imageUrl = groupedGameDeal.thumbUrl,
                savingsPercentage = groupedGameDeal.savingsPercentage.toInt(),
                retailPrice = priceFormatter.formatPrice(groupedGameDeal.normalPrice),
                salePrice = priceFormatter.formatPrice(groupedGameDeal.salePrice),
                storeImageUrls = groupedGameDeal.storeIds.map { storeRepository.getGameStoreForId(it).blockingGet().imageUrl }
            )
        }
    }

    private fun getSubtitleForDealAndSortType(deal: GroupedGameDeal, activeSortType: DealSortType): String {
        return when(activeSortType) {
            DealSortType.RATING -> resourceProvider.getString(R.string.rating_subtitle, deal.dealRating)
            DealSortType.STEAM_REVIEWS -> resourceProvider.getString(R.string.steam_rating_subtitle, deal.steamRatingPercent)
            DealSortType.METACRITIC -> resourceProvider.getString(R.string.metacritic_subtitle, deal.metacriticScore)
            // TODO - for recent dates use the "today, yesterday, days ago style"
            DealSortType.RECENT -> resourceProvider.getString(R.string.date_updated_subtitle, dateFormatter.format(deal.lastUpdatedDate))
            DealSortType.RELEASE -> {
                val inFuture = deal.releaseDate.after(Date())
                val stringRes = if (inFuture) R.string.release_date_future_subtitle else R.string.release_date_subtitle
                resourceProvider.getString(stringRes, dateFormatter.format(deal.releaseDate))
            }
            else -> {
                return ""
            }
        }
    }

    private fun actionForMenuOption(@IdRes menuId: Int): DealListAction {
        val navigation = when (menuId) {
            R.id.menu_watch_list -> DealListUiState.Navigation.WATCH_LIST
            R.id.menu_store_filter -> DealListUiState.Navigation.STORE_FILTER
            R.id.menu_rate -> DealListUiState.Navigation.RATE_APP
            R.id.menu_share -> DealListUiState.Navigation.SHARE_APP
            R.id.menu_feedback -> DealListUiState.Navigation.SEND_FEEDBACK
            else -> {
                throw IllegalArgumentException("Invalid menuId supplied")
            }
        }

        return DealListAction.PerformNavigation(navigation)
    }

}