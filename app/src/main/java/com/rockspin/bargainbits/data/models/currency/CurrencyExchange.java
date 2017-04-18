/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.models.currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Currency exchange object returned from FixerIO API.
 */
public class CurrencyExchange {
    private String base; // "EUR"
    private String date; // "2014-08-12
    private List<BBCurrency> BBCurrencyRates = new ArrayList<>();

    public String getBase() {
        return base;
    }

    public void setBase(final String base) {
        this.base = base;
    }

    /**
     * Retrieves the date in the form "yyyy-mm-dd"
     *
     * @return string representation of the date
     */
    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public List<BBCurrency> getCurrencies() {
        return BBCurrencyRates;
    }

    public void setRates(final List<BBCurrency> rates) {
        this.BBCurrencyRates = rates;
    }
}
