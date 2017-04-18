package com.rockspin.bargainbits.ui.views.deallist;

public class DealShareModel {

    private final int index;
    private final String gameName;
    private final float savingPercentage;

    public DealShareModel(int index, String gameName, float savingPercentage) {
        this.index = index;
        this.gameName = gameName;
        this.savingPercentage = savingPercentage;
    }

    public int getIndex() {
        return index;
    }

    public String getGameName() {
        return gameName;
    }

    public float getSavingPercentage() {
        return savingPercentage;
    }
}
