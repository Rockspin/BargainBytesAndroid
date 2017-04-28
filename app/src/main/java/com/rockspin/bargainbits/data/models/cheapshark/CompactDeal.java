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

    /**
     * Construct a DisplayDeal object with a root deal
     *
     * @param deal Deal to add as the first one in the list.
     */
    public CompactDeal(final Deal deal) {
        mGameName = deal.getTitle();
        mLowestSalePrice = deal.getSalePrice();
        mHighestNormalPrice = deal.getNormalPrice();
        mTopSavings = deal.getSavings();
        mThumbnailURL = deal.getThumb();
        mReleaseDateSeconds = deal.getReleaseDate();
        mGameId = deal.getGameID();
        mDealList.add(deal);
    }

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
                                 //.custom(new ToSortedList<Deal>((lhs, rhs) -> Float.compare(lhs.getSalePrice(), rhs.getSalePrice())));
        mDealList.addAll(deals);

        mLowestSalePrice = deals.get(0)
                                .getSalePrice();
        mHighestNormalPrice = deals.get(0)
                                   .getNormalPrice();
        mTopSavings = deals.get(0)
                           .getSavings();
        mReleaseDateSeconds = 0L;
    }

    public boolean canContain(final Deal deal) {
        return mDealList.isEmpty() || deal.equals(mDealList.get(0));
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
