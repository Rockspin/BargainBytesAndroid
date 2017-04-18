package com.rockspin.bargainbits.ui.activities.main.deals;

import com.rockspin.bargainbits.ui.BasePresenter;
import javax.inject.Inject;
import rx.Observable;

public class DealFragmentPresenter extends BasePresenter<DealFragmentPresenter.View, DealFragmentPresenter.Model, Void> {

    @Inject protected DealFragmentPresenter() {
        super();
    }

    @Override public void start(View view) {
        super.start(view);

        addSubscription(view.onListLoading().subscribe(view::showListLoading));
        addSubscription(view.onReloadList().subscribe(aVoid -> view.refreshList()));
    }

    public interface View {

        Observable<Boolean> onListLoading();

        Observable<Void> onReloadList();

        void refreshList();

        void showListLoading(boolean loading);
    }

    public interface Model { /* not used */ }
}
