package com.rockspin.bargainbits.ui.watch_list

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import butterknife.Bind
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.view.RxView
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.ui.BaseMvpActivity
import com.rockspin.bargainbits.ui.views.deallist.DealsListView
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListPresenter
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListViewImpl
import com.rockspin.bargainbits.util.visible
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable
import javax.inject.Inject

class WatchListActivity : BaseMvpActivity<WatchListView, WatchListPresenter>(), WatchListView, DealsListViewImpl.DealsListContainer {

    @Inject override lateinit var presenter: WatchListPresenter
    @Inject lateinit var dealsListPresenter: DealsListPresenter

    @Bind(R.id.watchlist_toolbar) lateinit var toolbar: Toolbar
    @Bind(R.id.watchlist_deals_list) lateinit var dealsList: DealsListView
    @Bind(R.id.watchlist_layout_loading) lateinit var loadingView: View
    @Bind(R.id.watchlist_button_back) lateinit var backButton: Button
    @Bind(R.id.watchlist_layout_no_results) lateinit var noResultsView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchlist)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        dealsList.setPresenter(dealsListPresenter)
        dealsList.viewWillShow()
        dealsList.loadDealsInWatchList()
    }

    override fun onDestroy() {
        dealsList.viewWillHide()

        super.onDestroy()
    }

    override fun closeView() {
        finish()
    }

    override fun onListIsLoading(): Observable<Boolean> {
        return RxJavaInterop.toV2Observable(dealsList.onListLoading())
    }

    override fun showLoadingView(loading: Boolean) {
        loadingView.visible = loading
    }

    override fun onBackButtonPressed(): Observable<Any> {
        return RxView.clicks(backButton)
    }

    override fun closeWatchList() {
        finish()
    }

    override fun showListEmptyView(show: Boolean) {
        noResultsView.visible = show
    }

    override fun isDealsEmpty(): Boolean {
        return dealsList.isListEmpty
    }
}
