package com.rockspin.bargainbits.data.repository.stores

import android.support.annotation.VisibleForTesting
import com.rockspin.bargainbits.data.BBDatabase
import com.rockspin.bargainbits.data.models.GameStore
import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.utils.NetworkUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

/**
 * Created by valentin.hinov on 24/04/2017.
 */
class BBStoreRepository(
    private val apiService: GameApiService,
    private val database: BBDatabase,
    private val primitiveStore: PrimitiveStore,
    private val networkUtils: NetworkUtils) : StoreRepository {

    companion object {
        @VisibleForTesting internal val BASE_STORE_IMAGE_URL = "https://www.cheapshark.com"
        @VisibleForTesting internal val LAST_CACHED_TIME_KEY = "last_time_stores_cached"
        private val MILLIS_IN_DAY = 86400 * 1000
        @VisibleForTesting internal val MAX_CACHE_TIME = MILLIS_IN_DAY * 2
    }

    private var storeIdMap = mapOf<String, GameStore>()

    private var stores: List<GameStore>? = null

    override fun getGameStoreForId(storeID: String): Single<GameStore> {
        return getStores()
            .flatMapCompletable {
                if (storeIdMap.containsKey(storeID)) {
                    Completable.complete()
                } else {
                    getStores(forceFromServer = true).toCompletable()
                }
            }
            .toSingle { storeIdMap[storeID] }
    }

    override fun getStores(): Single<List<GameStore>> {
        return getStores(forceFromServer = false)
    }

    private fun getStores(forceFromServer: Boolean): Single<List<GameStore>> {
        val cached: Observable<List<GameStore>> = if (stores != null && !forceFromServer) Observable.just(stores) else Observable.empty()
        val fromDatabase = if (isDatabaseDataFresh && !forceFromServer) database.gameStoreDao().getAll().toObservable() else Observable.empty()

        return Observable.concat(listOf(cached, fromDatabase, apiStoresCall.toObservable()))
            .first(emptyList())
            .doOnSuccess {
                storeIdMap = it.map { it.id to it }.toMap()
            }
    }

    private val apiStoresCall: Single<List<GameStore>> = Single.defer {
            apiService.getStores()
                .map { it.filter { it.isActive } }
                .map { it.map { GameStore(it.storeId, it.storeName, "$BASE_STORE_IMAGE_URL${it.imageUrl}") } }
                .doOnSuccess {
                    stores = it
                    database.gameStoreDao().insertAll(it)
                    primitiveStore.storeLong(LAST_CACHED_TIME_KEY, Date().time)
                }
        }

    private val isDatabaseDataFresh: Boolean
    get() {
        // Return from database only if last time it was cached was younger than 2 days ago
        // otherwise, ignore the database result if network is available

        val lastCached = primitiveStore.getStoredLong(LAST_CACHED_TIME_KEY)
        if (lastCached == 0L) {
            return false
        }

        if (!networkUtils.isConnectedToInternet) {
            return true
        }

        val now = Date().time
        val difference = now - lastCached
        return difference < MAX_CACHE_TIME
    }
}