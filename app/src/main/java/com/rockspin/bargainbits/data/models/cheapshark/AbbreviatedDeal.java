package com.rockspin.bargainbits.data.models.cheapshark;

/**
 * Abbreviated deal.
 */
public class AbbreviatedDeal {

    private String storeID;
    private String dealID;
    private float price;
    private float retailPrice;

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(final String storeID) {
        this.storeID = storeID;
    }

    public String getDealID() {
        return dealID;
    }

    public void setDealID(final String dealID) {
        this.dealID = dealID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(final float price) {
        this.price = price;
    }

    public float getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(final float retailPrice) {
        this.retailPrice = retailPrice;
    }

    public float getSavingsFraction() {
        final float fraction = price / retailPrice;
        return 1.0f - fraction;
    }
}
