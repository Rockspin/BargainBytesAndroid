package com.rockspin.bargainbits.data.repository;

import android.support.annotation.NonNull;
import com.rockspin.bargainbits.data.models.cheapshark.CompactDeal;
import com.rockspin.bargainbits.data.models.cheapshark.Deal;
import com.rockspin.bargainbits.data.rest_client.ICheapsharkAPIService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DealRepository {

    @Inject ICheapsharkAPIService iCheapsharkAPIService;
    @Inject StoresRepository storesRepository;

    @Inject DealRepository() { /* not used */ }

    /**
     * Load a list of deals with a given sorting order. Observable will emit a new list when the currently active stores are changed.
     * @param eDealsSorting sorting for deals
     * @return an observable that wil
     */
    public Observable<List<CompactDeal>> onDealsRefreshed(EDealsSorting eDealsSorting) {
        return storesRepository.onSelectedStoresChanged()
                               .map(selectedStoresString -> buildRequestMap(eDealsSorting, selectedStoresString))
                               .flatMap(this::requestDeals)
                               .flatMap(this::groupAndSortDeals);
    }

    private @NonNull Map<String, String> buildRequestMap(EDealsSorting eDealsSorting, String filterString) {
        Map<String, String> dealRequestMap = new HashMap<>();
        dealRequestMap.put("onSale", "1");
        dealRequestMap.put("sortBy", eDealsSorting.toString());

        if (!filterString.isEmpty()) {
            dealRequestMap.put("storeID", filterString);
        }

        return dealRequestMap;
    }


    private Observable<List<Deal>> requestDeals(Map<String, String> stringStringMap) {
        return iCheapsharkAPIService.getDeals(stringStringMap).first().subscribeOn(Schedulers.io());
    }

    private Observable<List<CompactDeal>> groupAndSortDeals(List<Deal> serverDeals) {
        // TODO: investigate replacing with a single reduce call will be 0n and not 2n complexity
        final Observable<Map<String, Collection<Deal>>> mapObservable = Observable.from(serverDeals).toMultimap(Deal::getGameID);

        return Observable.from(serverDeals)
                         .map(Deal::getGameID)
                         .distinct() // remove duplicates
                         .withLatestFrom(mapObservable, (s, stringCollectionMap) -> stringCollectionMap.get(s))
                         .flatMap(sortedDeals -> Observable.from(sortedDeals).toSortedList((deal, deal2) -> Float.compare(deal2.getSalePrice(), deal.getSalePrice())))
                         .map(sortedDeals -> new CompactDeal(sortedDeals, sortedDeals.get(0)))
                         .toList();
    }

    public String loadDealUrl(String pDealID) {
        return "http://www.cheapshark.com/redirect?dealID=" + pDealID;
    }

    /**
     * Deals sorting.
     */
    public enum EDealsSorting {

        /**
         * DEALS_RATING
         */
        DEALS_RATING("Deal Rating"),
        /**
         *
         */
        RELEASE("Release"),
        /**
         * SAVING
         */
        SAVING("Savings"),
        /**
         * PRICE
         */
        PRICE("Price"),
        /**
         *
         */
        METACRITIC("Metacritic"),
        /**
         *
         */
        TITLE("Title"),
        /**
         *
         */
        STORE("Store");

        private final String mText;

        /**
         * Endpoints for URL.
         *
         * @param pText the name of this endpoint according to api.
         */
        EDealsSorting(final String pText) {
            this.mText = pText;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override public String toString() {
            return mText;
        }
    }
}
