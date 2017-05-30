package com.rockspin.bargainbits.ui.deals

sealed class DealListEvent
data class DealSortingChanged(val index: Int): DealListEvent()
