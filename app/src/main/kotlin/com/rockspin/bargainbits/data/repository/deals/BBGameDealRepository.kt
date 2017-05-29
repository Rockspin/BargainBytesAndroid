package com.rockspin.bargainbits.data.repository.deals

import com.rockspin.bargainbits.data.BBDatabase
import com.rockspin.bargainbits.data.models.DbDeals
import com.rockspin.bargainbits.data.models.DealSortType
import com.rockspin.bargainbits.data.models.GameDeal
import com.rockspin.bargainbits.data.models.GroupedGameDeal
import com.rockspin.bargainbits.data.rest_client.GameApiService
import com.rockspin.bargainbits.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by valentin.hinov on 29/05/2017.
 */
class BBGameDealRepository(
    private val apiService: GameApiService,
    private val database: BBDatabase,
    private val networkUtils: NetworkUtils): GameDealRepository {

    override fun getDeals(sortType: DealSortType, storeIds: Set<String>): Single<List<GroupedGameDeal>> {
        val sortedIds = storeIds.toSortedSet()

        val apiCall = apiCallForParams(sortType, sortedIds)

        // TODO - introduce DB cache "freshness" so cached data is not fetched if too old

        return apiCall
            .onErrorResumeNext(fromDatabase(sortType, sortedIds))
            .flatMap { Observable.fromIterable(it) }
            .groupBy { it.gameId }
            .concatMap { groups ->
                groups.toList().map { GroupedGameDeal(it) }.toObservable()
            }
            .toList()
    }

    private fun fromDatabase(sortType: DealSortType, storeIds: Set<String>): Observable<List<GameDeal>> {
        val key = DbDeals.constructKey(sortType, storeIds)
        return database.dbDealsDao().getDbDeals(key)
            .flatMap { dbDeals ->
                val dealIds = dbDeals.dealIdsCommaSeparated.split(",")
                database.gameDealDao().getDealsById(dealIds)
            }
            .toObservable()

    }

    private fun apiCallForParams(sortType: DealSortType, storeIds: Set<String>): Observable<List<GameDeal>> {
        val params = mapOf(
            "onSale" to "1",
            "sortBy" to sortType.queryParameter,
            "storeID" to storeIds.joinToString(separator = ","))

        return apiService.getDeals(params)
            .doOnSuccess { gameDeals ->
                database.gameDealDao().insertAll(gameDeals)

                val dealIdsCommaSeparated = gameDeals.map { it.dealId }.joinToString(separator = ",")
                val key = DbDeals.constructKey(sortType, storeIds)
                database.dbDealsDao().insert(DbDeals(key, dealIdsCommaSeparated))
            }
            .toObservable()
    }

    // TODO - Action that cleanups unreferenced GameDeals in database based on DbDeals
}