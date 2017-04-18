package com.rockspin.bargainbits.data.models.cheapshark;

/**
 * Cheapest price ever.
 */
public final class CheapestPriceEver {

    private String price;
    private Integer date;

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(final Integer date) {
        this.date = date;
    }
}
