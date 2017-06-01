package com.rockspin.bargainbits.ui.deals

sealed class DealListEvent {
    data class SortingChanged(val index: Int): DealListEvent()
}
