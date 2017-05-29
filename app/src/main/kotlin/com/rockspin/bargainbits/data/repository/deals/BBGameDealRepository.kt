package com.rockspin.bargainbits.data.repository.deals

import com.rockspin.bargainbits.data.BBDatabase
import com.rockspin.bargainbits.data.models.DealSortType
import com.rockspin.bargainbits.data.models.GroupedGameDeal
import com.rockspin.bargainbits.data.rest_client.GameApiService
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by valentin.hinov on 29/05/2017.
 */
class BBGameDealRepository(private val apiService: GameApiService, private val database: BBDatabase): GameDealRepository {

    override fun getDeals(sortType: DealSortType, storeIds: Set<String>): Single<List<GroupedGameDeal>> {
        val sortedIds = storeIds.sorted()

        val params = mapOf(
            "onSale" to "1",
            "sortBy" to sortType.queryParameter,
            "storeID" to sortedIds.joinToString(separator = ","))

        return apiService.getDeals(params)
            .flatMapObservable { Observable.fromIterable(it) }
            .groupBy { it.gameId }
            .concatMap { groups ->
                groups.toList().map { GroupedGameDeal(it) }.toObservable()
            }
            .toList()

    }

    // TODO - Action that cleanups unreferenced GameDeals in database based on DbDeals
}