/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.rest_client;

import com.rockspin.bargainbits.data.models.cheapshark.Deal;
import com.rockspin.bargainbits.data.models.GameSearchResult;
import com.rockspin.bargainbits.data.models.GameInfo;
import com.rockspin.bargainbits.data.models.Store;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;

/**
 * Retrofit Interface for the Cheapshark API.
 * @deprecated Use {@link GameApiService} instead
 */
@Deprecated
public interface ICheapsharkAPIService {
    // Cache store results for a day, accept stale responses for a week
    @Headers("Cache-Control: public, max-age=86400, s-maxage=86400, max-stale=604800") @GET("/stores") Observable<List<Store>> getStores();

    // Deals lookups must always be fresh - do not cache
    @Headers("Cache-Control: public, max-age=0, s-maxage=0") @GET("/deals") Observable<List<Deal>> getDeals(@QueryMap Map<String, String> requestParams);

    @GET("/games") Observable<List<GameSearchResult>> searchGames(@Query("title") String gameTitle);

    @GET("/games") Observable<GameInfo> getGameInfo(@Query("id") String gameId);

    /**
     * Allows lookup of a list of games. Includes list of all deals for each game. Maximum of 25 games.
     * @param commaSeparatedIds  Maximum comma seperated list of 25 game ids.
     * @return Observable that emits a list, null or error.
     */
    @GET("/games") Observable<Map<String, GameInfo>> getGamesInfo(@Query("ids") String commaSeparatedIds);
}
