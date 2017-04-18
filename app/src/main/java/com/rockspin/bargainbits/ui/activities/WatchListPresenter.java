package com.rockspin.bargainbits.ui.activities;

import com.rockspin.bargainbits.ui.BasePresenter;
import javax.inject.Inject;
import rx.Observable;

public class WatchListPresenter extends BasePresenter<WatchListPresenter.View, Void, Void> {

    @Inject public WatchListPresenter() { /* not used */ }

    @Override public void start(View view) {
        super.start(view);

        addSubscription(view.onListLoading().subscribe(view::showListLoading));
        addSubscription(view.onBackButtonPressed().subscribe(aVoid -> view.closeWatchList()));
    }

    public interface View {
        Observable<Boolean> onListLoading();

        Observable<Void> onBackButtonPressed();

        void closeWatchList();

        void showListLoading(boolean loading);
    }
}
