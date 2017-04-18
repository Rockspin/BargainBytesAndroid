/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.models.currency;

/**
 * Currency object returned from FixerIO API.
 */
public final class BBCurrency {
    private String isoName; // EUR, GBP, BGN, USD
    private float exchangeRate; // exchange rate

    public BBCurrency(final String isoName, final float exchangeRate) {
        this.isoName = isoName;
        this.exchangeRate = exchangeRate;
    }

    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(final String isoName) {
        this.isoName = isoName;
    }

    public float getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(final float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
