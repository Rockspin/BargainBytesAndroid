package com.rockspin.bargainbits.ui.search

import com.rockspin.bargainbits.data.models.currency.BBCurrency
import com.rockspin.bargainbits.data.models.currency.CurrencyHelper
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter
import com.rockspin.bargainbits.ui.mvp.BaseMvpView
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by valentin.hinov on 22/04/2017.
 */
class SearchPresenter @Inject constructor(val apiService: GameApiService) : BaseMvpPresenter<SearchPresenter.SearchView>() {

    interface SearchView : BaseMvpView {
        fun updateResults(viewModels: List<ResultViewModel>)
        fun showFetchError()
        fun showNoResults()
    }

    var searchQuery: String = ""
        set(value) {
            // TODO - fetch helper from API
            val currencyHelper = CurrencyHelper(BBCurrency("USD", 1.0f))

            addLifetimeDisposable(apiService.searchGames(value)
                .map { it.map { ResultViewModel(it.name, it.thumbnailUrl, currencyHelper.getFormattedPrice(it.cheapestPrice)) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { viewModels ->
                    if (viewModels.isEmpty()) {
                        view?.showNoResults()
                    } else {
                        view?.updateResults(viewModels)
                    }
                }, { throwable ->
                    Timber.d("Error fetching list of game search results", throwable)
                    view?.showFetchError()
                }))
        }
}