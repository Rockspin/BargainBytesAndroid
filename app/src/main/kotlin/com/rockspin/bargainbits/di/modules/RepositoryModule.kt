package com.rockspin.bargainbits.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import com.rockspin.apputils.di.annotations.ApplicationScope
import com.rockspin.bargainbits.data.BBDatabase
import com.rockspin.bargainbits.data.repository.deals.BBGameDealRepository
import com.rockspin.bargainbits.data.repository.deals.GameDealRepository
import com.rockspin.bargainbits.data.repository.storage.BBPrimitiveStore
import com.rockspin.bargainbits.data.repository.storage.PrimitiveStore
import com.rockspin.bargainbits.data.repository.stores.BBStoreRepository
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.repository.stores.filter.BBStoreFilter
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.utils.NetworkUtils
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
    internal fun providesDatabase(@ApplicationScope context: Context): BBDatabase {
        return Room.databaseBuilder(context, BBDatabase::class.java, "bb-database").build()
    }

    @Provides
    @Singleton
    internal fun providesGameDealRepository(gameApiService: GameApiService, database: BBDatabase, networkUtils: NetworkUtils): GameDealRepository {
        return BBGameDealRepository(gameApiService, database, networkUtils)
    }

    @Provides
    @Singleton
    internal fun providesStoreRepository(gameApiService: GameApiService, database: BBDatabase, primitiveStore: PrimitiveStore, networkUtils: NetworkUtils):
        StoreRepository {
        return BBStoreRepository(gameApiService, database, primitiveStore, networkUtils)
    }

    @Provides
    @Singleton
    internal fun providesStoreFilter(storeRepository: StoreRepository, primitiveStore: PrimitiveStore): StoreFilter {
        return BBStoreFilter(storeRepository, primitiveStore)
    }

    @Provides
    @Singleton
    internal fun primitiveStore(sharedPreferences: SharedPreferences): PrimitiveStore {
        return BBPrimitiveStore(sharedPreferences)
    }
}