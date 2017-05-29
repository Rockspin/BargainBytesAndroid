package com.rockspin.bargainbits.data.models

/**
 * Object that represents a collection of [GameDeal]s grouped by gameId.
 */
data class GroupedGameDeal(private val deals: List<GameDeal>) {

    val title = deals[0].title
    val metacriticLink = deals[0].metacriticLink
    val storeIds = deals.map { it.storeId }
    val gameId = deals[0].gameId
    val salePrice = deals.minBy { it.salePrice }!!.salePrice
    val normalPrice = deals.maxBy { it.normalPrice }!!.normalPrice
    val savingsPercentage = deals.maxBy { it.savingsPercentage }!!.savingsPercentage
    val metacriticScore = deals[0].metacriticScore
    val steamRatingPercent = deals[0].steamRatingPercent
    val steamRatingCount = deals[0].steamRatingCount
    val dealRating = deals.maxBy { it.dealRating }
    val thumbUrl = deals[0].thumbUrl
    val releaseDate = deals[0].releaseDate
    val lastUpdatedDate = deals[0].lastUpdatedDate

    fun dealIdAtIndex(index: Int) = deals[index].dealId
}