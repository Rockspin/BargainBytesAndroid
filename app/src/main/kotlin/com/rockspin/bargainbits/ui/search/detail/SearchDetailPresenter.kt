package com.rockspin.bargainbits.ui.search.detail

import com.rockspin.bargainbits.data.models.AbbreviatedDeal
import com.rockspin.bargainbits.data.repository.stores.GameStore
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter
import com.rockspin.bargainbits.ui.mvp.BaseMvpView
import com.rockspin.bargainbits.util.format.PriceFormatter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by valentin.hinov on 23/04/2017.
 */
class SearchDetailPresenter @Inject constructor(val apiService: GameApiService, val storeRepository: StoreRepository, val formatter: PriceFormatter)
    : BaseMvpPresenter<SearchDetailPresenter.SearchDetailView>() {

    interface SearchDetailView : BaseMvpView {
        fun showLoading(show: Boolean)
        fun showAvailableDeals(deals: List<AbbreviatedDealViewModel>)
        fun showLoadError()

        var title: String
        val onItemClicked: Observable<Int>
        val onWatchListClicked: Observable<*>
    }

    fun onViewCreated(view: SearchDetailView, gameId: String, gameName: String) {
        super.onViewCreated(view)

        this.view?.title = gameName

        addLifetimeDisposable(view.onItemClicked
            .subscribe {

            })

        addLifetimeDisposable(view.onWatchListClicked
            .subscribe {
            })

        fetchGameInfo(gameId)
    }

    private fun fetchGameInfo(gameId: String) {
        addLifetimeDisposable(apiService.getGameInfo(gameId)
            .flatMapObservable { Observable.fromIterable(it.deals) }
            .flatMapSingle {
                Single.zip<AbbreviatedDeal, GameStore, Pair<AbbreviatedDeal, GameStore>>(Single.just(it), storeRepository.getGameStoreForId
                (it.storeID), BiFunction { deal, store -> Pair(deal, store) })
            }
            .map { pair ->
                val deal = pair.first
                val store = pair.second

                val hasSavings = deal.price < deal.retailPrice
                val dealPrice = if (hasSavings) formatter.formatPrice(deal.price) else null
                val savingPercentage = if (hasSavings) deal.savingsFraction else null

                AbbreviatedDealViewModel(
                    storeImageUrl = store.imageUrl,
                    storeName = store.name,
                    normalPrice = formatter.formatPrice(deal.retailPrice),
                    dealPrice = dealPrice,
                    savingPercentage = savingPercentage)
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { this.view?.showLoading(true) }
            .doFinally { this.view?.showLoading(false) }
            .subscribe({ viewModels ->
                this.view?.showAvailableDeals(viewModels)
            }, { throwable ->
                Timber.e("Error while loading game info", throwable)
                this.view?.showLoadError()
            }))
    }
}