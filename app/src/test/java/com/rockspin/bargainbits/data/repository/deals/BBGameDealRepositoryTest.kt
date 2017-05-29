package com.rockspin.bargainbits.data.repository.deals

import com.nhaarman.mockito_kotlin.*
import com.rockspin.bargainbits.data.BBDatabase
import com.rockspin.bargainbits.data.models.*
import com.rockspin.bargainbits.data.models.daos.GameStoreDao
import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.repository.stores.BBStoreRepository
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
class BBGameDealRepositoryTest {

    companion object {
        val TEST_DEALS = listOf(
            createTestGameDeal(dealId = "dealId0", gameId = "testGameId0", title = "TestTitle0", storeId = "0"),
            createTestGameDeal(dealId = "dealId1", gameId = "testGameId1", title = "TestTitle1", storeId = "0"),
            createTestGameDeal(dealId = "dealId2", gameId = "testGameId0", title = "TestTitle0", storeId = "1"),
            createTestGameDeal(dealId = "dealId3", gameId = "testGameId1", title = "TestTitle1", storeId = "4"),
            createTestGameDeal(dealId = "dealId4", gameId = "testGameId2", title = "TestTitle2", storeId = "2"))

        val EXPECTED_GROUPED_DEALS = listOf(
            GroupedGameDeal(listOf(TEST_DEALS[0], TEST_DEALS[2])),
            GroupedGameDeal(listOf(TEST_DEALS[1], TEST_DEALS[3])),
            GroupedGameDeal(listOf(TEST_DEALS[4])))

        fun createTestGameDeal(dealId: String, title: String, storeId: String, gameId: String): GameDeal {
            return GameDeal(dealId = dealId, title = title, storeId = storeId, gameId = gameId,
                salePrice = 0.0, normalPrice = 0.0, metacriticScore = 0, steamRatingCount = 0, steamRatingPercent = 0,
                releaseTimestampSeconds = 0, lastUpdatedSeconds = 0, dealRating = 0.0, thumbUrl = "", savingsPercentage = 0.0)
        }
    }

    @Mock
    lateinit var mockService: GameApiService

    private lateinit var gameDealRepository: BBGameDealRepository

    @Before
    fun setUp() {
        val mockDatabase: BBDatabase = mock()
        whenever(mockService.getDeals(any())).thenReturn(Single.just(TEST_DEALS))

        gameDealRepository = BBGameDealRepository(mockService, mockDatabase)
    }

    @Test
    fun whenGetDealsFromApi_constructsCorrectParams() {
        gameDealRepository.getDeals(DealSortType.TITLE, setOf("2", "7", "4")).blockingGet()

        verify(mockService).getDeals(mapOf(
            "onSale" to "1",
            "sortBy" to DealSortType.TITLE.queryParameter,
            "storeID" to "2,4,7"))
    }

    @Test
    fun whenGetDeals_groupsDealsCorrectly() {
        val groupedDeals = gameDealRepository.getDeals(DealSortType.TITLE, emptySet()).blockingGet()

        assertThat(groupedDeals).isEqualTo(EXPECTED_GROUPED_DEALS)
    }
}