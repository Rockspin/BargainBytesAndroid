package com.rockspin.bargainbits.ui.activities.search.results;

import com.rockspin.bargainbits.data.models.GameSearchResult;
import com.rockspin.bargainbits.data.repository.CurrencyRepository;
import com.rockspin.bargainbits.data.rest_client.ICheapsharkAPIService;
import com.rockspin.bargainbits.ui.activities.search.results.recycler.SearchResultsAdapterModel;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class SearchResultsModel {

    private final List<GameSearchResult> gameList = new ArrayList<>();
    private String searchQuery;
    private final ICheapsharkAPIService iCheapsharkAPIService;
    private final CurrencyRepository currencyRepository;
    private final IAnalytics iAnalytics;

    @Inject SearchResultsModel(ICheapsharkAPIService iCheapsharkAPIService, CurrencyRepository currencyRepository, IAnalytics iAnalytics) {
        this.iCheapsharkAPIService = iCheapsharkAPIService;
        this.currencyRepository = currencyRepository;
        this.iAnalytics = iAnalytics;
    }

    public Observable<List<GameSearchResult>> searchGames(String mSearchQuery) {
        return iCheapsharkAPIService.searchGames(mSearchQuery);
    }

    public Observable<List<SearchResultsAdapterModel>> loadResults(String mSearchQuery) {
        if(searchQuery == null || gameList.isEmpty()) {
            searchQuery = mSearchQuery;
            return currencyRepository.onCurrentCurrencyChanged().flatMap(currencyHelper -> iCheapsharkAPIService.searchGames(mSearchQuery).doOnNext(games -> {
                gameList.clear();
                gameList.addAll(games);
            }).flatMap(Observable::from).map(game -> {
                final String cheapestPrice = currencyHelper.getFormattedPrice((float) game.getCheapestPrice());
                return new SearchResultsAdapterModel(game.getThumbnailUrl(), game.getName(), cheapestPrice);
            }).toList());
        }
        return Observable.empty();
    }

    public void onGameClicked(String external) {
        iAnalytics.onGameClicked(external);
    }

    public GameSearchResult getGameAtIndex(Integer integer) {
        return gameList.get(integer);
    }
}
