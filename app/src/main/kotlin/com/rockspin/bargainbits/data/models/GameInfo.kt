package com.rockspin.bargainbits.data.models

import com.google.gson.annotations.SerializedName

data class GameInfo(
    @SerializedName("info")
    val info: Info,
    @SerializedName("cheapestPriceEver")
    var cheapestPriceEver: CheapestPriceEver? = null,
    @SerializedName("deals")
    val deals: List<AbbreviatedDeal>
) {
    val lowestSalePrice: Double
        get() {
            val lowestSalePrice = deals
                .map { it.price }
                .min()
                ?: Double.MAX_VALUE

            return lowestSalePrice
        }

    val highestNormalPrice: Double
        get() {
            val highestNormalPrice = deals
                .map { it.retailPrice }
                .max()
                ?: 0.0

            return highestNormalPrice
        }

    val topSavingsFraction: Double
        get() {
            val topSavings = deals
                .map { it.savingsFraction }
                .max()
                ?: 0.0

            return topSavings
        }

    val isOnSale: Boolean
        get() {
            return deals.any { it.savingsFraction > 0.0 }
        }
}
