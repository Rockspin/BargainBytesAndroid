package com.rockspin.bargainbits.ui.deals

/**
 * Created by valentin.hinov on 26/05/2017.
 */
data class DealEntryUiModel(
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val savingsPercentage: Int,
    val retailPrice: String,
    val salePrice: String,
    val storeImageUrls: List<String>
)