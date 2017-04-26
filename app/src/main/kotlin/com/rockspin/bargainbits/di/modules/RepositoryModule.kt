package com.rockspin.bargainbits.di.modules

import com.rockspin.bargainbits.data.repository.stores.BBStoreRepository
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
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
}