package com.rockspin.bargainbits.ui.activities.search.gameinfo;

import com.rockspin.bargainbits.data.models.GameSearchResult;
import com.rockspin.bargainbits.di.modules.SchedulersModule;
import com.rockspin.bargainbits.ui.BasePresenter;
import com.rockspin.bargainbits.ui.activities.search.gameinfo.recycler.GamesInfoDealsViewModel;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import rx.Scheduler;

public class GameInfoPresenter extends BasePresenter<GameInfoPresenter.IView, GameInfoModel, GameSearchResult> {

    private GameSearchResult game;

    @Inject @Named(SchedulersModule.MAIN) Scheduler mainThread;

    @Inject public GameInfoPresenter(GameInfoModel gameInfoModel) {
        super(gameInfoModel);
    }

    @Override public void setData(GameSearchResult game) {
        this.game = game;
    }

    public void start(IView iView) {
        super.start(iView);

        addSubscription(getView().onItemSelected()
                                 .map(model::getUrlForIndex)
                                 .doOnNext(view::openDealUrl)
                                 .subscribe());

        getView().onGameLoaded(game);

        addSubscription(getModel().LoadGameInfo(game.getGameID())
                  .observeOn(mainThread)
                  .subscribe(getView()::refreshList, getView()::onRefreshListFailed));
    }

    public void addGameToWatchList() {
        getView().addItemToWatchList(WatchedItem.Companion.from(game));
    }

    interface IView {

        Observable<Integer> onItemSelected();

        void onGameLoaded(GameSearchResult game);

        void refreshList(List<GamesInfoDealsViewModel> abbreviatedDeals);

        void onRefreshListFailed(Throwable throwable);

        void addItemToWatchList(WatchedItem watchedItem);

        void openDealUrl(String url);
    }
}

