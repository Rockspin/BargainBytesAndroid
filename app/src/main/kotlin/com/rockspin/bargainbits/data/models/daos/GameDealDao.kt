package com.rockspin.bargainbits.data.models.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.rockspin.bargainbits.data.models.GameDeal
import io.reactivex.Flowable

@Dao
interface GameDealDao {

    @Query("SELECT * FROM gamedeal where dealId IN (:p0)")
    fun getDealsById(p0: List<String>): Flowable<List<GameDeal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(deals: List<GameDeal>)
}