package com.rockspin.bargainbits.ui.search

import com.rockspin.bargainbits.data.models.GameSearchResult
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.test_utils.RxSchedulersRule
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by valentin.hinov on 22/04/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class SearchPresenterTest {

    @get:Rule
    val rule = RxSchedulersRule()

    @Mock
    lateinit var mockApiService: GameApiService

    @Mock
    lateinit var mockView: SearchPresenter.SearchView

    private val mockBackClick = PublishSubject.create<Unit>()

    private lateinit var presenter: SearchPresenter

    @Before
    fun setUp() {
        `when`(mockView.backClick).thenReturn(mockBackClick)
        `when`(mockApiService.searchGames(anyString())).thenReturn(Observable.just(emptyList()))

        presenter = SearchPresenter(mockApiService)
        presenter.onViewCreated(mockView)
    }

    @After
    fun tearDown() {
        presenter.onViewDestroyed()
    }

    @Test
    fun whenBackClicked_goesBack() {
        mockBackClick.onNext(Unit)
        verify(mockView).goBack()
    }

    @Test
    fun whenSearchQuerySet_searchesGamesWithQuery() {
        presenter.searchQuery = "testQuery"
        verify(mockApiService).searchGames("testQuery")
    }

    @Test
    fun whenSearchResultsEmpty_showsNoResults() {
        presenter.searchQuery = "testQuery"
        verify(mockView).showNoResults()
    }

    @Test
    fun whenSearchError_showFetchError() {
        `when`(mockApiService.searchGames(anyString())).thenReturn(Observable.error(Throwable()))
        presenter.searchQuery = "testQuery"

        verify(mockView).showFetchError()
    }

    @Test
    fun whenSearchSuccessful_showsMappedViewModels() {
        `when`(mockApiService.searchGames("testQuery")).thenReturn(Observable.just(listOf(
            GameSearchResult(gameID ="testId0", cheapestPrice = 1.0f, name = "testName0", thumbnailUrl = "testUrl0"),
            GameSearchResult(gameID ="testId1", cheapestPrice = 2.0f, name = "testName1", thumbnailUrl = "testUrl1")
        )))

        presenter.searchQuery = "testQuery"

        verify(mockView).updateResults(listOf(
            ResultViewModel("testName0", "testUrl0", "USD 1.00"),
            ResultViewModel("testName1", "testUrl1", "USD 2.00")
        ))
    }
}