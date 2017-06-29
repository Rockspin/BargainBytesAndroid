package com.rockspin.bargainbits.data.models.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.rockspin.bargainbits.data.models.GameStore
import io.reactivex.Flowable

@Dao
interface GameStoreDao {

    @Query("SELECT * FROM gamestore")
    fun getAll(): Flowable<List<GameStore>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stores: List<GameStore>)
}