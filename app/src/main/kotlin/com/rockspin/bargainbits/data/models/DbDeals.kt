package com.rockspin.bargainbits.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Represents [GameDeal]s stored in the database.
 */
@Entity
data class DbDeals(
    @PrimaryKey
    val key: String,
    val dealIdsCommaSeparated: String
) {

    companion object {
        fun constructKey(sortType: DealSortType, storeIds: Set<String>): String {
            val queryParameter = sortType.queryParameter
            val storeString = storeIds.sorted().joinToString(separator = ",")

            return "$queryParameter;$storeString"
        }
    }
}