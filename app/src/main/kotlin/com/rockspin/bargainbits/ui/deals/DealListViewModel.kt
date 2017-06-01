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
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.rxkotlin.merge
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by valentin.hinov on 23/05/2017.
 */
class DealListViewModel(
    private val repository: GameDealRepository,
    private val filter: StoreFilter,
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
        data class Navigation(val navigation: DealListUiModel.Navigation): Result()
    }

    companion object {
        val SCREEN_SPINNER_SORT_ORDER = arrayOf(
            DealSortType.RATING, DealSortType.STEAM_REVIEWS, DealSortType.METACRITIC, DealSortType.RECENT,
            DealSortType.RELEASE, DealSortType.SAVINGS, DealSortType.PRICE )
    }

    val eventToUiModel = ObservableTransformer<DealListEvent, DealListUiModel> {
        it.compose(eventToAction).compose(actionToResult).compose(resultToUiModel)
    }

    private val eventToAction = ObservableTransformer<DealListEvent, DealListAction> {
        it.map {
            when (it) {
                is DealListEvent.SortingChanged -> DealListAction.LoadDealsWithSortType(SCREEN_SPINNER_SORT_ORDER[it.index])
                is DealListEvent.MenuItemClicked -> DealListAction.PerformNavigation(navigationForMenuOption(it.menuId))
            }
        }
    }

    private val actionToResult = ObservableTransformer<DealListAction, Result> {
        it.publish { shared ->
            listOf(
                shared.ofType<DealListAction.LoadDealsWithSortType>().compose(loadDealsActionToDealLoadResult),
                shared.ofType<DealListAction.PerformNavigation>().map { Result.Navigation(it.navigation) },
                networkUtils.onNetworkChanged().map { Result.InternetState(it) })
                .merge()
        }
    }

    private val resultToUiModel = ObservableTransformer<Result, DealListUiModel> {
        it.scan(DealListUiModel(), {oldState, result ->
            when (result) {
                is Result.InternetState -> oldState.copy(showInternetOffMessage = !result.isOn)
                is Result.DealLoad.Success -> oldState.copy(dealViewEntries = dealLoadResultSuccessToDealViewEntries(result),
                    inProgress = false, hasError = false)
                is Result.DealLoad.InProgress -> oldState.copy(inProgress = true, hasError = false)
                is Result.DealLoad.Failed -> oldState.copy(inProgress = false, hasError = true)
                is Result.Navigation -> oldState.copy(navigation = result.navigation)
            }
        })
    }

    private val loadDealsActionToDealLoadResult = ObservableTransformer<DealListAction.LoadDealsWithSortType, Result.DealLoad> {
        it.combineLatest(filter.activeStoresIdsObservable)
            .subscribeOn(Schedulers.io())
            .map { Pair(it.first.sortType, it.second) }
            .flatMap { (sortType, filter) ->
                repository.getDeals(sortType, filter).toObservable().zipWith(Observable.just(sortType),
                    BiFunction<List<GroupedGameDeal>, DealSortType, Pair<List<GroupedGameDeal>, DealSortType>> {
                        deals, sortType -> Pair(deals, sortType)
                    })
                    .map { Result.DealLoad.Success(it.first, it.second) }.cast(Result.DealLoad::class.java)
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

    private fun navigationForMenuOption(@IdRes menuId: Int): DealListUiModel.Navigation =
        when (menuId) {
            R.id.menu_watch_list -> DealListUiModel.Navigation.WATCH_LIST
            R.id.menu_store_filter -> DealListUiModel.Navigation.STORE_FILTER
            R.id.menu_rate -> DealListUiModel.Navigation.RATE_APP
            R.id.menu_share -> DealListUiModel.Navigation.SHARE_APP
            R.id.menu_feedback -> DealListUiModel.Navigation.SEND_FEEDBACK
            else -> {
                throw IllegalArgumentException("Invalid menuId supplied")
            }
        }
}