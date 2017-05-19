package com.rockspin.bargainbits.data.repository.stores

import com.rockspin.bargainbits.data.models.GameStore
import io.reactivex.Single

/**
 * Created by valentin.hinov on 24/04/2017.
 */
interface StoreRepository {
    fun getGameStoreForId(storeID: String): Single<GameStore>

    fun getStores(): Single<List<GameStore>>
}