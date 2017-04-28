package com.rockspin.bargainbits.ui.activities.main;

import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.utils.NetworkUtils;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import javax.inject.Inject;
import rx.Observable;

public class MainActivityModel {

    private final IAnalytics iAnalytics;
    private final WatchListRepository watchListRepository;
    private final NetworkUtils networkUtils;

    @Inject MainActivityModel(IAnalytics iAnalytics, WatchListRepository watchListRepository,
        NetworkUtils networkUtils) {
        this.iAnalytics = iAnalytics;

        this.watchListRepository = watchListRepository;
        this.networkUtils = networkUtils;
    }

    public void sendDrawerToggledAnalytic(boolean opened) {
        iAnalytics.onStoreSliderToggled(opened);
    }

    public void addItemToWatchList(WatchedItem watchedItem) {
        watchListRepository.addItemToWatchList(watchedItem);
    }


    public Observable<Boolean> onInternetAvailabilityChanged() {
        return networkUtils.onNetworkChanged();
    }
}
