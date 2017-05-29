package com.rockspin.bargainbits.data.repository.stores.filter

import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.models.GameStore
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BBStoreFilterTest {

    @Mock
    lateinit var mockRepository: StoreRepository

    @Mock
    lateinit var mockPrimitiveStore: PrimitiveStore

    private lateinit var storeFilter: BBStoreFilter

    companion object {
        val TEST_STORE_LIST = listOf(
            GameStore("id0", "name0", ""),
            GameStore("id1", "name1", ""),
            GameStore("id2", "name2", ""))
    }

    @Before
    fun setUp() {
        `when`(mockRepository.getStores()).thenReturn(Single.just(TEST_STORE_LIST))
        `when`(mockPrimitiveStore.getStoredStringSet(BBStoreFilter.STORE_FILTER_KEY)).thenReturn(emptySet())
        storeFilter = BBStoreFilter(mockRepository, mockPrimitiveStore)
    }

    @Test
    fun whenGetStoreListAndDataStoreEmpty_defaultsToAllStoresUsed() {
        val list = storeFilter.getStoreList().blockingGet()
        assertThat(list).isEqualTo(listOf(
            GameStoreFiltered(TEST_STORE_LIST[0], true),
            GameStoreFiltered(TEST_STORE_LIST[1], true),
            GameStoreFiltered(TEST_STORE_LIST[2], true)))
    }

    @Test
    fun whenGetStoreListAndDataStoreNotEmpty_usesStoredFilterData() {
        `when`(mockPrimitiveStore.getStoredStringSet(BBStoreFilter.STORE_FILTER_KEY)).thenReturn(setOf(TEST_STORE_LIST[1].id))

        val list = storeFilter.getStoreList().blockingGet()
        assertThat(list).isEqualTo(listOf(
            GameStoreFiltered(TEST_STORE_LIST[0], true),
            GameStoreFiltered(TEST_STORE_LIST[1], false),
            GameStoreFiltered(TEST_STORE_LIST[2], true)))
    }

    @Test
    fun whenUpdateStore_updatesDataStoreAndEmitsActiveStoreIds() {
        val updatedStore = GameStoreFiltered(TEST_STORE_LIST[1], false)
        storeFilter.updateStore(updatedStore)

        verify(mockPrimitiveStore).storeStringSet(BBStoreFilter.STORE_FILTER_KEY, setOf(TEST_STORE_LIST[1].id))
    }

    @Test
    fun whenUpdateStore_updatesActiveStoreIdsObservable() {
        storeFilter.activeStoresIdsObservable.blockingFirst()
        
        val updatedStore = GameStoreFiltered(TEST_STORE_LIST[1], false)
        `when`(mockPrimitiveStore.getStoredStringSet(BBStoreFilter.STORE_FILTER_KEY)).thenReturn(setOf(TEST_STORE_LIST[1].id))
        storeFilter.updateStore(updatedStore)

        storeFilter.activeStoresIdsObservable.test()
            .assertValue(setOf("id0", "id2"))
    }

    @Test
    fun getActiveStoreIdsObservable_isInitiallyAllStores() {
        val activeIds = storeFilter.activeStoresIdsObservable.blockingFirst()
        assertThat(activeIds).containsExactly("id0", "id1", "id2")
    }
}