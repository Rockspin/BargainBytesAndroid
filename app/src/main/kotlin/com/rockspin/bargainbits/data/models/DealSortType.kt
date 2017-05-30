package com.rockspin.bargainbits.data.models

/**
 * Created by valentin.hinov on 22/05/2017.
 */
enum class DealSortType(val queryParameter: String) {
    RATING("Deal Rating"),
    STEAM_REVIEWS("Reviews"),
    METACRITIC("Metacritic"),
    RECENT("Recent"),
    RELEASE("Release"),
    SAVINGS("Savings"),
    PRICE("Price")
}