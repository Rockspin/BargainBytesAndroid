package com.rockspin.bargainbits.ui.activities.search;

import android.util.Pair;
import android.view.View;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.ui.BasePresenter;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import javax.inject.Inject;
import rx.Observable;
import timber.log.Timber;

public final class GamesSearchPresenter extends BasePresenter<GamesSearchPresenter.IView, GamesSearchPresenter.IModel, String> {

    @Inject GamesSearchPresenter(GamesSearchModel gamesSearchModel) {
        super(gamesSearchModel);
    }


    @Override public void start(String searchQuery, IView iView) {
        super.start(searchQuery, iView);

        getModel().onGameSearchedFor(searchQuery);
        getView().setTitle(searchQuery);

        addSubscription(getView().onBackClicked().doOnNext(ignored -> getView().removeFragmentOrCloseActivity()).subscribe());

        addSubscription(getModel().onNetworkChanged()
                             .subscribe(getView()::onNetworkChanged, throwable -> Timber.d("error detecting network change")));

        addSubscription(getModel().onWatchListEdited()
                             .subscribe(watchedItemActionPair -> {
                                 switch (watchedItemActionPair.second) {
                                     case EDITED:
                                         getView().watchListEdited(watchedItemActionPair.first);
                                         break;
                                     case ADDED:
                                         getView().watchListAdded(watchedItemActionPair.first);
                                         break;
                                     case FULL:
                                         getView().watchListFull();
                                         break;
                                     case REMOVED:
                                         getView().onWatchListItemRemoved(watchedItemActionPair.first, v -> getModel().addToWatchList(watchedItemActionPair.first));
                                         break;
                                 }
                             }));
    }

    public interface IView {

        void onNetworkChanged(Boolean aBoolean);

        void watchListEdited(WatchedItem first);

        void watchListAdded(WatchedItem first);

        void watchListFull();

        void onWatchListItemRemoved(WatchedItem first, View.OnClickListener p1);

        void setTitle(String query);

        void removeFragmentOrCloseActivity();

        Observable<Void> onBackClicked();
    }

    public interface IModel {
        Observable<Boolean> onNetworkChanged();

        Observable<Pair<WatchedItem, WatchListRepository.Action>> onWatchListEdited();

        void onGameSearchedFor(String query);

        void addToWatchList(WatchedItem first);

    }
}
