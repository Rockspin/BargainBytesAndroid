package com.rockspin.bargainbits.ui.activities.main.storesdrawer.adapter;

import com.rockspin.bargainbits.data.models.Store;

public class StoreEnabled {
    public final Store store;
    //TODO: im not happy that this mutates
    public boolean checked;

    public StoreEnabled(Store store, boolean checked) {
        this.store = store;
        this.checked = checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}