package com.rockspin.bargainbits.ui.activities.search.gameinfo.recycler;

import android.graphics.drawable.Drawable;

public class GamesInfoDealsViewModel {
    public final String salePrice;
    public final Drawable storeImage;
    public final String storeName;

    public final String retailPrice;
    public final String dealPrice;

    public final float savingPercentage;
    public final boolean hasSavings;
    public final boolean singlePriceMode;

    private GamesInfoDealsViewModel(String salePrice, Drawable storeImage, String storeName, float savingPercentage, boolean hasSavings, boolean singlePriceMode,
        String retailPrice, String dealPrice) {
        this.salePrice = salePrice;
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.savingPercentage = savingPercentage;
        this.hasSavings = hasSavings;
        this.singlePriceMode = singlePriceMode;
        this.retailPrice = retailPrice;
        this.dealPrice = dealPrice;
    }

    public static class Builder {
        private String salePrice;
        private Drawable storeImage;
        private String storeName;
        private float savingPercentage;
        private boolean hasSavings;
        private boolean singlePriceMode;
        private String retailPrice;
        private String dealPrice;

        public Builder setSalePrice(String salePrice) {
            this.salePrice = salePrice;
            return this;
        }

        public Builder setStoreImage(Drawable storeImage) {
            this.storeImage = storeImage;
            return this;
        }

        public Builder setStoreName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        public Builder setSavingPercentage(float savingPercentage) {
            this.savingPercentage = savingPercentage;
            return this;
        }

        public Builder setHasSavings(boolean hasSavings) {
            this.hasSavings = hasSavings;
            return this;
        }

        public Builder setSinglePriceMode(boolean singlePriceMode) {
            this.singlePriceMode = singlePriceMode;
            return this;
        }

        public Builder setRetailPrice(String retailPrice) {
            this.retailPrice = retailPrice;
            return this;
        }

        public Builder setDealPrice(String dealPrice) {
            this.dealPrice = dealPrice;
            return this;
        }

        public GamesInfoDealsViewModel build() {
            return new GamesInfoDealsViewModel(salePrice, storeImage, storeName, savingPercentage, hasSavings, singlePriceMode, retailPrice, dealPrice);
        }
    }
}