package com.rockspin.bargainbits.ui.activities.search.results;

import com.rockspin.bargainbits.data.models.GameSearchResult;
import com.rockspin.bargainbits.di.modules.SchedulersModule;
import com.rockspin.bargainbits.ui.BasePresenter;
import com.rockspin.bargainbits.ui.activities.search.results.recycler.SearchResultsAdapterModel;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import rx.Scheduler;
import timber.log.Timber;

public class SearchResultsPresenter extends BasePresenter<SearchResultsPresenter.IView , SearchResultsModel, String> {

    @Inject @Named(SchedulersModule.MAIN) Scheduler main;

    @Inject SearchResultsPresenter(SearchResultsModel searchResultsModel) {
        super(searchResultsModel);
    }

    @Override
    public void start(String searchQuery, IView iView) {
        super.start(searchQuery, iView);
        iView.setActionBarSearchTitle(searchQuery);
        iView.checkForEmptyList();

       addSubscription(getModel().loadResults(searchQuery).observeOn(main).subscribe(iView::updateList, throwable -> {
            Timber.e(throwable, "Error retrieving game list for search term " + searchQuery);
            iView.showError();
        }));

        addSubscription(iView.onItemSelected().subscribe(integer -> {
            GameSearchResult game = model.getGameAtIndex(integer);
            iView.displayInfoForGame(game, integer);
        }));

        addSubscription(iView.onBackButtonPressed().subscribe(ignored -> iView.closeView() ));
    }

    public interface IView {

        void setActionBarSearchTitle(String searchQuery);

        void checkForEmptyList();

        void updateList(List<SearchResultsAdapterModel> games);

        void showError();

        void setTitle(String searchQuery);

        void displayInfoForGame(GameSearchResult game, Integer gameThumb);

        Observable<Integer> onItemSelected();

        Observable<Void> onBackButtonPressed();

        void closeView();
    }
}
