package com.rockspin.bargainbits.ui.views.deallist.view;

import android.graphics.drawable.Drawable;
import android.util.Pair;

import com.annimon.stream.Stream;
import com.fernandocejas.arrow.checks.Preconditions;
import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.bargainbits.CustomOperators.ToList;
import com.rockspin.bargainbits.data.models.cheapshark.CompactDeal;
import com.rockspin.bargainbits.data.repository.DealRepository;
import com.rockspin.bargainbits.data.repository.StoresRepository;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.di.modules.SchedulersModule;
import com.rockspin.bargainbits.ui.dialogs.store_picker.StorePickerAdapter;
import com.rockspin.bargainbits.ui.views.deallist.DealShareModel;
import com.rockspin.bargainbits.ui.views.deallist.recycleview.DealAdapterModel;
import com.rockspin.bargainbits.ui.views.deallist.recycleview.storesgrid.StoreEntryViewModel;
import com.rockspin.bargainbits.util.format.PriceFormatter;
import com.rockspin.bargainbits.utils.NetworkUtils;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.watch_list.WatchedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

import static com.rockspin.bargainbits.utils.OptionalUtils.or;

public class DealsListModel {

    private final List<CompactDeal> showDeals = new ArrayList<>();
    private Mode listMode;

    private final Scheduler mainScheduler;
    private final DealRepository dealRepository;
    private final StoresRepository storesRepository;
    private final PriceFormatter priceFormatter;
    private final WatchListRepository watchListRepository;
    private final IAnalytics iAnalytics;
    private final NetworkUtils networkUtils;

    @Inject DealsListModel(@Named(SchedulersModule.MAIN) Scheduler mainScheduler,
                           DealRepository dealRepository,
                           StoresRepository storesRepository,
                           PriceFormatter priceFormatter,
                           WatchListRepository watchListRepository,
                           IAnalytics iAnalytics,
                           NetworkUtils networkUtils) {
        this.mainScheduler = mainScheduler;
        this.dealRepository = dealRepository;
        this.storesRepository = storesRepository; /* not used */
        this.priceFormatter = priceFormatter;
        this.watchListRepository = watchListRepository;
        this.iAnalytics = iAnalytics;
        this.networkUtils = networkUtils;
    }

    void setMode(Mode mode){
        this.listMode = mode;
    }

    Observable<List<DealAdapterModel>> onDealsRefreshed(DealRepository.EDealsSorting eDealsSorting) {
        return dealRepository.onDealsRefreshed(eDealsSorting)
                .doOnNext(compactDeals -> {
                    // save the results of this request;
                    showDeals.clear();
                    showDeals.addAll(compactDeals);
                })
                .flatMap(compactDeals -> Observable
                        .from(compactDeals)
                        .map(this::createDealAdapterModelFrom)
                        .toList())
                .observeOn(mainScheduler);
    }

    Observable<List<DealAdapterModel>> loadWatchedItems() {
        return watchListRepository.getDealsInWatchList()
                .doOnNext(compactDeals -> {
                    // save the results of this request;
                    showDeals.clear();
                    showDeals.addAll(compactDeals);
                })
                .flatMap(compactDeals -> Observable
                        .from(compactDeals)
                        .map(this::createDealAdapterModelFrom)
                        .toList())
                .observeOn(mainScheduler);
    }

