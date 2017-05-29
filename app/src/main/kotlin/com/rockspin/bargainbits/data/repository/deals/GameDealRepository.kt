package com.rockspin.bargainbits.data.repository.deals

import com.rockspin.bargainbits.data.models.DealSortType
import com.rockspin.bargainbits.data.models.GroupedGameDeal
import io.reactivex.Single

/**
 * Created by valentin.hinov on 29/05/2017.
 */
interface GameDealRepository {

    fun getDeals(sortType: DealSortType, storeIds: Set<String>): Single<List<GroupedGameDeal>>
}