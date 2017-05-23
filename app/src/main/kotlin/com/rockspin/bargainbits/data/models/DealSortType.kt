package com.rockspin.bargainbits.data.models

/**
 * Created by valentin.hinov on 22/05/2017.
 */
enum class DealSortType(val queryParameter: String) {
    RATING("Deal Rating"),
    TITLE("Title"),
    SAVINGS("Savings"),
    PRICE("Price"),
    METACRITIC("Metacritic"),
    REVIEWS("Reviews"),
    RELEASE("Release"),
    STORE("Store"),
    RECENT("recent")
}