package com.rockspin.bargainbits.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Store object holding only the components of [Store] that the app uses.
 */
@Entity
data class GameStore(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String
)