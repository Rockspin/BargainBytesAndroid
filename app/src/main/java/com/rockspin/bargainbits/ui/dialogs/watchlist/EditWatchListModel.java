package com.rockspin.bargainbits.ui.dialogs.watchlist;

import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.watch_list.WatchedItem;

import javax.inject.Inject;

public class EditWatchListModel {

    private final WatchListRepository watchListRepository;

    @Inject EditWatchListModel(WatchListRepository watchListRepository) {
        this.watchListRepository = watchListRepository;
    }

    public void addItemToWatchList(WatchedItem entry) {
        watchListRepository.addItemToWatchList(entry);
    }

    public void removeItemFromWatchList(WatchedItem watchedItem) {
        watchListRepository.removeItemFromWatchList(watchedItem);
    }

    public boolean isItemInWatchList(WatchedItem passedItem) {
        return watchListRepository.isItemInWatchList(passedItem);
    }

    public WatchedItem verifyDataCorrect(WatchedItem watchedItem) {
        return watchListRepository.getWatchedItemByGameId(watchedItem.getGameId())
                                  .or(new WatchedItem(watchedItem.getGameName(), watchedItem.getGameId(), -1));
    }
}
