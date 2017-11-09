package com.rockspin.bargainbits.ui.activities.main.storesdrawer;

import com.rockspin.bargainbits.data.models.Store;
import com.rockspin.bargainbits.ui.BasePresenter;
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.adapter.StoreEnabled;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;

import static com.rockspin.bargainbits.di.modules.SchedulersModule.IO;
import static com.rockspin.bargainbits.di.modules.SchedulersModule.MAIN;

public class StoresDrawerPresenter extends BasePresenter<StoresDrawerPresenter.IView, StoresDrawerModel, Void> {
    private final Scheduler mainThread;
    private final Scheduler ioThread;

    @Inject StoresDrawerPresenter(StoresDrawerModel storesDrawerModel, @Named(MAIN) Scheduler mainThread, @Named(IO) Scheduler ioThread) {
        super(storesDrawerModel);
        this.mainThread = mainThread;
        this.ioThread = ioThread;
    }

    @Override public void start(IView iView) {
        super.start(iView);

        // update ui to reflect store state.
        iView.setAllStoresChecked(getModel().getAllStoresEnabled());

        // create an observable that loads all stores.
        final Observable<List<StoreEnabled>> loadStores = getModel().loadStores()
                                                                    .subscribeOn(ioThread)
                                                                    .observeOn(mainThread)
                                                                    .doOnSubscribe(() -> iView.setStoresLoading(true))
                                                                    .doOnTerminate(() -> getView().setStoresLoading(false))
                                                                    .doOnNext(stores -> getView().setListContent(stores))
                                                                    .doOnError(throwable -> getView().failedToLoadStores(throwable))
                                                                    .onErrorResumeNext(Observable.empty());

        addSubscription(loadStores.subscribe());

        // load all stores, then listen for clicks to reload stores
        addSubscription(iView.onClickReloadStores().flatMap(aVoid -> loadStores).subscribe());

        // update the list and model when user enables all stores
        addSubscription(iView.onAllStoresSwitched().doOnNext(checked -> {
            getModel().saveAllStoresEnabledState(checked);
            getView().updateStoreList(checked);
        }).subscribe());

        // when a store is selected update the model with the new state.
        addSubscription(iView.onStoreSelected().doOnNext(storePosition -> {
            final Store store = storePosition.store;
            final boolean checked = storePosition.checked;
            getModel().saveStoreEnabledState(store.getStoreId(), store.getStoreName(), checked);
        }).subscribe());
    }

    public interface IView {

        Observable<StoreEnabled> onStoreSelected();

        Observable<Boolean> onAllStoresSwitched();

        Observable<Void> onClickReloadStores();

        void failedToLoadStores(Throwable throwable);

        void setStoresLoading(boolean visible);

        void setAllStoresChecked(boolean allStoresEnabled);

        void setListContent(List<StoreEnabled> stores);

        void updateStoreList(Boolean allStoresChecked);
    }
}
