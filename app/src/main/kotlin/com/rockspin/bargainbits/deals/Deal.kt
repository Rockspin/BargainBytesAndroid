package com.rockspin.bargainbits.deals


data class Deal(
        var dealId: String,
        var dealRating: Double,
        var wasPrice: Double,
        var nowPrice: Double)