package com.rockspin.bargainbits.ui.watch_list

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
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
        supportActionBar!!.setHomeButtonEnabled(true)

        dealsList.setPresenter(dealsListPresenter)
        presenter.onViewCreated(this)
        dealsList.viewWillShow(this)
        dealsList.loadDealsInWatchList()
    }

    override fun onDestroy() {
        presenter.onViewDestroyed()
        dealsList.viewWillHide();

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.watch_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun closeView() {
        finish()
    }

    override fun onListIsLoading(): Observable<Boolean> {
        return RxJavaInterop.toV2Observable(dealsList.onListLoading())
    }

    override fun showLoadingView(loading: Boolean) {
        if (loading) {
            loadingView.visibility = View.VISIBLE
        } else {
            loadingView.visibility = View.GONE
        }
    }

    override fun onBackButtonPressed(): Observable<Any> {
        return RxView.clicks(backButton)
    }

    override fun closeWatchList() {
        finish()
    }

    override fun showListEmptyView(show: Boolean) {
        if (show) {
            noResultsView.visibility = View.VISIBLE
        } else {
            noResultsView.visibility = View.GONE
        }
    }

    override fun isDealsEmpty(): Boolean {
        return dealsList.isListEmpty
    }
}
