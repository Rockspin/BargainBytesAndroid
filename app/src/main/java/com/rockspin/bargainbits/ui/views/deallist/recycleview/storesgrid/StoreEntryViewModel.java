package com.rockspin.bargainbits.ui.views.deallist.recycleview.storesgrid;

import com.rockspin.bargainbits.data.models.cheapshark.Deal;

public class StoreEntryViewModel {

    private final String storeId;
    private final boolean isActive;

    private StoreEntryViewModel(String storeId, boolean isActive) {
        this.storeId = storeId;
        this.isActive = isActive;
    }

    public String getStoreId() {
        return storeId;
    }

    public boolean isActive() {
        return isActive;
    }

    public static StoreEntryViewModel from(Deal deal) {
        return new StoreEntryViewModel(deal.getStoreID(), deal.getSavings() > 0.0f);
    }
}
