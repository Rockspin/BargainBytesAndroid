package com.rockspin.bargainbits.data.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DbDealsTest {

    @Test
    fun testConstructKey() {
        val sortType = DealSortType.PRICE
        val stores = setOf("5", "1", "3", "2", "15")

        val key = DbDeals.constructKey(sortType, stores)
        assertThat(key).isEqualTo("Price;1,15,2,3,5")
    }
}