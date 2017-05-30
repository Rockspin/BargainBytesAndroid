package com.rockspin.bargainbits.ui.deals

import android.arch.lifecycle.ViewModel
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.data.models.DealSortType
import com.rockspin.bargainbits.data.models.GroupedGameDeal
import com.rockspin.bargainbits.data.repository.deals.GameDealRepository
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.util.ResourceProvider
import com.rockspin.bargainbits.util.format.PriceFormatter
import io.reactivex.ObservableTransformer
import io.reactivex.Single
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
    private val priceFormatter: PriceFormatter)
    : ViewModel() {

    companion object {
        val SCREEN_SPINNER_SORT_ORDER = arrayOf(
            DealSortType.RATING, DealSortType.STEAM_REVIEWS, DealSortType.METACRITIC, DealSortType.RECENT,
            DealSortType.RELEASE, DealSortType.SAVINGS, DealSortType.PRICE )
    }

    val eventToAction = ObservableTransformer<DealListEvent, DealListAction> {
        it.map {
            when (it) {
                is DealSortingChanged -> LoadDealsWithSortType(SCREEN_SPINNER_SORT_ORDER[it.index])
            }
        }
    }

    val actionToUiModel = ObservableTransformer<DealListAction, DealListUiModel> {
        it.publish { shared ->
            // TODO - cast will be unnecessary once we have more action
            listOf(shared.ofType<LoadDealsWithSortType>().compose(loadDealsActionToShowDealEntries).cast(DealListUiModel::class.java))
                .merge()
        }
    }

    private val loadDealsActionToShowDealEntries = ObservableTransformer<LoadDealsWithSortType, ShowDealEntriesUiModel> {
        it.combineLatest(filter.activeStoresIdsObservable)
            .subscribeOn(Schedulers.io())
            .flatMapSingle { (first, second) ->
                repository.getDeals(first.sortType, second).zipWith(Single.just(first.sortType),
                    BiFunction<List<GroupedGameDeal>, DealSortType, Pair<List<GroupedGameDeal>, DealSortType>> {
                        deals, sortType -> Pair(deals, sortType)
                    })
            }
            .compose(gameDealsToDealViewEntries)
            .map { ShowDealEntriesUiModel(it) }
    }

    private val dateFormatter = SimpleDateFormat.getDateInstance()

    override fun onCleared() {
        super.onCleared()
    }

    private val gameDealsToDealViewEntries = ObservableTransformer<Pair<List<GroupedGameDeal>, DealSortType>, List<DealViewEntry>> {
        it.map { (gameDeals, sortType) ->
            gameDeals.map { groupedGameDeal ->
                DealViewEntry(
                    title = groupedGameDeal.title,
                    subtitle = getSubtitleForDealAndSortType(groupedGameDeal, sortType),
                    imageUrl = groupedGameDeal.thumbUrl,
                    savingsPercentage = groupedGameDeal.savingsPercentage.toInt(),
                    retailPrice = priceFormatter.formatPrice(groupedGameDeal.normalPrice),
                    salePrice = priceFormatter.formatPrice(groupedGameDeal.salePrice),
                    storeImageUrls = groupedGameDeal.storeIds.map { storeRepository.getGameStoreForId(it).blockingGet().imageUrl }
                )
            }
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
}