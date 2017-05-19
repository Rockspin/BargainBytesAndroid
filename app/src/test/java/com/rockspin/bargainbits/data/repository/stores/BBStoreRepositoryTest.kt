package com.rockspin.bargainbits.data.repository.stores

import com.nhaarman.mockito_kotlin.*
import com.rockspin.bargainbits.data.BBDatabase
import com.rockspin.bargainbits.data.models.GameStore
import com.rockspin.bargainbits.data.models.GameStoreDao
import com.rockspin.bargainbits.data.models.Store
import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.utils.NetworkUtils
import io.reactivex.Flowable
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.leq
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class BBStoreRepositoryTest {

    companion object {
        private val TEST_API_STORES = listOf(
            Store("TestStoreId0", "TestStoreName0", 1, createImagesWithLogoUrl("TestImageUrl0")),
            Store("TestStoreId1", "TestStoreName1", 0, createImagesWithLogoUrl("TestImageUrl1")),
            Store("TestStoreId2", "TestStoreName2", 1, createImagesWithLogoUrl("TestImageUrl2"))
        )

        private val TEST_GAME_STORES = listOf(
            GameStore("TestStoreId0", "TestStoreName0", "${BBStoreRepository.BASE_STORE_IMAGE_URL}TestImageUrl0"),
            GameStore("TestStoreId2", "TestStoreName2", "${BBStoreRepository.BASE_STORE_IMAGE_URL}TestImageUrl2")
        )

        private fun createImagesWithLogoUrl(url: String): Store.Images {
            return Store.Images("", url, "")
        }
    }

    @Mock
    lateinit var mockService: GameApiService

    @Mock
    lateinit var mockDao: GameStoreDao

    @Mock
    lateinit var mockPrimitiveStore: PrimitiveStore

    @Mock
    lateinit var mockNetworkUtils: NetworkUtils

    private lateinit var storeRepository: BBStoreRepository

    @Before
    fun setUp() {
        val mockDatabase: BBDatabase = mock()
        whenever(mockDatabase.gameStoreDao()).thenReturn(mockDao)
        whenever(mockService.getStores()).thenReturn(Single.just(TEST_API_STORES))
        whenever(mockDao.getAll()).thenReturn(Flowable.empty())
        whenever(mockPrimitiveStore.getStoredLong(BBStoreRepository.LAST_CACHED_TIME_KEY)).thenReturn(0)
        whenever(mockNetworkUtils.isConnectedToInternet).thenReturn(true)

        storeRepository = BBStoreRepository(mockService, mockDatabase, mockPrimitiveStore, mockNetworkUtils)
    }

    @Test
    fun whenGetStoresNotCachedAndNotInDatabase_getStoresFetchesFromApiAndFiltersInvalidStores() {
        val storeList = storeRepository.getStores().blockingGet()
        assertThat(storeList).isEqualTo(TEST_GAME_STORES)
    }

    @Test
    fun whenGetStoresNotCachedButInDatabaseAndFresh_getStoresFromDatabase() {
        val testDbStores = listOf(
            GameStore("TestStoreId0", "TestStoreName0", "${BBStoreRepository.BASE_STORE_IMAGE_URL}TestImageUrl0")
        )

        whenever(mockPrimitiveStore.getStoredLong(BBStoreRepository.LAST_CACHED_TIME_KEY)).thenReturn(Date().time)
        whenever(mockDao.getAll()).thenReturn(Flowable.just(testDbStores))
        val storeList = storeRepository.getStores().blockingGet()
        assertThat(storeList).isEqualTo(testDbStores)
    }

    @Test
    fun whenGetStoresNotCachedButInDatabaseAndResultsAreOld_getStoresFromApi() {
        val oldCacheTime = BBStoreRepository.MAX_CACHE_TIME + 1
        val outdatedTimestamp = Date().time - oldCacheTime
        whenever(mockPrimitiveStore.getStoredLong(BBStoreRepository.LAST_CACHED_TIME_KEY)).thenReturn(outdatedTimestamp)

        val storeList = storeRepository.getStores().blockingGet()

        assertThat(storeList).isEqualTo(TEST_GAME_STORES)
        verify(mockDao).insertAll(TEST_GAME_STORES)
    }

    @Test
    fun whenGetStoresNotCachedButInDatabaseAndResultsAreOldAndNotConnectedToInternet_getStoresFromDatabase() {
        val testDbStores = listOf(
            GameStore("TestStoreId0", "TestStoreName0", "${BBStoreRepository.BASE_STORE_IMAGE_URL}TestImageUrl0")
        )
        whenever(mockDao.getAll()).thenReturn(Flowable.just(testDbStores))

        val oldCacheTime = BBStoreRepository.MAX_CACHE_TIME + 1
        val outdatedTimestamp = Date().time - oldCacheTime
        whenever(mockPrimitiveStore.getStoredLong(BBStoreRepository.LAST_CACHED_TIME_KEY)).thenReturn(outdatedTimestamp)
        whenever(mockNetworkUtils.isConnectedToInternet).thenReturn(false)

        val storeList = storeRepository.getStores().blockingGet()

        assertThat(storeList).isEqualTo(testDbStores)
    }

    @Test
    fun whenGetStoresCached_getStoresReturnsCache() {
        storeRepository.getStores().blockingGet()
        verify(mockService).getStores()

        val storeList = storeRepository.getStores().blockingGet()
        assertThat(storeList).isEqualTo(TEST_GAME_STORES)

        verifyNoMoreInteractions(mockService)
    }

    @Test
    fun whenGetStoresNotCachedAndNotInDatabase_getStoresStoresInDatabaseAndRecordsTimestamp() {
        storeRepository.getStores().blockingGet()
        verify(mockDao).insertAll(TEST_GAME_STORES)
        verify(mockPrimitiveStore).storeLong(eq(BBStoreRepository.LAST_CACHED_TIME_KEY), leq(Date().time))
    }

    @Test
    fun getGameStoreForId_returnsCorrectStore() {
        val gameStore = storeRepository.getGameStoreForId(TEST_API_STORES[0].storeId).blockingGet()

        assertThat(gameStore).isEqualTo(TEST_GAME_STORES[0])
    }

    @Test
    fun whenGetGameStoreForIdUnknownId_refetchStoresFromApi() {
        val newApiStores = listOf(
            Store("TestStoreId3", "TestStoreName3", 1, createImagesWithLogoUrl("TestImageUrl3"))
        )

        val expectedStore = GameStore("TestStoreId3", "TestStoreName3", "${BBStoreRepository.BASE_STORE_IMAGE_URL}TestImageUrl3")

        whenever(mockService.getStores()).thenReturn(Single.just(newApiStores))

        storeRepository.getGameStoreForId("TestStoreId3").blockingGet()
        assertThat(expectedStore).isEqualTo(expectedStore)
        verify(mockService).getStores()
    }
}