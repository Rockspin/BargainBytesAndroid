package com.rockspin.bargainbits.data.models.currency;

import java.io.Serializable;

public class CurrencyNamesAndISOCodes implements Serializable {
    public final String[] isoList;
    public final String[] currencyList;

    public CurrencyNamesAndISOCodes(final String[] isoList, final String[] currencyList) {
        this.isoList = isoList;
        this.currencyList = currencyList;
    }
}
