package com.rockspin.bargainbits.data.models

import com.google.gson.annotations.SerializedName

/**
 * Abbreviated deal.
 */
data class AbbreviatedDeal(
    @SerializedName("storeID")
    val storeID: String? = null,
    @SerializedName("dealID")
    val dealID: String? = null,
    @SerializedName("price")
    val price: Double,
    @SerializedName("retailPrice")
    var retailPrice: Double
) {
    val savingsFraction: Double
        get() {
            val fraction = price / retailPrice
            return 1.0 - fraction
        }
}
