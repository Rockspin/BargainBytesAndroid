package com.rockspin.bargainbits.data.models.cheapshark;

import java.util.ArrayList;
import java.util.List;

/**
 * response from the game info call.
 */
public class GameInfo {

    private Info info;
    private CheapestPriceEver cheapestPriceEver;
    private List<AbbreviatedDeal> deals = new ArrayList<>();

    public Info getInfo() {
        return info;
    }

    public void setInfo(final Info info) {
        this.info = info;
    }

    public CheapestPriceEver getCheapestPriceEver() {
        return cheapestPriceEver;
    }

    public void setCheapestPriceEver(final CheapestPriceEver cheapestPriceEver) {
        this.cheapestPriceEver = cheapestPriceEver;
    }

    public List<AbbreviatedDeal> getDeals() {
        return deals;
    }

    public void setDeals(final List<AbbreviatedDeal> deals) {
        this.deals = deals;
    }

    public float getLowestSalePrice() {
        float lowestSalePrice = Float.MAX_VALUE;

        for (final AbbreviatedDeal deal : deals) {
            lowestSalePrice = Math.min(deal.getPrice(), lowestSalePrice);
        }

        return lowestSalePrice;
    }

    public float getHighestNormalPrice() {
        float highestNormalPrice = 0;

        for (final AbbreviatedDeal deal : deals) {
            highestNormalPrice = Math.max(deal.getRetailPrice(), highestNormalPrice);
        }

        return highestNormalPrice;
    }

    public float getTopSavingsFraction() {
        float topSavings = 0;

        for (final AbbreviatedDeal deal : deals) {
            topSavings = Math.max(deal.getSavingsFraction(), topSavings);
        }

        return topSavings;
    }

    public boolean isOnSale() {
        for (final AbbreviatedDeal deal : deals) {
            if (deal.getSavingsFraction() > 0.0f) {
                return true;
            }
        }

        return false;
    }

    public boolean containsMultipleStores() {
        return deals.size() > 1;
    }
}
