/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.watch_list;

import android.os.Parcelable;

import com.rockspin.bargainbits.data.models.cheapshark.CompactDeal;
import com.rockspin.bargainbits.data.models.GameSearchResult;
import com.rockspin.bargainbits.data.models.cheapshark.GameInfo;

import java.io.Serializable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class WatchedItem implements Parcelable, Serializable {
    static final long serialVersionUID = 1L;

    public abstract String gameName();
    public abstract String gameId();
    public abstract float watchedPrice();

    public boolean hasCustomWatchPrice() {
        return watchedPrice() >= 0;
    }

    public static WatchedItem create(String gameName, String gameId, float watchedPrice) {
        return new AutoParcel_WatchedItem(gameName, gameId, watchedPrice);
    }

    public static WatchedItem from(GameSearchResult game){
        return  create(game.getName(), game.getGameID(), (float) game.getCheapestPrice());
    }

    public static WatchedItem from(CompactDeal compactDeal){
        return  create(compactDeal.getGameName(),compactDeal.getGameId(), compactDeal.getLowestSalePrice());
    }

    public static WatchedItem from(GameInfo gameInfo) {
        return create(gameInfo.getInfo().getGameName(), gameInfo.getInfo().getGameId(), gameInfo.getLowestSalePrice());
    }
}
