package com.rockspin.bargainbits.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.rockspin.bargainbits.data.models.*
import com.rockspin.bargainbits.data.models.daos.DbDealsDao
import com.rockspin.bargainbits.data.models.daos.GameDealDao
import com.rockspin.bargainbits.data.models.daos.GameStoreDao

/**
 * Created by valentin.hinov on 19/05/2017.
 */
@Database(entities = arrayOf(GameStore::class, DbDeals::class, GameDeal::class), version = 1)
abstract class BBDatabase : RoomDatabase() {
    abstract fun gameStoreDao(): GameStoreDao
    abstract fun dbDealsDao(): DbDealsDao
    abstract fun gameDealDao(): GameDealDao
}