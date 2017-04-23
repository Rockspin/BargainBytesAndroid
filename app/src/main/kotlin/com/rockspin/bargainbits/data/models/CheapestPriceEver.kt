package com.rockspin.bargainbits.data.models

import com.google.gson.annotations.SerializedName

data class CheapestPriceEver(
    @SerializedName("price")
    val price: Double,
    @SerializedName("date")
    val dateTimestamp: Int
)
