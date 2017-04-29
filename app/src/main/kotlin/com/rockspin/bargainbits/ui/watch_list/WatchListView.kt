package com.rockspin.bargainbits.ui.watch_list

import com.rockspin.bargainbits.ui.mvp.BaseMvpView
import io.reactivex.Observable

interface WatchListView : BaseMvpView {
    fun onListIsLoading(): Observable<Boolean>
    fun showLoadingView(loading: Boolean)
    fun onBackButtonPressed(): Observable<Any>
    fun closeWatchList()
    fun showListEmptyView(show: Boolean)
    fun isDealsEmpty(): Boolean
}


