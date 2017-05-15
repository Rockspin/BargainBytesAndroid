package com.rockspin.bargainbits.ui.top_navigation

import com.rockspin.bargainbits.ui.mvp.BaseMvpView
import io.reactivex.Observable

interface TopNavigationView : BaseMvpView {
    fun onDrawerToggled(): Observable<Boolean>
    fun onOpenWatchListPressed(): Observable<Any>
    fun openWatchList()
    fun onStoresFilterPressed(): Observable<Any>
    fun openStoresDrawer()
    fun onRateAppPressed(): Observable<Any>
    fun goToStoreAndRate()
    fun onFeedbackPressed(): Observable<Any>
    fun sendFeedbackEmail()
    fun onShareAppPressed(): Observable<Any>
    fun showShareAppDialog()
    fun onViewPagerChanged(): Observable<Int>
    fun setNavigationSpinnerItem(page: Int)
    fun onNavigationItemSelected(): Observable<Int>
    fun selectTab(item: Int)
    fun hideNoInternetMessage()
    fun showNoInternetMessage()
}