package com.rockspin.bargainbits.di.modules

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.rockspin.apputils.di.annotations.ApplicationScope
import com.rockspin.bargainbits.data.models.cheapshark.GameInfo
import com.rockspin.bargainbits.data.models.currency.CurrencyExchange
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.data.rest_client.ICheapsharkAPIService
import com.rockspin.bargainbits.data.rest_client.ICurrencyAPIService
import com.rockspin.bargainbits.di.annotations.CheapsharkUrl
import com.rockspin.bargainbits.di.annotations.CurrencyUrl
import com.rockspin.bargainbits.services.Services
import com.rockspin.bargainbits.utils.analytics.IAnalytics
import com.rockspin.bargainbits.utils.environment.IServices
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit.RestAdapter
import retrofit.client.OkClient
import retrofit.converter.GsonConverter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class APIServiceModule {

    companion object {
        private val OLD_HTTP_CACHE_NAME = "BB_HTTP_CACHE"
        private val NEW_HTTP_CACHE_NAME = "NEW_BB_HTTP_CACHE"
        private val DISC_CACHE_SIZE_MB = 10
    }

    private val services = Services()

    @Deprecated("Use new OkHttp cache")
    @Provides
    @Singleton
    internal fun providesOldCache(@ApplicationScope pContext: Context): com.squareup.okhttp.Cache {
        return com.squareup.okhttp.Cache(File(pContext.filesDir
            .path + OLD_HTTP_CACHE_NAME), (DISC_CACHE_SIZE_MB * 1024 * 1024).toLong())
    }

    @Deprecated("Use new OkHttp client instead")
    @Provides
    @Singleton
    internal fun providesOldOkHttpClient(cache: com.squareup.okhttp.Cache): com.squareup.okhttp.OkHttpClient {
        val okHttpClient = com.squareup.okhttp.OkHttpClient()
        okHttpClient.cache = cache
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS)
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS)
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS)
        return okHttpClient
    }

    @Provides
    @Singleton
    internal fun providesNewCache(@ApplicationScope context: Context): Cache {
        return Cache(File(context.filesDir.path + NEW_HTTP_CACHE_NAME), (DISC_CACHE_SIZE_MB * 1024 * 1024).toLong())
    }

    @Provides
    @Singleton
    internal fun providesOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    internal fun providesGameApiService(@CurrencyUrl url: String, okHttpClient: OkHttpClient): GameApiService {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(url)
            .build()
            .create(GameApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun providesCurrencyApiService(okHttpClient: com.squareup.okhttp.OkHttpClient, currencyExchangeJsonDeserializer: JsonDeserializer<CurrencyExchange>,
        @CurrencyUrl url: String): ICurrencyAPIService {
        val currencyExchangeGson = GsonBuilder().registerTypeAdapter(CurrencyExchange::class.java, currencyExchangeJsonDeserializer)
            .create()

        val restAdapterBuilder = RestAdapter.Builder().setClient(OkClient(okHttpClient))
            .setLogLevel(RestAdapter.LogLevel.BASIC)
            .setEndpoint(url)
            .setConverter(GsonConverter(currencyExchangeGson))

        return restAdapterBuilder.build()
            .create(ICurrencyAPIService::class.java)
    }

    @Provides
    @Singleton
    internal fun providesCheapsharkAPIService(okHttpClient: com.squareup.okhttp.OkHttpClient, gameInfoListDeserializer: JsonDeserializer<List<GameInfo>>,
        @CheapsharkUrl url: String): ICheapsharkAPIService {
        val gameInfoListType = object : TypeToken<List<GameInfo>>() {}.type
        val cheapsharkGson = GsonBuilder().registerTypeAdapter(gameInfoListType, gameInfoListDeserializer)
            .create()

        val restAdapterBuilder = RestAdapter.Builder().setClient(OkClient(okHttpClient))
            .setLogLevel(RestAdapter.LogLevel.BASIC)
            .setEndpoint(url)
            .setConverter(GsonConverter(cheapsharkGson))

        return restAdapterBuilder.build()
            .create(ICheapsharkAPIService::class.java)
    }

    @Provides
    @Singleton
    internal fun providesServices(): IServices {
        return services
    }

    @Provides
    @Singleton
    internal fun providesAnalytics(): IAnalytics {
        return services.analytics
    }
}
