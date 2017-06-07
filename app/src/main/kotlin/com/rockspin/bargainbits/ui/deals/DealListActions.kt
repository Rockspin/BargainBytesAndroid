package com.rockspin.bargainbits.ui.deals

import com.rockspin.bargainbits.data.models.DealSortType

sealed class DealListAction {
    data class LoadDealsWithSortType(val sortType: DealSortType): DealListAction()
    data class PerformNavigation(val navigation: DealListUiState.Navigation): DealListAction()
    object CheckForFilterChanges: DealListAction()
}
