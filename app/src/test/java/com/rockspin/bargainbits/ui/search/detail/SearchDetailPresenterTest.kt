package com.rockspin.bargainbits.ui.search.detail

import com.rockspin.bargainbits.data.models.AbbreviatedDeal
import com.rockspin.bargainbits.data.models.GameInfo
import com.rockspin.bargainbits.data.models.Info
import com.rockspin.bargainbits.data.repository.stores.GameStore
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.test_utils.RxSchedulersRule
import com.rockspin.bargainbits.test_utils.completeWithValue
import com.rockspin.bargainbits.util.format.PriceFormatter
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by valentin.hinov on 24/04/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class SearchDetailPresenterTest {

    companion object {
        private val TEST_GAME_ID = "0"
        private val TEST_GAME_NAME = "testGameName"

        private val TEST_STORE_URL_PREFIX = "prefix:"

        fun createGameInfoWithDeals(deals: List<AbbreviatedDeal>): GameInfo {
            return GameInfo(Info(TEST_GAME_NAME), deals = deals)
        }
    }

    @get:Rule
    val rule = RxSchedulersRule()

    @Mock
    lateinit var mockApiService: GameApiService

    @Mock
    lateinit var mockView: SearchDetailPresenter.SearchDetailView

    @Mock
    lateinit var mockFormatter: PriceFormatter

    @Mock
    lateinit var mockStoreRepository: StoreRepository

    private val mockWatchListClick = PublishSubject.create<Unit>()
    private val mockDealClick = PublishSubject.create<Int>()
    private val gameInfoResult = PublishSubject.create<GameInfo>()

    private lateinit var presenter: SearchDetailPresenter

    @Before
    fun setUp() {

        `when`(mockView.onItemClicked).thenReturn(mockDealClick)
        `when`(mockView.onWatchListClicked).thenReturn(mockWatchListClick)
        `when`(mockApiService.getGameInfo(anyString())).thenReturn(gameInfoResult.singleOrError())
        `when`(mockStoreRepository.getGameStoreForId(anyString())).thenReturn(Single.just(GameStore("", "")))

        presenter = SearchDetailPresenter(mockApiService, mockStoreRepository, mockFormatter)
        presenter.onViewCreated(mockView, TEST_GAME_ID, TEST_GAME_NAME)
    }

    @After
    fun tearDown() {
        presenter.onViewDestroyed()
    }

    @Test
    fun whenViewCreated_setsTitleToGameName() {
        verify(mockView).title = TEST_GAME_NAME
    }

    @Test
    fun whenViewCreated_getsGameInfoForGameId() {
        verify(mockApiService).getGameInfo(TEST_GAME_ID)
    }

    @Test
    fun whenGetGameInfo_controlsLoadingState() {
        verify(mockView).showLoading(true)

        gameInfoResult.completeWithValue(createGameInfoWithDeals(emptyList()))

        verify(mockView).showLoading(false)
    }

    @Test
    fun whenGetGameInfoError_showsLoadError() {
        gameInfoResult.onError(Throwable())

        verify(mockView).showLoadError()
    }

    @Test
    fun whenGetGameInfoSuccess_showsMappedViewModels() {
        val testDeals = listOf(
            AbbreviatedDeal("testStoreId0", "testDealId0", 1.0, 1.0, 0.0),
            AbbreviatedDeal("testStoreId1", "testDealId1", 1.0, 2.0, 0.5))

        `when`(mockFormatter.formatPrice(2.0)).thenReturn("$2.00")
        `when`(mockFormatter.formatPrice(1.0)).thenReturn("$1.00")
        `when`(mockStoreRepository.getGameStoreForId("testStoreId0")).thenReturn(Single.just(
            GameStore("testStoreName0", "$TEST_STORE_URL_PREFIX testStoreId0")))
        `when`(mockStoreRepository.getGameStoreForId("testStoreId1")).thenReturn(Single.just(
            GameStore("testStoreName1", "$TEST_STORE_URL_PREFIX testStoreId1")))

        gameInfoResult.completeWithValue(createGameInfoWithDeals(testDeals))

        val expectedViewModels = listOf(
            AbbreviatedDealViewModel("$TEST_STORE_URL_PREFIX testStoreId0", "testStoreName0", "$1.00"),
            AbbreviatedDealViewModel("$TEST_STORE_URL_PREFIX testStoreId1", "testStoreName1", "$2.00", "$1.00", 0.5))

        verify(mockView).showAvailableDeals(expectedViewModels)
    }
}