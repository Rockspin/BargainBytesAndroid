package com.rockspin.bargainbits.ui.activities.main;

import com.rockspin.bargainbits.data.repository.CurrencyRepository;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.data.models.currency.CurrencyNamesAndISOCodes;
import com.rockspin.bargainbits.utils.Constants;
import com.rockspin.bargainbits.utils.NetworkUtils;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.watch_list.WatchedItem;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public class MainActivityModel {

    private final IAnalytics iAnalytics;
    private final CurrencyRepository currencyRepository;
    private final WatchListRepository watchListRepository;
    private final NetworkUtils networkUtils;

    @Inject MainActivityModel(IAnalytics iAnalytics, CurrencyRepository currencyRepository, WatchListRepository watchListRepository,
        NetworkUtils networkUtils) {
        this.iAnalytics = iAnalytics;

        this.currencyRepository = currencyRepository;
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

    public Observable<CurrencyNamesAndISOCodes> onCurrenciesUpdated() {
        return currencyRepository.getCurrencyNamesAndIsoCodes().subscribeOn(Schedulers.io());
    }

    public Observable<Void> onCurrencyUpdatedToNonDefault() {
        return currencyRepository.onIsoChanged().filter(isoCode -> !isoCode.equals(Constants.DEFAULT_CURRENCY_CODE)).map(s -> null);
    }
}
