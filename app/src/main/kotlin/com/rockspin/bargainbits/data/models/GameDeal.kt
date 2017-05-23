package com.rockspin.bargainbits.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * GameDeal object returned when browsing Cheapshark deals
 */
data class GameDeal(
    @SerializedName("title")
    val title: String,
    @SerializedName("metacriticLink")
    val metacriticLink: String? = null,
    @SerializedName("dealID")
    val dealId: String,
    @SerializedName("storeID")
    val storeId: String,
    @SerializedName("gameID")
    val gameId: String,
    @SerializedName("salePrice")
    val salePrice: Double,
    @SerializedName("normalPrice")
    val normalPrice: Double,
    @SerializedName("savings")
    val savingsPercentage: Double,
    @SerializedName("metacriticScore")
    val metacriticScore: Int,
    @SerializedName("steamRatingText")
    val steamRatingText: String? = null,
    @SerializedName("steamRatingPercent")
    val steamRatingPercent: Int,
    @SerializedName("steamRatingCount")
    val steamRatingCount: Int,
    @SerializedName("releaseDate")
    private val releaseTimestampSeconds: Long,
    @SerializedName("lastChange")
    private val lastUpdatedSeconds: Long,
    @SerializedName("dealRating")
    val dealRating: Double,
    @SerializedName("thumb")
    val thumbUrl: String
) {
    val releaseDate by lazy {
        Date(releaseTimestampSeconds * 1000)
    }

    val lastUpdatedDate by lazy {
        Date(lastUpdatedSeconds * 1000)
    }
}