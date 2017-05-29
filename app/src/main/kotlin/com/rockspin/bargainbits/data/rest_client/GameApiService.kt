package com.rockspin.bargainbits.data.rest_client

import com.rockspin.bargainbits.data.models.GameDeal
import com.rockspin.bargainbits.data.models.GameInfo
import com.rockspin.bargainbits.data.models.GameSearchResult
import com.rockspin.bargainbits.data.models.Store
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Retrofit instance for the Cheapshark API.
 */
interface GameApiService {

    @GET("games")
    fun searchGames(@Query("title") gameTitle: String): Single<List<GameSearchResult>>

    @GET("games")
    fun getGameInfo(@Query("id") gameId: String): Single<GameInfo>

    @GET("stores")
    fun getStores(): Single<List<Store>>

    @GET("deals")
    fun getDeals(@QueryMap requestParams: Map<String, String>): Single<List<GameDeal>>
}