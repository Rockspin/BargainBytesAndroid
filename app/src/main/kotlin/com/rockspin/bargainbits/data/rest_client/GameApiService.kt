package com.rockspin.bargainbits.data.rest_client

import com.rockspin.bargainbits.data.models.GameSearchResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit instance for the Cheapshark API.
 */
interface GameApiService {

    @GET("games")
    fun searchGames(@Query("title") gameTitle: String): Observable<List<GameSearchResult>>
}