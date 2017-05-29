package com.rockspin.bargainbits.data.repository.stores.filter

import android.support.annotation.VisibleForTesting
import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class BBStoreFilter(private val storeRepository: StoreRepository, private val primitiveStore: PrimitiveStore) : StoreFilter {

    companion object {
        @VisibleForTesting val STORE_FILTER_KEY = "STORE_FILTER"
    }

    private val activeStoreIdsPS = PublishSubject.create<Set<String>>()
    private var activeStoreIds = emptySet<String>()

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
        val newFilteredSet: Set<String>

        if (gameStoreFiltered.isUsed) {
            newFilteredSet = filteredStoreIdSet.minus(gameStoreFiltered.id)
            activeStoreIds = activeStoreIds.plus(gameStoreFiltered.id)
        } else {
            newFilteredSet = filteredStoreIdSet.plus(gameStoreFiltered.id)
            activeStoreIds = activeStoreIds.minus(gameStoreFiltered.id)
        }

        primitiveStore.storeStringSet(STORE_FILTER_KEY, newFilteredSet)
        activeStoreIdsPS.onNext(activeStoreIds)
    }

    override val activeStoresIdsObservable: Observable<Set<String>>
        get() = activeStoreIdsPS
            .startWith(getStoreList().toObservable()
                .map { gameStoresFiltered ->
                    gameStoresFiltered.filter { it.isUsed }.map { it.id }.toSet()
                }
                .doOnNext {
                    activeStoreIds = it
                }
            )
}