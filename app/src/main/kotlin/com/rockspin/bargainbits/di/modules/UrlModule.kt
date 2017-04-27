package com.rockspin.bargainbits.di.modules

import com.rockspin.bargainbits.di.annotations.GameApiUrl
import com.rockspin.bargainbits.di.annotations.GameDealUrl

import dagger.Module
import dagger.Provides

@Module
class UrlModule {

    companion object {
        private val BASE_CHEAPSHARK_URL = "https://www.cheapshark.com"
        private val BASE_CHEAPSHARK_API_URL = "$BASE_CHEAPSHARK_URL/api/1.0"
        private val BASE_CHEAPSHARK_DEAL_URL = "$BASE_CHEAPSHARK_URL/redirect?dealID="
    }

    @Provides @GameApiUrl fun providesGameApiUrl(): String {
        return BASE_CHEAPSHARK_API_URL
    }

    @Provides @GameDealUrl fun providesGameDealUrl(): String {
        return BASE_CHEAPSHARK_DEAL_URL
    }
}
