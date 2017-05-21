package com.rockspin.bargainbits.ui.search.detail

import com.rockspin.bargainbits.data.models.AbbreviatedDeal
import com.rockspin.bargainbits.data.models.GameStore
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.di.annotations.GameDealUrl
import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter
import com.rockspin.bargainbits.ui.mvp.BaseMvpView
import com.rockspin.bargainbits.util.format.PriceFormatter
import com.rockspin.bargainbits.watch_list.WatchedItem
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by valentin.hinov on 23/04/2017.
 */
class SearchDetailPresenter @Inject constructor(
    val apiService: GameApiService,
    val storeRepository: StoreRepository,
    val formatter: PriceFormatter,
    @GameDealUrl val gameDealBaseUrl: String)
    : BaseMvpPresenter<SearchDetailPresenter.View>() {

    interface View : BaseMvpView {
        fun showLoading(show: Boolean)
        fun showAvailableDeals(deals: List<AbbreviatedDealViewModel>)
        fun showLoadError()
        fun setScreenTitle(title: String)
        fun openDealUrl(url: String)

        val onItemClicked: Observable<Int>
        val onWatchListClicked: Observable<*>
        fun openWatchItemView(watchedItem: WatchedItem)
    }

    private var loadedDeals: List<AbbreviatedDeal> = emptyList()

    fun setData(gameId: String, gameName: String) {
        this.view?.setScreenTitle(gameName)

        addLifetimeDisposable(view!!.onItemClicked
            .subscribe {
                view?.openDealUrl("$gameDealBaseUrl${loadedDeals[it].dealID}")
            })

        addLifetimeDisposable(view!!.onWatchListClicked
            .map {
                val cheapestDeal = loadedDeals.sortedByDescending { it.price }.last()
                WatchedItem(gameName, gameId, cheapestDeal.price.toFloat())
            }
            .subscribe {
                view?.openWatchItemView(it)
            })

        fetchGameInfo(gameId)
    }

    private fun fetchGameInfo(gameId: String) {
        addLifetimeDisposable(apiService.getGameInfo(gameId)
            .doOnSuccess { loadedDeals = it.deals }
            .flatMapObservable { Observable.fromIterable(it.deals) }
            .flatMapSingle {
                Single.zip<AbbreviatedDeal, GameStore, Pair<AbbreviatedDeal, GameStore>>(Single.just(it), storeRepository.getGameStoreForId
                (it.storeID), BiFunction { deal, store -> Pair(deal, store) })
            }
            .map { (deal, store) ->
                val hasSavings = deal.savingsFraction > 0
                val savingPercentage = if (hasSavings) deal.savingsFraction else null

                AbbreviatedDealViewModel(
                    storeImageUrl = store.imageUrl,
                    storeName = store.name,
                    normalPrice = if (hasSavings) formatter.formatPrice(deal.retailPrice) else null,
                    dealPrice = formatter.formatPrice(deal.price),
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