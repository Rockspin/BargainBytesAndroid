package com.rockspin.bargainbits.data.repository.deals

import com.rockspin.bargainbits.data.models.DealSortType
import com.rockspin.bargainbits.data.models.GroupedGameDeal
import io.reactivex.Single

interface GameDealRepository {

    fun getDeals(sortType: DealSortType, storeIds: Set<String>): Single<List<GroupedGameDeal>>
}