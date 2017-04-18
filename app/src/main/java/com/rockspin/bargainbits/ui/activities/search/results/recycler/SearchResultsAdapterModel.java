package com.rockspin.bargainbits.ui.activities.search.results.recycler;

public class SearchResultsAdapterModel {
    public final String thumb;
    public final String title;
    public final String cheapestPrice;

    public SearchResultsAdapterModel(String thumb, String title, String cheapestPrice) {
        this.thumb = thumb;
        this.title = title;
        this.cheapestPrice = cheapestPrice;
    }
}
