package com.rockspin.bargainbits.ui.activities.search;

import android.util.Pair;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.utils.NetworkUtils;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import javax.inject.Inject;
import rx.Observable;

public final class GamesSearchModel implements GamesSearchPresenter.IModel {

    @Inject WatchListRepository watchListRepository;
    @Inject NetworkUtils networkChanged;
    @Inject IAnalytics iAnalytics;

    @Inject GamesSearchModel() {
    }

    public void addToWatchList(final WatchedItem watchedItem) {
        watchListRepository.addItemToWatchList(watchedItem);
    }

    @Override public Observable<Boolean> onNetworkChanged() {
        return networkChanged.onNetworkChanged();
    }

    @Override public Observable<Pair<WatchedItem, WatchListRepository.Action>> onWatchListEdited() {
        return watchListRepository.onWatchListEdited();
    }

    @Override public void onGameSearchedFor(String query) {
        iAnalytics.onGameSearchedFor(query);
    }
}
