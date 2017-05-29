package com.rockspin.bargainbits.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * GameDeal object returned when browsing Cheapshark deals
 */
@Entity
data class GameDeal(
    @PrimaryKey
    @SerializedName("dealID")
    val dealId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("metacriticLink")
    val metacriticLink: String? = null,
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
    val releaseTimestampSeconds: Long,
    @SerializedName("lastChange")
    val lastUpdatedSeconds: Long,
    @SerializedName("dealRating")
    val dealRating: Double,
    @SerializedName("thumb")
    val thumbUrl: String
) {
    @Ignore
    @Transient
    val releaseDate = Date(releaseTimestampSeconds * 1000)

    @Ignore
    @Transient
    val lastUpdatedDate = Date(lastUpdatedSeconds * 1000)
}