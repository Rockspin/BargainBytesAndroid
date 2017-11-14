package com.rockspin.bargainbits.deals

import android.arch.lifecycle.ViewModel
import io.reactivex.Single

class DealsViewModel(private var dealsRepo: DealsRepository) : ViewModel() {

    fun getDeals(): Single<List<Game>> {
        return dealsRepo.deals()
    }
}