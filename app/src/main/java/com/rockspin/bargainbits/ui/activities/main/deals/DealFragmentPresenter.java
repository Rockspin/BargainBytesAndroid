package com.rockspin.bargainbits.ui.activities.main.deals;

import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter;
import com.rockspin.bargainbits.ui.mvp.BaseMvpView;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.Observable;

public class DealFragmentPresenter extends BaseMvpPresenter<DealFragmentPresenter.View> {

    @Inject protected DealFragmentPresenter() {
        super();
    }

    @Override
    public void onViewCreated(@NotNull View view) {
        super.onViewCreated(view);

        addLifetimeDisposable(view.onListLoading().subscribe(view::showListLoading));
        addLifetimeDisposable(view.onReloadList().subscribe(aVoid -> view.refreshList()));
    }

    public interface View extends BaseMvpView {

        Observable<Boolean> onListLoading();

        Observable<Object> onReloadList();

        void refreshList();

        void showListLoading(boolean loading);
    }
}
