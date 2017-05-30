package com.rockspin.bargainbits.ui.deals

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.rockspin.bargainbits.data.repository.deals.GameDealRepository
import com.rockspin.bargainbits.data.repository.stores.StoreRepository
import com.rockspin.bargainbits.data.repository.stores.filter.StoreFilter
import com.rockspin.bargainbits.util.ResourceProvider
import com.rockspin.bargainbits.util.format.PriceFormatter
import javax.inject.Inject

/**
 * Created by valentin.hinov on 29/05/2017.
 */
class DealListViewModelFactory @Inject constructor(
    private val repository: GameDealRepository,
    private val filter: StoreFilter,
    private val storeRepository: StoreRepository,
    private val resourceProvider: ResourceProvider,
    private val priceFormatter: PriceFormatter)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(aClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DealListViewModel(repository, filter, storeRepository, resourceProvider, priceFormatter) as T
    }
}