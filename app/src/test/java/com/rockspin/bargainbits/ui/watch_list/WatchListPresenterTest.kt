package com.rockspin.bargainbits.ui.watch_list

import io.reactivex.Observable
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class WatchListPresenterTest {

    var presenter: WatchListPresenter = WatchListPresenter()

    @Mock var view: WatchListView = mock(WatchListView::class.java)

    @Test
    fun whenLoadingShowLoadingView() {
        `when`(view.onListIsLoading()).thenReturn(Observable.just(true))
        `when`(view.onBackButtonPressed()).thenReturn(Observable.never())

        presenter.onViewCreated(view)

        verify(view).showLoadingView(true)
    }

    @Test
    fun whenNotLoadingAndListIsEmptyShowNoResultsView() {
        `when`(view.onListIsLoading()).thenReturn(Observable.just(false))
        `when`(view.onBackButtonPressed()).thenReturn(Observable.never())
        `when`(view.isDealsEmpty()).thenReturn(true)

        presenter.onViewCreated(view)

        verify(view).showListEmptyView(true)
    }

    @Test
    fun whenNotLoadingAndListNotEmptyHideLoadingView() {
        `when`(view.onListIsLoading()).thenReturn(Observable.just(false))
        `when`(view.onBackButtonPressed()).thenReturn(Observable.never())
        `when`(view.isDealsEmpty()).thenReturn(false)

        presenter.onViewCreated(view)

        verify(view).showLoadingView(false)
    }

    @Test
    fun whenOnBackButtonTriggeredCloseWatchList() {
        `when`(view.onListIsLoading()).thenReturn(Observable.never())
        `when`(view.onBackButtonPressed()).thenReturn(Observable.just(Any()))

        presenter.onViewCreated(view)

        verify(view).closeWatchList()
    }
}