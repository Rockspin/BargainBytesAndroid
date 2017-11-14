package com.rockspin.bargainbits.deals

import com.rockspin.bargainbits.data.models.GameDeal
import com.rockspin.bargainbits.data.rest_client.GameApiService
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

class DealsRepositoryTest {

    val gameService: GameApiService = Mockito.mock(GameApiService::class.java)

    val dealsRepository: DealsRepository = DealsRepository(gameService)

    @Test
    fun whenLoadThreeUniqueDeals_threeGamesWithOneDealEach() {
        val one: GameDeal = GameDealBuilder(
                testDealId = "10",
                testTitle = "One",
                testGameId = "1",
                testThumbUrl = "ThumbOne",
                testDealRating = 60.00,
                testNormalPrice = 29.99,
                testSalePrice = 19.99).build()
        val two: GameDeal = GameDealBuilder(
                testDealId = "21",
                testTitle = "Two",
                testGameId = "2",
                testThumbUrl = "ThumbTwo",
                testDealRating = 70.00,
                testNormalPrice = 69.99,
                testSalePrice = 59.99).build()
        val three: GameDeal = GameDealBuilder(
                testDealId = "31",
                testTitle = "Three",
                testGameId = "3",
                testThumbUrl = "ThumbThree",
                testDealRating = 80.00,
                testNormalPrice = 129.99,
                testSalePrice = 119.99).build()
        `when`(gameService.getDeals()).thenReturn(Single.just(Arrays.asList(one, two, three)))

        val games: Single<List<Game>> = dealsRepository.deals()

        // assert
        val expected1 = Game(
                "1",
                "One",
                "ThumbOne",
                Arrays.asList(Deal("10", 60.00, 29.99, 19.99)))
        val expected2 = Game(
                "2",
                "Two",
                "ThumbTwo",
                Arrays.asList(Deal("21", 70.00, 69.99, 59.99)))
        val expected3 = Game(
                "3",
                "Three",
                "ThumbThree",
                Arrays.asList(Deal("31", 80.00, 129.99, 119.99)))
        val test = games.test()

        test.assertResult(Arrays.asList(expected1, expected2, expected3))
    }

    @Test
    fun whenTwoApiDealsHaveSameGame_shouldProduceGameWithTwoDeals() {
        val one: GameDeal = GameDealBuilder(
                testDealId = "10",
                testTitle = "One",
                testGameId = "1",
                testThumbUrl = "ThumbOne",
                testDealRating = 60.00,
                testNormalPrice = 29.99,
                testSalePrice = 19.99).build()
        val two: GameDeal = GameDealBuilder(
                testDealId = "21",
                testTitle = "Two",
                testGameId = "2",
                testThumbUrl = "ThumbTwo",
                testDealRating = 70.00,
                testNormalPrice = 69.99,
                testSalePrice = 59.99).build()
        val three: GameDeal = GameDealBuilder(
                testDealId = "31",
                testTitle = "Three",
                testGameId = "3",
                testThumbUrl = "ThumbThree",
                testDealRating = 80.00,
                testNormalPrice = 129.99,
                testSalePrice = 119.99).build()
        val four: GameDeal = GameDealBuilder(
                testDealId = "41",
                testTitle = "One",
                testGameId = "1",
                testThumbUrl = "ThumbOne",
                testDealRating = 50.00,
                testNormalPrice = 100.00,
                testSalePrice = 50.00).build()
        `when`(gameService.getDeals()).thenReturn(Single.just(Arrays.asList(one, two, three, four)))

        val games: Single<List<Game>> = dealsRepository.deals()

        // assert
        val expected1 = Game(
                "1",
                "One",
                "ThumbOne",
                Arrays.asList(
                        Deal("10", 60.00, 29.99, 19.99),
                        Deal("41", 50.00, 100.00, 50.00)))
        val expected2 = Game(
                "2",
                "Two",
                "ThumbTwo",
                Arrays.asList(Deal("21", 70.00, 69.99, 59.99)))
        val expected3 = Game(
                "3",
                "Three",
                "ThumbThree",
                Arrays.asList(Deal("31", 80.00, 129.99, 119.99)))
        val test = games.test()

        test.assertResult(Arrays.asList(expected1, expected2, expected3))
    }

    data class GameDealBuilder(
            private val testDealId: String = "12345",
            private val testTitle: String = "title",
            private val testGameId: String = "OneTwoThreeFourFive",
            private val testSalePrice: Double = 12.99,
            private val testNormalPrice: Double = 29.99,
            private val testSavingsPercentage: Double = 78.67,
            private val testDealRating: Double = 78.89,
            private val testThumbUrl: String = "http://www.url.com"

    ) {
        fun build() : GameDeal {
            return GameDeal(testDealId, testTitle, "", "", testGameId, testSalePrice,
                    testNormalPrice, testSavingsPercentage, 0, "",
                    0, 0, 0L,
                    0L, testDealRating, testThumbUrl)
        }
    }
}