package com.rockspin.bargainbits.ui.views.deallist.recycleview;

import com.annimon.stream.Stream;
import com.rockspin.bargainbits.CustomOperators.ToList;
import com.rockspin.bargainbits.data.models.GameInfo;
import com.rockspin.bargainbits.ui.views.deallist.recycleview.storesgrid.StoreEntryViewModel;
import com.rockspin.bargainbits.utils.DealUtils;

import java.util.List;

public class DealAdapterModel {

    private final String releaseDateString;
    private final String gameName;
    private final List<StoreEntryViewModel> storeEntryViewModels;
    private final String thumbnailURL;
    private final String lowestSalePriceString;
    private final String highestNormalPrice;
    private final String savingString;
    private final boolean isReleased;
    private final boolean hasReleaseInfo;

    private DealAdapterModel(DealAdapterModelBuilder builder) {
        this.releaseDateString = builder.releaseDateString;
        this.gameName = builder.gameName;
        this.storeEntryViewModels = builder.gridViewModel;
        this.thumbnailURL = builder.thumbnailURL;
        this.lowestSalePriceString = builder.lowestSalePriceString;
        this.highestNormalPrice = builder.highestNormalPrice;
        this.savingString = builder.savingString;
        this.isReleased = builder.isReleased;
        this.hasReleaseInfo = builder.hasReleaseInfo;
    }

    boolean hasReleaseDate() {
        return hasReleaseInfo;
    }

    public String getReleaseDateString() {
        return releaseDateString;
    }

    public String getGameName() {
        return gameName;
    }

    public List<StoreEntryViewModel> getStoreEntryViewModels() {
        return storeEntryViewModels;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getLowestSalePriceString() {
        return lowestSalePriceString;
    }

    public String getHighestNormalPrice() {
        return highestNormalPrice;
    }

    public String getSavingString() {
        return savingString;
    }

    public static DealAdapterModel from(GameInfo gameInfo) {
        final List< StoreEntryViewModel> storeEntryViewModels = Stream.of(gameInfo.getDeals())
                                                                       .map(DealUtils::abbreviatedDealToDeal)
                                                                       .map(StoreEntryViewModel::from)
                                                                       .custom(new ToList<StoreEntryViewModel>());

        final float savingsPercent = (float) (gameInfo.getTopSavingsFraction() * 100);
        return new DealAdapterModelBuilder().setGameName(gameInfo.getInfo().getGameName())
                                            .setHighestNormalPrice(String.valueOf(gameInfo.getHighestNormalPrice()))
                                            .setLowestSalePriceString(String.valueOf(gameInfo.getLowestSalePrice()))
                                            .setGridViewModel(storeEntryViewModels)
                                            .setThumbnailURL(gameInfo.getInfo().getThumbnailURL())
                                            .setSavingString(String.valueOf(savingsPercent))
                                            .build();
    }

    public boolean isReleased() {
        return isReleased;
    }

    public static class DealAdapterModelBuilder {
        private String releaseDateString = "";
        private String gameName;
        private List<StoreEntryViewModel> gridViewModel;
        private String thumbnailURL;
        private String lowestSalePriceString;
        private String highestNormalPrice;
        private String savingString;
        private boolean isReleased;
        private boolean hasReleaseInfo = false;

        public DealAdapterModelBuilder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public DealAdapterModelBuilder setGridViewModel(List<StoreEntryViewModel> gridViewModel) {
            this.gridViewModel = gridViewModel;
            return this;
        }

        public DealAdapterModelBuilder setThumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
            return this;
        }

        public DealAdapterModelBuilder setLowestSalePriceString(String lowestSalePriceString) {
            this.lowestSalePriceString = lowestSalePriceString;
            return this;
        }

        public DealAdapterModelBuilder setHighestNormalPrice(String highestNormalPrice) {
            this.highestNormalPrice = highestNormalPrice;
            return this;
        }

        public DealAdapterModelBuilder setSavingString(String savingString) {
            this.savingString = savingString;
            return this;
        }

        public DealAdapterModelBuilder setReleaseDate(String releaseDate) {
            this.releaseDateString = releaseDate;
            return this;
        }

        public DealAdapterModelBuilder setReleased(boolean released) {
            this.isReleased = released;
            return this;
        }

        public DealAdapterModel build() {
            return new DealAdapterModel(this);
        }

        public DealAdapterModelBuilder setHasReleaseInfo(boolean hasReleaseInfo) {
            this.hasReleaseInfo = hasReleaseInfo;
            return this;
        }
    }
}
