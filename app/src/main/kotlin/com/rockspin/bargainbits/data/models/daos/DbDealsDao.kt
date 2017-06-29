package com.rockspin.bargainbits.data.models.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.rockspin.bargainbits.data.models.DbDeals
import io.reactivex.Flowable

@Dao
interface DbDealsDao {

    @Query("SELECT * FROM dbdeals WHERE key = (:p0) LIMIT 1")
    fun getDbDeals(key: String): Flowable<DbDeals>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbDeals: DbDeals)
}