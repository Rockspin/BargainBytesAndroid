package com.rockspin.bargainbits.ui.activities.main.storesdrawer;

import android.support.annotation.NonNull;

import com.rockspin.bargainbits.data.repository.StoresRepository;
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.adapter.StoreEnabled;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class StoresDrawerModel {

    private final StoresRepository storeFilter;
    private final IAnalytics iAnalytics;

    @Inject StoresDrawerModel(StoresRepository storeFilter, IAnalytics iAnalytics) {
        this.storeFilter = storeFilter;

        this.iAnalytics = iAnalytics;
    }

    public Observable<List<StoreEnabled>> loadStores() {
        return storeFilter.getStores()
                          .flatMap(Observable::from)
                          .map(store -> new StoreEnabled(store, storeFilter.isStoreEnabled(store.getStoreId())))
                          .toList();
    }

    public void saveAllStoresEnabledState(boolean useAllStoresState) {
        storeFilter.setUseAllStoresState(useAllStoresState);
        iAnalytics.onAllStoreToggled(useAllStoresState);
    }

    public boolean getAllStoresEnabled() {
        return storeFilter.getUseAllStoresState();
    }

    public void saveStoreEnabledState(@NonNull String storeID, @NonNull String storeName, boolean checked) {
        storeFilter.setStoreIdState(storeID, checked);
        iAnalytics.onStoreToggled(storeID, storeName, checked);
    }
}
