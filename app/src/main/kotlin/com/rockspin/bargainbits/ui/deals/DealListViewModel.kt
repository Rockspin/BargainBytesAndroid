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
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
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

    var sortIndex: Int = 0
    set(value) {
        field = value
        onActiveSortTypeChange.onNext(SCREEN_SPINNER_SORT_ORDER[value])
    }

    // TODO - remember user default choice for deal sorting

    private val activeSortType: DealSortType
    get() = SCREEN_SPINNER_SORT_ORDER[sortIndex]

    private val onActiveSortTypeChange = BehaviorSubject.createDefault(SCREEN_SPINNER_SORT_ORDER[sortIndex])
    private val dateFormatter = SimpleDateFormat.getDateInstance()

    val dealEntries: Observable<List<DealEntryUiModel>>
    get() {
        return Observable.combineLatest(onActiveSortTypeChange, filter.activeStoresIdsObservable,
            BiFunction<DealSortType, Set<String>, Pair<DealSortType, Set<String>>> {
                sortType, storeIdSet ->
                Pair(sortType, storeIdSet)
            })
            .subscribeOn(Schedulers.io())
            .flatMap { (first, second) ->
                repository.getDeals(first, second).toObservable()
            }
            .compose(gameDealListToUiModelList)
    }

    override fun onCleared() {
        super.onCleared()
    }

    private val gameDealListToUiModelList = ObservableTransformer<List<GroupedGameDeal>, List<DealEntryUiModel>> {
        it.map { gameDeals ->
            gameDeals.map { groupedGameDeal ->
                DealEntryUiModel(
                    title = groupedGameDeal.title,
                    subtitle = getSubtitleForDealAndSortType(groupedGameDeal, activeSortType),
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