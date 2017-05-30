package com.rockspin.bargainbits.ui.deals

sealed class DealListUiModel
data class ShowDealEntriesUiModel(val dealViewEntries: List<DealViewEntry>): DealListUiModel()

data class DealViewEntry(
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val savingsPercentage: Int,
    val retailPrice: String,
    val salePrice: String,
    val storeImageUrls: List<String>
)