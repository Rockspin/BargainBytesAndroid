package com.rockspin.bargainbits.di.modules

import android.content.SharedPreferences
import com.google.gson.Gson
import com.rockspin.bargainbits.data.repository.storage.BBObjectStore
import com.rockspin.bargainbits.data.repository.storage.BBPrimitiveStore
import com.rockspin.bargainbits.data.repository.storage.ObjectStore
import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.repository.stores.BBStoreRepository
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.repository.stores.filter.BBStoreFilter
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.data.rest_client.GameApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by valentin.hinov on 24/04/2017.
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun providesStoreRepository(gameApiService: GameApiService): StoreRepository {
        return BBStoreRepository(gameApiService)
    }

    @Provides
    @Singleton
    internal fun providesStoreFilter(storeRepository: StoreRepository, primitiveStore: PrimitiveStore): StoreFilter {
        return BBStoreFilter(storeRepository, primitiveStore)
    }

    @Provides
    @Singleton
    internal fun providesDataStore(gson: Gson, primitiveStore: PrimitiveStore): ObjectStore {
        return BBObjectStore(gson, primitiveStore)
    }

    @Provides
    @Singleton
    internal fun primitiveStore(sharedPreferences: SharedPreferences): PrimitiveStore {
        return BBPrimitiveStore(sharedPreferences)
    }
}