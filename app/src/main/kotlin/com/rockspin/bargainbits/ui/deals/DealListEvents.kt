package com.rockspin.bargainbits.ui.deals

import android.support.annotation.IdRes

sealed class DealListEvent {
    data class SortingChanged(val index: Int): DealListEvent()
    data class MenuItemClicked(@IdRes val menuId: Int): DealListEvent()
}
