package com.rockspin.bargainbits.ui.deals

data class DealListUiModel(
    val dealViewEntries: List<DealViewEntry> = emptyList(),
    val showInternetOffMessage: Boolean = false,
    val inProgress: Boolean = false,
    val hasError: Boolean = false
)

data class DealViewEntry(
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val savingsPercentage: Int,
    val retailPrice: String,
    val salePrice: String,
    val storeImageUrls: List<String>
)