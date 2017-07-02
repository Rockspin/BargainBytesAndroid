package com.rockspin.bargainbits.ui.deals

data class DealListUiState(
    val dealViewEntries: List<DealViewEntry> = emptyList(),
    val showInternetOffMessage: Boolean = false,
    val inProgress: Boolean = false,
    val hasError: Boolean = false,
    val navigation: Navigation? = null
) {
    enum class Navigation {
        STORE_FILTER,
        WATCH_LIST,
        RATE_APP,
        SHARE_APP,
        SEND_FEEDBACK
    }
}

data class DealViewEntry(
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val savingsPercentage: Int,
    val retailPrice: String,
    val salePrice: String,
    val storeImageUrls: List<String>
)