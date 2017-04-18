/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.models.cheapshark;

/**
 * Store object returned from API.
 */
public class Store {

    private String storeID; //: "2",
    private String storeName; //: "Gamer's Gate"

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(final String pStoreId) {
        this.storeID = pStoreId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(final String pStoreName) {
        this.storeName = pStoreName;
    }
}
