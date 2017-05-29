package com.rockspin.bargainbits.ui.deals

import android.arch.lifecycle.ViewModel
import com.rockspin.bargainbits.data.models.DealSortType
import com.rockspin.bargainbits.data.models.GroupedGameDeal
import com.rockspin.bargainbits.data.repository.deals.GameDealRepository
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.util.format.PriceFormatter
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers


/**
 * Created by valentin.hinov on 23/05/2017.
 */
class DealListViewModel(
    private val repository: GameDealRepository,
    private val filter: StoreFilter,
    private val storeRepository: StoreRepository,
    private val priceFormatter: PriceFormatter)
    : ViewModel() {

    val dealEntries: Observable<List<DealEntryUiModel>>
    get() {
        return filter.activeStoresIdsObservable
            .subscribeOn(Schedulers.io())
            .flatMap { storeIds ->
                repository.getDeals(DealSortType.RATING, storeIds).toObservable()
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
                    subtitle = "Rating ${groupedGameDeal.dealRating}", // TODO - get from resources
                    imageUrl = groupedGameDeal.thumbUrl,
                    savingsPercentage = groupedGameDeal.savingsPercentage.toInt(),
                    retailPrice = priceFormatter.formatPrice(groupedGameDeal.normalPrice),
                    salePrice = priceFormatter.formatPrice(groupedGameDeal.salePrice),
                    storeImageUrls = groupedGameDeal.storeIds.map { storeRepository.getGameStoreForId(it).blockingGet().imageUrl }
                    )
            }
        }
    }
}