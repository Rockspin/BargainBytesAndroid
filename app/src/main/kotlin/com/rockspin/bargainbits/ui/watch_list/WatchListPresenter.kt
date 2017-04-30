package com.rockspin.bargainbits.ui.watch_list

import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter
import javax.inject.Inject


class WatchListPresenter @Inject constructor(): BaseMvpPresenter<WatchListView>() {

    override fun onViewCreated(view: WatchListView) {
        super.onViewCreated(view)

        addLifetimeDisposable(view.onListIsLoading().subscribe({
            loading ->
            run {
                view.showLoadingView(loading)

                if (loading) {
                    view.showListEmptyView(false)
                } else {
                    if (view.isDealsEmpty()) {
                        view.showListEmptyView(true)
                    } else {
                        view.showListEmptyView(false)
                    }
                }
            }
        }))
        addLifetimeDisposable(view.onBackButtonPressed().subscribe({ view.closeWatchList() }))
    }
}