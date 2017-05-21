package com.rockspin.bargainbits.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.rockspin.bargainbits.data.models.GameStore
import com.rockspin.bargainbits.data.models.GameStoreDao

/**
 * Created by valentin.hinov on 19/05/2017.
 */
@Database(entities = arrayOf(GameStore::class), version = 1)
abstract class BBDatabase : RoomDatabase() {
    abstract fun gameStoreDao(): GameStoreDao
}