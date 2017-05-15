package com.rockspin.bargainbits.ui.top_navigation

import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter
import com.rockspin.bargainbits.utils.NetworkUtils
import com.rockspin.bargainbits.utils.analytics.IAnalytics
import io.reactivex.Observable
import javax.inject.Inject

class TopNavigationPresenter @Inject constructor(private val analytics: IAnalytics,
                                                 private val networkUtils: NetworkUtils)
    : BaseMvpPresenter<TopNavigationView>() {

    override fun onViewCreated(view: TopNavigationView) {
        super.onViewCreated(view)

        // when the drawer opens
        addLifetimeDisposable(view.onDrawerToggled()
                .subscribe { open -> analytics.onStoreSliderToggled(open) })

        // when the user pressed the open watchlist button
        addLifetimeDisposable(view.onOpenWatchListPressed().subscribe { view.openWatchList() })
        // when the user pressed the stores filter button
        addLifetimeDisposable(view.onStoresFilterPressed().subscribe { view.openStoresDrawer() })

        // when the user pressed the app rate button
        addLifetimeDisposable(view.onRateAppPressed().subscribe { view.goToStoreAndRate() })
        // when the user pressed the feed back button
        addLifetimeDisposable(view.onFeedbackPressed().subscribe { view.sendFeedbackEmail() })
        addLifetimeDisposable(view.onShareAppPressed().subscribe { view.showShareAppDialog() })
        addLifetimeDisposable(view.onViewPagerChanged()
                .subscribe { page -> view.setNavigationSpinnerItem(page) })
        //watch list reactions
        addLifetimeDisposable(view.onNavigationItemSelected()
                .subscribe { item -> view.selectTab(item) })

        addLifetimeDisposable(onInternetAvailabilityChanged()
                .subscribe { internetAvailable ->
            if (internetAvailable) {
                view.hideNoInternetMessage()
            } else {
                view.showNoInternetMessage()
            }
        })
    }

    fun onInternetAvailabilityChanged(): Observable<Boolean> {
        return networkUtils.onNetworkChanged()
    }
}