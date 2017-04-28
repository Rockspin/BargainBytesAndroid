package com.rockspin.bargainbits.ui.dialogs.watchlist;

import com.rockspin.bargainbits.data.repository.CurrencyRepository;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.data.models.currency.CurrencyHelper;
import com.rockspin.bargainbits.watch_list.WatchedItem;

import javax.inject.Inject;

import rx.Observable;

public class EditWatchListModel {

    private final WatchListRepository watchListRepository;
    private final CurrencyRepository currencyRepository;

    @Inject EditWatchListModel(WatchListRepository watchListRepository, CurrencyRepository currencyRepository) {
        this.watchListRepository = watchListRepository;
        this.currencyRepository = currencyRepository;
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

    public Observable<CurrencyHelper> onCurrentCurrencyChanged() {
        return currencyRepository.onCurrentCurrencyChanged();
    }

    public String getActiveCurrencySymbol() {
        return currencyRepository.getActiveCurrencySymbol();
    }

    public WatchedItem verifyDataCorrect(WatchedItem watchedItem) {
        return watchListRepository.getWatchedItemByGameId(watchedItem.getGameId())
                                  .or(new WatchedItem(watchedItem.getGameName(), watchedItem.getGameId(), -1));
    }
}
