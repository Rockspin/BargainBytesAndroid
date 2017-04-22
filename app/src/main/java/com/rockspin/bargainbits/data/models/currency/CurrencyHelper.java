package com.rockspin.bargainbits.data.models.currency;

import java.util.Currency;
import java.util.Locale;

/**
 * Utility that can be used to convert values from USD to active currency and back.
 */
public class CurrencyHelper {
    private final String countryName;
    private final BBCurrency bbCurrency;

    public CurrencyHelper(BBCurrency bbCurrency) {
        Currency javaCurrency = Currency.getInstance(bbCurrency.getIsoName());
        this.bbCurrency = bbCurrency;
        this.countryName = javaCurrency.getDisplayName(Locale.getDefault());
    }

    public String getFormattedPrice(float pPrice) {
        final java.util.Currency javaCurrency = java.util.Currency.getInstance(bbCurrency.getIsoName());
        return javaCurrency.getSymbol() + " " + String.format("%." + Math.max(0, javaCurrency.getDefaultFractionDigits()) + "f", pPrice * bbCurrency.getExchangeRate());
    }

    public String getCountryName() {
        return countryName;
    }

    public String getIsoCOde() {
        return bbCurrency.getIsoName();
    }

    public Float getActiveCurrencyValueOf(final float price) {
        return price * bbCurrency.getExchangeRate();
    }

    public Float getBaseCurrencyValueOf(final float price) {
        return price / bbCurrency.getExchangeRate();
    }
}