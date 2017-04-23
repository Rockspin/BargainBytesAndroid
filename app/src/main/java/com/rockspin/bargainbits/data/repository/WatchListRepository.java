/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.repository;

import android.util.Pair;
import com.annimon.stream.Stream;
import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.apputils.cache.helper.SimpleCacheReader;
import com.rockspin.apputils.cache.helper.SimpleCacheWriter;
import com.rockspin.bargainbits.data.models.cheapshark.CompactDeal;
import com.rockspin.bargainbits.data.models.GameInfo;
import com.rockspin.bargainbits.data.rest_client.ICheapsharkAPIService;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Single;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Object that contains the current WatchListRepository setting and provide data from it.
 */
@Singleton
public class WatchListRepository {
    private static final String WATCH_LIST_KEY = "WATCH_LIST";
    public static final int MAX_WATCHED_ITEMS = 25;

    private final PublishSubject<Pair<WatchedItem, Action>> watchListEdited = PublishSubject.create();

    private final ArrayList<WatchedItem> mWatchedItemList = new ArrayList<>();
    private final Map<String, CompactDeal> watchedCompactDealToGameID = new HashMap<>();

    @Inject IAnalytics iAnalytics;
    @Inject ICheapsharkAPIService iCheapsharkAPIService;
    @Inject SimpleCacheWriter cacheWriter;

    @Inject public WatchListRepository(SimpleCacheReader cacheReader) {
        Single<? extends ArrayList> result = cacheReader.runRequest(WATCH_LIST_KEY, mWatchedItemList.getClass());

        try {
            mWatchedItemList.addAll(result.toObservable()
                                          .toBlocking()
                                          .first());
        } catch (final Exception e) {
            Timber.d(e, "Watchlist failure to retrieve from cache");
        }
    }

    /**
     * Adds this item in the Watch List. If item already exists, it replaces the stored occurrence with this one
     *
     * @param watchedItem WatchedItem to store in list.
     */
    public void addItemToWatchList(final WatchedItem watchedItem) {

        if (mWatchedItemList.size() >= MAX_WATCHED_ITEMS) {
            watchListEdited.onNext(new Pair<>(watchedItem, Action.FULL));
            return;
        }

        if (isItemInWatchList(watchedItem)) {
            int indexOfItem = mWatchedItemList.indexOf(watchedItem);
            mWatchedItemList.set(indexOfItem, watchedItem);
            watchListEdited.onNext(new Pair<>(watchedItem, Action.EDITED));
            Timber.d("Updating entry " + watchedItem.gameName() + " in watch list");
        } else {
            mWatchedItemList.add(watchedItem);
            Timber.d("Item " + watchedItem.gameName() + " added to watch list");

            iAnalytics.onGameAddedToWatchList(watchedItem.gameName(), watchedItem.hasCustomWatchPrice());
            watchListEdited.onNext(new Pair<>(watchedItem, Action.ADDED));
        }

        cacheWriter.cacheValue(WATCH_LIST_KEY, mWatchedItemList)
                   .subscribe();
    }

    public void removeItemFromWatchList(final WatchedItem watchedItem) {
        if (!isItemInWatchList(watchedItem)) {
            return;
        }

        mWatchedItemList.remove(watchedItem);

        cacheWriter.cacheValue(WATCH_LIST_KEY, mWatchedItemList)
                   .subscribe();
        watchListEdited.onNext(new Pair<>(watchedItem, Action.REMOVED));
        Timber.d("Item " + watchedItem.gameName() + " removed from watch list");
    }

    public Optional<WatchedItem> getWatchedItemByGameId(final String gameId) {
        for (WatchedItem watchedItem : mWatchedItemList) {
            if (watchedItem.gameId()
                           .equals(gameId)) {
                return Optional.of(watchedItem);
            }
        }
        return Optional.absent();
    }

    public boolean isItemInWatchList(final WatchedItem watchedItem) {
        return mWatchedItemList.contains(watchedItem);
    }

    public boolean isEmpty() {
        return mWatchedItemList.isEmpty();
    }

    private String[] getGameIdArray() {
        return Stream.of(mWatchedItemList)
                     .map(WatchedItem::gameId)
                     .toArray(String[]::new);
    }

    private String getGameIdsCommaSeparated() {
        return Stream.of(getGameIdArray())
                     .reduce(new StringBuilder(), (stringBuilder, value2) -> {
                         if (stringBuilder.length() == 0) {
                             stringBuilder.append(value2);
                         } else {
                             stringBuilder.append(",")
                                          .append(value2);
                         }
                         return stringBuilder;
                     }).toString();
    }

    public Observable<Pair<WatchedItem, Action>> onWatchListEdited() {
        return watchListEdited;
    }

    /**
     * Return a single that emits all the watched games that are currently on sale.
     *
     * @return a single that emits all the watched games that are currently on sale.
     */
    public Single<List<GameInfo>> getGamesOnSale() {
        return loadGameInfosFromWatchlist().flatMap(Observable::from)
                                           .filter(gameInfo -> {
                                               final Optional<WatchedItem> watchedItem = getWatchedItemByGameId(gameInfo.getInfo()
                                                                                                                        .getGameId());

                                               if (watchedItem.get()
                                                              .hasCustomWatchPrice()) {
                                                   return gameInfo.getLowestSalePrice() <= watchedItem.get()
                                                                                                      .watchedPrice();
                                               } else {
                                                   return gameInfo.isOnSale();
                                               }
                                           })
                                           .doOnNext(gamesOnSale -> Timber.d("found sale %f", gamesOnSale.getLowestSalePrice()))
                                           .toList()
                                           .toSingle();
    }

    /**
     * Return a single that emits all the watched games that are currently on sale.
     *
     * @return a single that emits all the watched games that are currently on sale.
     */
    public Observable<List<CompactDeal>> getDealsInWatchList() {
        return loadGameInfosFromWatchlist().flatMap(Observable::from)
                                           .map(CompactDeal::new)
                                           .doOnNext(compactDeals -> watchedCompactDealToGameID.put(compactDeals.getGameId(), compactDeals))
                                           .toList();
    }

    private Observable<List<GameInfo>> loadGameInfosFromWatchlist() {
        return iCheapsharkAPIService.getGamesInfo(getGameIdsCommaSeparated())
                                    .filter(gameInfos -> gameInfos != null) // skip nulls
                                    .filter(gameInfos -> !gameInfos.isEmpty());
    }

    public Observable<CompactDeal> onWatchedDealRemoved() {
        return watchListEdited.filter(watchedItemActionPair -> watchedItemActionPair.second == Action.REMOVED)
                              .compose(getCompactDealForGameID());
    }

    public Observable<CompactDeal> onWatchedDealAdded() {
        return watchListEdited.filter(watchedItemActionPair -> watchedItemActionPair.second == Action.ADDED)
                              .compose(getCompactDealForGameID());
    }

    private Observable.Transformer<? super Pair<WatchedItem, Action>, CompactDeal> getCompactDealForGameID() {
        return pairObservable -> pairObservable.map(watchedItemActionPair1 -> watchedItemActionPair1.first.gameId())
                                               .map(watchedCompactDealToGameID::get);
    }

    public enum Action {
        EDITED,
        ADDED,
        FULL,
        REMOVED
    }
}
