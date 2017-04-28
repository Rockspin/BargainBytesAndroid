package com.rockspin.bargainbits.data.models

import com.google.gson.annotations.SerializedName

/**
 * Abbreviated deal.
 */
data class AbbreviatedDeal(
    @SerializedName("storeID")
    val storeID: String,
    @SerializedName("dealID")
    val dealID: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("retailPrice")
    var retailPrice: Double,
    @SerializedName("savings")
    val savingsFraction: Double
)
