/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.models.cheapshark;

import com.annimon.stream.Stream;
import com.rockspin.bargainbits.CustomOperators;
import com.rockspin.bargainbits.data.models.GameInfo;
import com.rockspin.bargainbits.utils.DealUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a more compact Deal class containing a list of deals which contain the same display information.
 * It has functionality for retrieving the underlying deal object associated with it
 */
public class CompactDeal {
    private final List<Deal> mDealList = new ArrayList<>();
    private final String mGameName;
    private final String mThumbnailURL;
    private final String mGameId;
    private final long mReleaseDateSeconds;

    private final float mLowestSalePrice;
    private final float mHighestNormalPrice;
    private final float mTopSavings;

    //TODO: look at the mHighestNormalPrice and the mLowestSalePrice and see if i need to use the deal with the highest price.

    public CompactDeal(List<Deal> deals, Deal cheapestDeal) {
        mGameName = cheapestDeal.getTitle();
        mLowestSalePrice = cheapestDeal.getSalePrice();
        mHighestNormalPrice = cheapestDeal.getNormalPrice();
        mTopSavings = cheapestDeal.getSavings();
        mThumbnailURL = cheapestDeal.getThumb();
        mReleaseDateSeconds = cheapestDeal.getReleaseDate();
        mGameId = cheapestDeal.getGameID();
        //TODO replace this with a reference.
        mDealList.addAll(deals);
    }

    public CompactDeal(GameInfo gameInfo) {
        mGameName = gameInfo.getInfo()
                            .getGameName();
        mThumbnailURL = gameInfo.getInfo()
                                .getThumbnailURL();
        mGameId = gameInfo.getInfo()
                          .getGameId();

        List<Deal> deals = Stream.of(gameInfo.getDeals())
                                 .map(DealUtils::abbreviatedDealToDeal)
                                 .sortBy(Deal::getSalePrice)
                                 .custom(new CustomOperators.ToList<>());
        mDealList.addAll(deals);

        mLowestSalePrice = deals.get(0)
                                .getSalePrice();
        mHighestNormalPrice = deals.get(0)
                                   .getNormalPrice();
        mTopSavings = deals.get(0)
                           .getSavings();
        mReleaseDateSeconds = 0L;
    }

    public String getGameName() {
        return mGameName;
    }

    public float getLowestSalePrice() {
        return mLowestSalePrice;
    }

    public float getHighestNormalPrice() {
        return mHighestNormalPrice;
    }

    public float getTopSavingsPercentage() {
        return mTopSavings;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public String getGameId() {
        return mGameId;
    }

    public long getReleaseDateSeconds() {
        return mReleaseDateSeconds;
    }

    public List<Deal> getDealList() {
        return mDealList;
    }
}
