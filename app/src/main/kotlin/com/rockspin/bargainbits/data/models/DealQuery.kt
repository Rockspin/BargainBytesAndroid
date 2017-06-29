package com.rockspin.bargainbits.data.models

import com.rockspin.bargainbits.data.rest_client.GameApiService

/**
 * Represents the data necessary to perform a [GameApiService.getDeals] API operation.
 */
data class DealQuery(
    val onSale: Boolean,
    val sortBy: DealSortType,
    val storeIds: Set<String>
) {
    val paramMap = mapOf(
        "onSale" to if(onSale) "1" else "0",
        "sortBy" to sortBy.queryParameter,
        "storeID" to storeIds.joinToString(separator = ",")
    )
}