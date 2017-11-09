package com.rockspin.bargainbits.ui.views.deallist.view;

import android.support.annotation.NonNull;

import com.rockspin.bargainbits.data.models.cheapshark.CompactDeal;
import com.rockspin.bargainbits.data.repository.DealRepository;
import com.rockspin.bargainbits.ui.BasePresenter;
import com.rockspin.bargainbits.ui.dialogs.store_picker.StorePickerAdapter;
import com.rockspin.bargainbits.ui.views.deallist.DealShareModel;
import com.rockspin.bargainbits.ui.views.deallist.recycleview.DealAdapterModel;
import com.rockspin.bargainbits.watch_list.WatchedItem;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class DealsListPresenter extends BasePresenter<DealsListPresenter.View, DealsListModel, Void> {

    @Inject public DealsListPresenter(DealsListModel model) {
        super(model);
    }

    public void start( View view) {
        super.start(view);

        // listen for a deal being opened from the list.
        addSubscription(view.onClickOpenDeal()
                                 .flatMap(indexInDealList -> {
                                     CompactDeal compactDeal = model.getCompactDealAtIndex(indexInDealList);
                                     if (compactDeal.getDealList().size() > 1) {
                                         return model.createStorePickerModels(indexInDealList)
                                                     .flatMap(view::showStorePicker)
                                                     .map(indexInStorePicker -> compactDeal.getDealList().get(indexInStorePicker).getDealID());
                                     } else {
                                         return Observable.just(model.getDealIdAt(indexInDealList));
                                     }
                                 })
                                 .map(model::getDealUrlFromDealID)
                                 .doOnNext(this.view::openDealUrl)
                                 .subscribe());

        // listen for editing the watchlist from the list.
        addSubscription(view.onClickEditDeal().map(model::getWatchedItemForIndex).doOnNext(view::editOrAddToWatchList).subscribe());

        // listen for a deal being shared from the list.
        addSubscription(view.onClickShareDeal().doOnNext(model::sendGameSharedAnalytic).map(model::getShareModel).doOnNext(view::shareDeal).subscribe());

        addSubscription(model.onWatchListEdited().doOnNext(watchedItemActionPair -> {

            final WatchedItem watchedItem = watchedItemActionPair.first;
            switch (watchedItemActionPair.second) {
                case EDITED:
                    this.view.showGameInWatchlistEdited(watchedItem);
                    break;
                case ADDED:
                    this.view.showItemAddedToWatchList(watchedItem);
                    break;
                case FULL:
                    this.view.showWatchListFull();
                    break;
                case REMOVED:
                    this.view.showItemRemovedFromWatchlist(watchedItem);
                    break;
            }
        }).subscribe());

        // listen for adding and removing items from watchlist.
        addSubscription(model.onItemRemovedFromWatchList().filter(integer -> model.isShowingWatchedItems()).subscribe(index -> {
            view.removeDealAtIndex(index);
            view.setLoadingVisible(false);
        }));
        addSubscription(model.onItemAddedToWatchList().filter(integer -> model.isShowingWatchedItems()).subscribe(dealAdapterModel -> {
            view.addDealToList(dealAdapterModel);
            view.setLoadingVisible(false);
        }));
    }

    public void loadDealsWithSorting(DealRepository.EDealsSorting dealsSorting) {
        model.setMode(DealsListModel.Mode.BROWSING_DEALS);
        addSubscription(checkForInternet()
            .skipWhile(connectedToInternet -> !connectedToInternet)
            .flatMap(ignored -> model.onDealsRefreshed(dealsSorting).compose(populateListOrShowEmpty()))
            .subscribe());
    }

    public void loadDealsInWatchList() {
        model.setMode(DealsListModel.Mode.WATCH_LIST);

        addSubscription(checkForInternet().skipWhile(connectedToInternet -> !connectedToInternet)
                                          .flatMap(ignored -> model.loadWatchedItems().compose(populateListOrShowEmpty()))
                                          .subscribe());
    }

    private Observable<Boolean> checkForInternet() {
        return Observable.just(model.isConnectedToInternet()).doOnNext(connectedToInternet -> {
            if (!connectedToInternet) {
                view.setLoadingVisible(false);
            }
        });
    }

    private Observable.Transformer<List<DealAdapterModel>, Void> populateListOrShowEmpty() {
        return listObservable -> listObservable.doOnSubscribe(() -> view.setLoadingVisible(true)).doOnNext(list -> {
            if (list.size() > 0) {
                view.updateListView(list);
            }
            view.setLoadingVisible(false);
        }).doOnError(throwable -> {
            view.showLoadingDealsError(throwable);
            view.setLoadingVisible(false);
        }).onErrorResumeNext(Observable.empty()).map(currencyHelper1 -> null);
    }

    public void addItemToWatchList(WatchedItem watchedItem) {
        model.addItemToWatchList(watchedItem);
    }

    public interface View {

        Observable<Void> onBackPressed();

        Observable<Integer> showStorePicker(List<StorePickerAdapter.StorePickerData> deals);

        Observable<Integer> onClickOpenDeal();

        Observable<Integer> onClickEditDeal();

        Observable<Integer> onClickShareDeal();

        void openDealUrl(@NonNull String url);

        void setLoadingVisible(boolean visible);

        void updateListView(@NonNull List<DealAdapterModel> result);

        void shareDeal(DealShareModel dealShareModel);

        void showLoadingDealsError(Throwable throwable);

        void editOrAddToWatchList(WatchedItem watchedItem);

        void removeDealAtIndex(Integer integer);

        void addDealToList(DealAdapterModel dealAdapterModel);

        void showItemAddedToWatchList(WatchedItem first);

        void showGameInWatchlistEdited(WatchedItem watchedItem);

        void showWatchListFull();

        void showItemRemovedFromWatchlist(WatchedItem watchedItem);
    }


}
