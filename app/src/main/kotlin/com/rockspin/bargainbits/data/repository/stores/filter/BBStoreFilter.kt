package com.rockspin.bargainbits.data.repository.stores.filter

import android.support.annotation.VisibleForTesting
import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import io.reactivex.Single

class BBStoreFilter(private val storeRepository: StoreRepository, private val primitiveStore: PrimitiveStore) : StoreFilter {

    companion object {
        @VisibleForTesting val STORE_FILTER_KEY = "STORE_FILTER"
    }

    override fun getStoreList(): Single<List<GameStoreFiltered>> {
        return storeRepository.getStores()
            .map { storeList ->
                val filteredStoreIdSet = primitiveStore.getStoredStringSet(STORE_FILTER_KEY)
                storeList.map {
                    val isUsed = !filteredStoreIdSet.contains(it.id)
                    GameStoreFiltered(it, isUsed)
                }
            }
    }

    override fun updateStore(gameStoreFiltered: GameStoreFiltered) {
        val filteredStoreIdSet = primitiveStore.getStoredStringSet(STORE_FILTER_KEY)
        val mutable = filteredStoreIdSet.toMutableSet()
        if (gameStoreFiltered.isUsed) mutable.remove(gameStoreFiltered.id) else mutable.add(gameStoreFiltered.id)

        primitiveStore.storeStringSet(STORE_FILTER_KEY, mutable)
    }
}