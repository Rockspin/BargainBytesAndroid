package com.rockspin.bargainbits.ui.views.deallist;

import com.rockspin.bargainbits.data.repository.DealRepository;
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListPresenter;
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListViewImpl;
import rx.Observable;

public interface DealsListView {

    void viewWillShow();

    /**
     * Load a list of deals.
     * @param dealsSorting the list of deals we will be displaying.
     */
    void loadDealsWithSorting(DealRepository.EDealsSorting dealsSorting);

    /**
     * Load deals based on the deals the user has added to his watchlist.
     */
    void loadDealsInWatchList();


    void viewWillHide();

    /**
     * Emits when the list begins to load is loading. Will emit true if the list contains items
     * @return
     */
    Observable<Boolean> onListLoading();

    boolean isListEmpty();

    void setPresenter(DealsListPresenter dealsListPresenter);
}
