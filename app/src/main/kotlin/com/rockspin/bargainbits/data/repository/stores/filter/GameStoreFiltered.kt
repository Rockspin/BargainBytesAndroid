package com.rockspin.bargainbits.data.repository.stores.filter

import com.rockspin.bargainbits.data.models.GameStore

data class GameStoreFiltered(
    private val gameStore: GameStore,
    val isUsed: Boolean)
{
    val id: String
    get() = gameStore.id

    val storeName: String
    get() = gameStore.name

    val imageUrl: String
    get() = gameStore.imageUrl

    fun withState(isUsed: Boolean): GameStoreFiltered {
        return GameStoreFiltered(gameStore, isUsed)
    }
}