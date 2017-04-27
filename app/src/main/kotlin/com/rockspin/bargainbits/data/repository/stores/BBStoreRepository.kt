package com.rockspin.bargainbits.data.repository.stores

import android.support.annotation.VisibleForTesting
import com.rockspin.bargainbits.data.rest_client.GameApiService
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by valentin.hinov on 24/04/2017.
 */
class BBStoreRepository(val apiService: GameApiService) : StoreRepository {

    companion object {
        // Note (Val): Is this the best place to have this? Should there be a separate class that constructs the store image url?
        @VisibleForTesting val BASE_STORE_IMAGE_URL = "https://www.cheapshark.com"
    }

    private var storeIdMap = mapOf<String, GameStore>()

    private var cachedStores: List<GameStore>? = null

    override fun getGameStoreForId(storeID: String): Single<GameStore> {
        return getStores()
            .map { storeIdMap[storeID] }
    }

    fun getStores(): Single<List<GameStore>> {
        val cached: Observable<List<GameStore>> = if (cachedStores != null) Observable.just(cachedStores) else Observable.empty()

        return Observable.concat(listOf(cached, apiStoresCall.toObservable()))
            .first(emptyList())
    }

    private val apiStoresCall: Single<List<GameStore>>
        get() {
            return Single.defer {
                apiService.getStores()
                    .map { it.filter { it.isActive } }
                    .map {
                        storeIdMap = it.map { it.storeId to GameStore(it.storeName, "$BASE_STORE_IMAGE_URL${it.imageUrl}") }.toMap()
                        storeIdMap.values.toList()
                    }
                    .doOnSuccess { cachedStores = it }
            }
        }
}