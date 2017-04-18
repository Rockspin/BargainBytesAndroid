/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.rest_client;

import com.rockspin.bargainbits.data.models.currency.CurrencyExchange;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import rx.Observable;

/**
 * Retrofit interface for Currency API.
 */
public interface ICurrencyAPIService {
    // Cache currency results for a day, accept stale responses for a week
    @Headers("Cache-Control: public, max-age=86400, s-maxage=86400, max-stale=604800") @GET("/latest") Observable<CurrencyExchange> getLatest(
        @Query("base") String baseCurrency);

    // Cache currency results for a day, accept stale responses for a week
    @Headers("Cache-Control: public, max-age=86400, s-maxage=86400, max-stale=604800") @GET("/latest?base=USD") Observable<CurrencyExchange> getLatestUSD();
}
