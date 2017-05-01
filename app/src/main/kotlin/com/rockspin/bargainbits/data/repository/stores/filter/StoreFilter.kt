package com.rockspin.bargainbits.data.repository.stores.filter

import io.reactivex.Single

/**
 * Contains logic for getting and managing the active store filter
 * used when fetching deals.
 */
interface StoreFilter {

    fun getStoreList(): Single<List<GameStoreFiltered>>

    /**
     * Finds an instance of [GameStoreFiltered] with the same StoreId and replaces it with the one passed in
     */
    fun updateStore(gameStoreFiltered: GameStoreFiltered)
}