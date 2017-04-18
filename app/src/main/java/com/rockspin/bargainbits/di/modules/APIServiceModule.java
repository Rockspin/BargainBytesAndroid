package com.rockspin.bargainbits.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.rockspin.bargainbits.data.models.cheapshark.GameInfo;
import com.rockspin.bargainbits.data.models.currency.CurrencyExchange;
import com.rockspin.bargainbits.data.rest_client.ICheapsharkAPIService;
import com.rockspin.bargainbits.data.rest_client.ICurrencyAPIService;
import com.rockspin.bargainbits.di.annotations.CheapsharkUrl;
import com.rockspin.bargainbits.di.annotations.CurrencyUrl;
import com.rockspin.bargainbits.services.Services;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.utils.environment.IServices;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Singleton;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public class APIServiceModule {
    private final Services services = new Services();

    @Provides @Singleton
    ICurrencyAPIService providesCurrencyApiService(OkHttpClient okHttpClient, JsonDeserializer<CurrencyExchange>
        currencyExchangeJsonDeserializer, @CurrencyUrl String url) {
        final Gson currencyExchangeGson = new GsonBuilder().registerTypeAdapter(CurrencyExchange.class, currencyExchangeJsonDeserializer)
                                                           .create();

        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder().setClient(new OkClient(okHttpClient))
                                                                          .setLogLevel(RestAdapter.LogLevel.BASIC)
                                                                          .setEndpoint(url)
                                                                          .setConverter(new GsonConverter(currencyExchangeGson));

        return restAdapterBuilder.build()
                                 .create(ICurrencyAPIService.class);
    }

    @Provides @Singleton
    ICheapsharkAPIService providesCheapsharkAPIService(OkHttpClient okHttpClient, JsonDeserializer<List<GameInfo>>
        gameInfoListDeserializer, @CheapsharkUrl String url) {
        final Type gameInfoListType = new TypeToken<List<GameInfo>>() {
        }.getType();
        final Gson cheapsharkGson = new GsonBuilder().registerTypeAdapter(gameInfoListType, gameInfoListDeserializer)
                                                     .create();

        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder().setClient(new OkClient(okHttpClient))
                                                                          .setLogLevel(RestAdapter.LogLevel.BASIC)
                                                                          .setEndpoint(url)
                                                                          .setConverter(new GsonConverter(cheapsharkGson));

        return restAdapterBuilder.build()
                                 .create(ICheapsharkAPIService.class);
    }

    @Provides @Singleton
    IServices providesServices() {
        return services;
    }

    @Provides @Singleton
    IAnalytics providesAnalytics() {
        return services.getAnalytics();
    }
}
