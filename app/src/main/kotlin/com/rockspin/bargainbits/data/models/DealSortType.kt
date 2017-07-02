package com.rockspin.bargainbits.data.models

enum class DealSortType(val queryParameter: String) {
    RATING("Deal Rating"),
    STEAM_REVIEWS("Reviews"),
    METACRITIC("Metacritic"),
    RECENT("Recent"),
    RELEASE("Release"),
    SAVINGS("Savings"),
    PRICE("Price")
}