    private DealAdapterModel createDealAdapterModelFrom(CompactDeal compactDeal) {
        final List<StoreEntryViewModel> dealList = Stream.of(compactDeal.getDealList()).map(StoreEntryViewModel::from).custom(new ToList<>());

        final float topSavingsPercentage = compactDeal.getTopSavingsPercentage();
        final String highestNormalPrice = priceFormatter.formatPrice(compactDeal.getHighestNormalPrice());
        final String lowestSalePriceString = priceFormatter.formatPrice(compactDeal.getLowestSalePrice());
        final String savingString = String.valueOf(Math.round(topSavingsPercentage));

        final long releaseDateMillis = compactDeal.getReleaseDateSeconds() * 1000;
        final boolean isReleased = releaseDateMillis < System.currentTimeMillis();
        final boolean hasReleaseInfo = releaseDateMillis > 0;

        return new DealAdapterModel.DealAdapterModelBuilder().setGameName(compactDeal.getGameName())
                                                             .setHighestNormalPrice(highestNormalPrice)
                                                             .setLowestSalePriceString(lowestSalePriceString)
                                                             .setGridViewModel(dealList)
                                                             .setThumbnailURL(compactDeal.getThumbnailURL())
                                                             .setSavingString(savingString)
                                                             .setHasReleaseInfo(hasReleaseInfo)
                                                             .setReleaseDate(SimpleDateFormat.getDateInstance().format(new Date(releaseDateMillis)))
                                                             .setReleased(isReleased)
                                                             .build();
    }

    WatchedItem getWatchedItemForIndex(int index) {
        final CompactDeal compactDeal = getCompactDeal(index);
        Optional<WatchedItem> fromWatchList = watchListRepository.getWatchedItemByGameId(compactDeal.getGameId());
        return or(fromWatchList, () -> WatchedItem.Companion.from(compactDeal));
    }

    DealShareModel getShareModel(int index) {
        final CompactDeal compactDeal = getCompactDeal(index);
        return new DealShareModel(index, compactDeal.getGameName(), compactDeal.getTopSavingsPercentage());
    }

    Observable<List<StorePickerAdapter.StorePickerData>> createStorePickerModels(int index) {
        //TODO: add currency.
        return Observable.from(getCompactDeal(index).getDealList()).flatMap(deal -> {
            Drawable drawable = storesRepository.getStoreIconDrawableForId(deal.getStoreID());
            String savings = String.valueOf(deal.getSalePrice());
            return storesRepository.getStoreNameForID(deal.getStoreID()).map(name -> StorePickerAdapter.StorePickerData.create(name, drawable, savings));
        }).toList();
    }

    String getDealIdAt(int index) {
        Preconditions.checkArgument(showDeals.get(index).getDealList().size() == 1, "trying to get deal id from multideal. should use store picker." );
        return showDeals.get(index).getDealList().get(0).getDealID();
    }

    void sendGameSharedAnalytic(int integer) {
        iAnalytics.onSharedGame(getCompactDealAtIndex(integer).getGameName());
    }

    CompactDeal getCompactDealAtIndex(int index) {
        return getCompactDeal(index);
    }

    Observable<Integer> onItemRemovedFromWatchList() {
        return watchListRepository.onWatchedDealRemoved()
                                  .filter(compactDeal1 -> isShowingWatchedItems())
                                  .map(showDeals::indexOf)
                                  .doOnNext(integer -> showDeals.remove((int) integer));
    }

    Observable<DealAdapterModel> onItemAddedToWatchList() {
        return watchListRepository.onWatchedDealAdded()
                .filter(compactDeal1 -> isShowingWatchedItems())
                .doOnNext(compactDeal -> showDeals.add(0, compactDeal))
                .map(this::createDealAdapterModelFrom);
    }

    boolean isShowingWatchedItems() {
        return listMode == Mode.WATCH_LIST;
    }

    Observable<Pair<WatchedItem, WatchListRepository.Action>> onWatchListEdited() {
        return watchListRepository.onWatchListEdited();
    }

    boolean isConnectedToInternet() {
        return networkUtils.isConnectedToInternet();
    }

    String getDealUrlFromDealID(String dealId) {
        return dealRepository.loadDealUrl(dealId);
    }

    public void addItemToWatchList(WatchedItem watchedItem) {
        watchListRepository.addItemToWatchList(watchedItem);
    }

    private CompactDeal getCompactDeal(Integer index) {
        return showDeals.get(index);
    }

    public enum Mode {
        WATCH_LIST,
        BROWSING_DEALS
    }
}
