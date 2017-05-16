package com.rockspin.bargainbits.ui.store_filter

import com.rockspin.bargainbits.data.repository.stores.GameStore
import com.rockspin.bargainbits.data.repository.stores.filter.GameStoreFiltered
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.test_utils.RxSchedulersRule
import com.rockspin.bargainbits.test_utils.completeWithValue
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoreFilterPresenterTest {

    @get:Rule
    val rule = RxSchedulersRule()

    @Mock
    lateinit var mockFilter: StoreFilter

    @Mock
    lateinit var mockView: StoreFilterPresenter.View

    private lateinit var presenter: StoreFilterPresenter

    private val testGetStoreResult = PublishSubject.create<List<GameStoreFiltered>>()
    private val testStoreStateChanged = PublishSubject.create<Pair<Int, Boolean>>()
    private val testTryAgainClicked = PublishSubject.create<Unit>()

    @Before
    fun setUp() {
        `when`(mockFilter.getStoreList()).thenReturn(testGetStoreResult.singleOrError())
        `when`(mockView.onItemToggled).thenReturn(testStoreStateChanged)
        `when`(mockView.onTryAgainClicked).thenReturn(testTryAgainClicked)

        presenter = StoreFilterPresenter(mockFilter)
        presenter.onViewCreated(mockView)
    }

    @After
    fun tearDown() {
        presenter.onViewDestroyed()
    }

    @Test
    fun whenViewCreated_fetchesStoreList() {
        verify(mockFilter).getStoreList()
    }

    @Test
    fun whenFetchingStoreList_controlsLoadingState() {
        verify(mockView).showLoading(true)

        testGetStoreResult.completeWithValue(emptyList())

        verify(mockView).showLoading(false)
    }

    @Test
    fun whenFetchingStoreSuccess_showStoreList() {
        val testList = listOf(GameStoreFiltered(GameStore("", "testName0", "testImageUrl0"), true),
            GameStoreFiltered(GameStore("", "testName1", "testImageUrl1"), false),
            GameStoreFiltered(GameStore("", "testName2", "testImageUrl2"), true))

        testGetStoreResult.completeWithValue(testList)

        verify(mockView).showStoreList(listOf(StoreViewModel("testName0", "testImageUrl0", true),
            StoreViewModel("testName1", "testImageUrl1", false),
            StoreViewModel("testName2", "testImageUrl2", true)))
    }

    @Test
    fun whenFetchingStoreError_showLoadError() {
        testGetStoreResult.onError(Throwable())

        verify(mockView).showLoadError()
    }

    @Test
    fun whenOnStoreStateChanged_doNothingIfNoChange() {
        val testList = listOf(GameStoreFiltered(GameStore("testId0", "testName0", ""), true),
            GameStoreFiltered(GameStore("testId1", "testName1", ""), false))

        testGetStoreResult.completeWithValue(testList)

        testStoreStateChanged.onNext(Pair(1, false))

        verify(mockFilter, never()).updateStore(GameStoreFiltered(GameStore("testId1", "testName1", ""), false))
        verify(mockView, never()).updateStoreAtIndex(1, StoreViewModel("testName1", "", false))
    }

    @Test
    fun whenOnStoreStateChanged_updateFilterAndList() {
        val testList = listOf(GameStoreFiltered(GameStore("testId0", "testName0", ""), true),
            GameStoreFiltered(GameStore("testId1", "testName1", ""), false),
            GameStoreFiltered(GameStore("testId2", "testName2", ""), false))

        testGetStoreResult.completeWithValue(testList)

        testStoreStateChanged.onNext(Pair(2, true))

        val updatedStored = GameStoreFiltered(GameStore("testId2", "testName2", ""), true)
        verify(mockFilter).updateStore(updatedStored)
        verify(mockView).updateStoreAtIndex(2, StoreViewModel("testName2", "", true))
    }

    @Test
    fun whenOnTryAgainClicked_tryReload() {
        testTryAgainClicked.onNext(Unit)

        verify(mockFilter, times(2)).getStoreList()
    }
}