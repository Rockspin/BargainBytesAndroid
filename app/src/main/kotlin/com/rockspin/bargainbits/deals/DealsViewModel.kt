package com.rockspin.bargainbits.deals

import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import javax.inject.Inject

class DealsViewModel(@Inject var dealsRepo: DealsRepository) : ViewModel() {

    fun getDeals(): Single<List<Game>> {
        return dealsRepo.deals()
    }
}