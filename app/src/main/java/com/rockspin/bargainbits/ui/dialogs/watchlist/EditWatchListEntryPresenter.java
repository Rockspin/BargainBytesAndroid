package com.rockspin.bargainbits.ui.dialogs.watchlist;

import com.fernandocejas.arrow.checks.Preconditions;
import com.rockspin.bargainbits.ui.BasePresenter;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import javax.inject.Inject;
import rx.Observable;
import rx.Single;

public class EditWatchListEntryPresenter extends BasePresenter<EditWatchListEntryPresenter.IView, EditWatchListModel, WatchedItem> {
    public enum WatchedState {
        ANY, PRICE
    }

    public static final float MAX_VALUE_USD = 150;
    public static final float SELECTION_INTERVALS = 200;

    private final EditWatchListModel model;
    private WatchedItem originalItem;

    @Inject public EditWatchListEntryPresenter(EditWatchListModel model) {
        super(model);
        this.model = model;
    }

    //TODO: this should pass a gameID and create a watched item.
    public void start(final WatchedItem passedItem, IView iView) {
        super.start(passedItem, iView);

        iView.setSelectionIntervals((int) SELECTION_INTERVALS);

        //TODO: make this nice i.e one pass in correct data.
        // the passedItem can be a new watchlist item or an old on recreated from the game Id.
        // as such the data it holds can be incorrect.
        // verify the data we have here is the same as in database and pass back the correct data.
        originalItem = model.verifyDataCorrect(passedItem);

        addSubscription(Observable.just(originalItem)
                                  .doOnNext(watchedItem -> getView().setGameName(watchedItem.gameName()))
                                  .doOnNext(this::updateSeekbar)
                                  .switchMap(ignored -> onWatchStateChanged())
                                  .doOnNext(initialWatchedState -> getView().showWatchStateSelected(initialWatchedState))
                                  .switchMap(watchedState -> {
                                      switch (watchedState) {
                                          case ANY:
                                              return getAnyStatePrice().toObservable();
                                          case PRICE:
                                              return onSelectedPriceChanges();
                                          default:
                                              return Observable.error(new IllegalStateException("no state emitted"));
                                      }
                                  })
                                  //TODO: viewWillHide creating a new object everytime we move the slider
                                  .map(price -> WatchedItem.create(originalItem.gameName(), originalItem.gameId(), price))
                                  .switchMap(watchedItem -> getView().onDialogDismissObservable()
                                                                   .doOnNext(userSelectedAddItem -> {
                                                                       if (userSelectedAddItem) {
                                                                           model.addItemToWatchList(watchedItem);
                                                                       } else {
                                                                           model.removeItemFromWatchList(watchedItem);
                                                                       }
                                                                   }))
                                  .subscribe());
    }

    private Observable<Float> onSelectedPriceChanges() {
        return getView().onSelectedPercentagePrice()
                        .map(selectedInterval -> MAX_VALUE_USD * (selectedInterval / SELECTION_INTERVALS))
                        .startWith(originalItem.watchedPrice())
                        .filter(price -> price > 0)
                        .distinctUntilChanged()
                        .withLatestFrom(model.onCurrentCurrencyChanged(), (priceInUsd, currencyHelper) -> currencyHelper.getActiveCurrencyValueOf(priceInUsd))
                        .doOnNext(price -> getView().showCustomSaleText(model.getActiveCurrencySymbol(), price));
    }

    private Single<Float> getAnyStatePrice() {
        return Single.just(-1.0f)
                     .doOnSuccess(ignored -> getView().showAnySaleText());
    }

    private Observable<WatchedState> onWatchStateChanged() {
        return getView().onWatchStateChanged()
                        .startWith(getOriginalState())
                        .distinctUntilChanged();
    }

    private WatchedState getOriginalState() {
        return originalItem.hasCustomWatchPrice() ? WatchedState.PRICE : WatchedState.ANY;
    }

    private void updateSeekbar(WatchedItem watchedItem) {
        if (watchedItem.watchedPrice() > 0) {
            getView().setSeekbarProgress((int) (watchedItem.watchedPrice() * (SELECTION_INTERVALS / MAX_VALUE_USD)));
        } else {
            getView().setSeekbarProgress(1);
        }
    }

    public boolean isInWatchList() {
        Preconditions.checkNotNull(originalItem, "item should not be null");
        return model.isItemInWatchList(originalItem);
    }

    public interface IView {

        /**
         * emits a boolean when the dialog is dismissed, if true an item was added/ edited if false an item was removed
         *
         * @return a Observable that emits a boolean, true when an item was added/ edited if false an item was removed
         */
        Observable<Boolean> onDialogDismissObservable();

        /**
         * Emits when the user changes the way the type of watched item they are editing. Emits once and repeats selected value.
         *
         * @return Observable that Emits when the user changes the way the type of watched item they are editing.
         */
        Observable<WatchedState> onWatchStateChanged();

        /**
         * emits a value between 0 - 100 that represents a value a requested price.
         *
         * @return Observable that emits an integer.
         */
        Observable<Integer> onSelectedPercentagePrice();

        void setSeekbarProgress(int currentValue);

        void setGameName(String gameName);

        void showAnySaleText();

        void showCustomSaleText(String activeCurrencySymbol, float price);

        void setSelectionIntervals(int maxPercentage);

        void showWatchStateSelected(WatchedState watchedState);
    }
}
