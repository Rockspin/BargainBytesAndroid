package com.rockspin.bargainbits.ui.activities.main;

import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.currency.CurrencyNamesAndISOCodes;
import com.rockspin.bargainbits.ui.BasePresenter;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import javax.inject.Inject;
import rx.Observable;

public class MainActivityPresenter extends BasePresenter<MainActivityPresenter.IView, MainActivityModel, Void> {

    @Inject MainActivityPresenter(MainActivityModel mainModel) {
        super(mainModel);
    }

    @Override public void start(MainActivityPresenter.IView iView) {
        super.start(iView);
        // when the drawer opens
        addSubscription(getView().onDrawerOpened().doOnNext(opened -> {
            // Change the title and record analytics events
            if (opened) {
                getView().setTitle(R.string.store_filter);
                getModel().sendDrawerToggledAnalytic(true);
            } else {
                getView().setTitle(R.string.title_activity_all_deals);
                getModel().sendDrawerToggledAnalytic(false);
            }
        }).subscribe());

        // when the user pressed the open watchlist button
        addSubscription(getView().onOpenWatchListPressed().doOnNext(aVoid -> getView().openWatchList()).subscribe());
        // when the user pressed the stores filter button
        addSubscription(getView().onStoresFilterPressed().doOnNext(aVoid -> getView().setStoresDrawerOpen(true)).subscribe());
        // when the user pressed the select currency button
        addSubscription(getView().onSelectCurrencyPressed()
                                 .flatMap(aVoid1 -> getModel().onCurrenciesUpdated())
                                 .doOnNext(getView()::showSelectCurrencyDialog)
                                 .doOnError(getView()::errorLoadingCurrencies)
                                 .subscribe());

        // when the user pressed the app rate button
        addSubscription(getView().onRateAppPressed().doOnNext(aVoid -> getView().goToStoreAndRate()).subscribe());
        // when the user pressed the feed back button
        addSubscription(getView().onFeedbackPressed().doOnNext(aVoid -> getView().sendFeedbackEmail()).subscribe());
        addSubscription(getView().onShareAppPressed().doOnNext(aVoid -> getView().showShareAppDialog()).subscribe());
        addSubscription(getView().onViewPagerChanged().doOnNext(getView()::setNavigationSpinnerItem).subscribe());
        //watch list reactions

        addSubscription(getModel().onCurrencyUpdatedToNonDefault().doOnNext(aVoid -> getView().showCurrencyDisclaimer()).subscribe());

        addSubscription(getView().onNavigationItemSelected().doOnNext(getView()::selectTab).subscribe());

        addSubscription(getModel().onInternetAvailabilityChanged().doOnNext(internetAvailable -> {
            if (internetAvailable) {
                getView().hideNoInternetMessage();
            } else {
                getView().showNoInternetMessage();
            }
        }).subscribe());
    }

    public void addItemToWatchList(WatchedItem watchedItem) {
        model.addItemToWatchList(watchedItem);
    }

    /**
     * The view contains events generates by user interaction with the screen and functions that change the information
     */
    public interface IView {

        Observable<Boolean> onDrawerOpened();

        Observable<Void> onOpenWatchListPressed();

        Observable<Void> onStoresFilterPressed();

        Observable<Void> onSelectCurrencyPressed();

        Observable<Void> onRateAppPressed();

        Observable<Void> onShareAppPressed();

        Observable<Void> onFeedbackPressed();

        Observable<Integer> onNavigationItemSelected();

        Observable<Integer> onViewPagerChanged();

        // inputs from presenter to view
        void openWatchList();

        void sendFeedbackEmail();

        void goToStoreAndRate();

        void setTitle(int store_filter);

        void showCurrencyDisclaimer();

        void showShareAppDialog();

        void errorLoadingCurrencies(Throwable throwable);

        void showSelectCurrencyDialog(CurrencyNamesAndISOCodes currencyNamesAndISOCodes);

        void setStoresDrawerOpen(boolean open);

        void selectTab(Integer integer);

        void setNavigationSpinnerItem(Integer integer);

        void showNoInternetMessage();

        void hideNoInternetMessage();
    }
}
