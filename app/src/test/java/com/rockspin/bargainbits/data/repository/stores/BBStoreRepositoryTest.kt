package com.rockspin.bargainbits.data.repository.stores

import com.rockspin.bargainbits.data.models.Store
import com.rockspin.bargainbits.data.rest_client.GameApiService
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BBStoreRepositoryTest {

    companion object {
        private val TEST_API_STORES = listOf(
            Store("TestStoreId0", "TestStoreName0", 1, createImagesWithLogoUrl("TestImageUrl0")),
            Store("TestStoreId1", "TestStoreName1", 0, createImagesWithLogoUrl("TestImageUrl1")),
            Store("TestStoreId2", "TestStoreName2", 1, createImagesWithLogoUrl("TestImageUrl2"))
        )

        private val TEST_GAME_STORES = listOf(
            GameStore("TestStoreName0", "${BBStoreRepository.BASE_STORE_IMAGE_URL}TestImageUrl0"),
            GameStore("TestStoreName2", "${BBStoreRepository.BASE_STORE_IMAGE_URL}TestImageUrl2")
        )

        private fun createImagesWithLogoUrl(url: String): Store.Images {
            return Store.Images("", url, "")
        }
    }

    @Mock
    lateinit var mockService: GameApiService

    private lateinit var storeRepository: BBStoreRepository

    @Before
    fun setUp() {
        storeRepository = BBStoreRepository(mockService)
        `when`(mockService.getStores()).thenReturn(Single.just(TEST_API_STORES))
    }

    @Test
    fun whenGetStoresNotCached_getStoresFetchesFromApiAndFiltersInvalidStores() {
        val storeList = storeRepository.getStores().blockingGet()
        assertThat(storeList).isEqualTo(TEST_GAME_STORES)
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
    fun getGameStoreForId_returnsCorrectStore() {
        val gameStore = storeRepository.getGameStoreForId(TEST_API_STORES[0].storeId).blockingGet()

        assertThat(gameStore).isEqualTo(TEST_GAME_STORES[0])
    }
}