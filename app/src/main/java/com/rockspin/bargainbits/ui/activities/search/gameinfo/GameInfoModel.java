package com.rockspin.bargainbits.ui.activities.search.gameinfo;

import android.graphics.drawable.Drawable;

import com.rockspin.bargainbits.data.repository.CurrencyRepository;
import com.rockspin.bargainbits.data.repository.DealRepository;
import com.rockspin.bargainbits.data.repository.StoresRepository;
import com.rockspin.bargainbits.data.models.AbbreviatedDeal;
import com.rockspin.bargainbits.data.models.GameInfo;
import com.rockspin.bargainbits.data.rest_client.ICheapsharkAPIService;
import com.rockspin.bargainbits.ui.activities.search.gameinfo.recycler.GamesInfoDealsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GameInfoModel {

    private final List<AbbreviatedDeal> listModels = new ArrayList<>();
    private final StoresRepository storesRepository;
    private final DealRepository dealRepository;
    private final CurrencyRepository currencyRepository;
    private final ICheapsharkAPIService iCheapsharkAPIService;

    @Inject GameInfoModel(StoresRepository storesRepository, DealRepository dealRepository, CurrencyRepository currencyRepository,
        ICheapsharkAPIService iCheapsharkAPIService) {
        this.storesRepository = storesRepository;
        this.dealRepository = dealRepository;
        this.currencyRepository = currencyRepository;

        this.iCheapsharkAPIService = iCheapsharkAPIService;
    }

    public Observable<List<GamesInfoDealsViewModel>> LoadGameInfo(String gameID) {
        return currencyRepository.onCurrentCurrencyChanged().flatMap(currencyHelper -> {

            final Observable<GameInfo> loadGameInfo = iCheapsharkAPIService.getGameInfo(gameID);
            final Observable<AbbreviatedDeal> sortedDeals = loadGameInfo.flatMap(gameInfo -> Observable.from(gameInfo.getDeals())).toSortedList((dealOne, dealTwo) -> {
                final float dealOnePrice = (float) dealOne.getPrice();
                final float dealTwoPrice = (float) dealTwo.getPrice();

                return Float.compare(dealOnePrice, dealTwoPrice);
            }).doOnNext(abbreviatedDeals -> {
                listModels.clear();
                listModels.addAll(abbreviatedDeals);
            }).flatMap(Observable::from);
            final Observable<String> storeNameObservable = sortedDeals.flatMap(deal -> storesRepository.getStoreNameForID(deal.getStoreID()));

            return Observable.zip(sortedDeals, storeNameObservable, (deal, storeName) -> {
                float saving = (float) (deal.getSavingsFraction() * 100.0f);
                final String dealPrice = currencyHelper.getFormattedPrice((float) deal.getPrice());
                final String retailPrice = currencyHelper.getFormattedPrice((float) deal.getRetailPrice());
                final boolean hasSavings = deal.getSavingsFraction() > 0.0f;
                final boolean singlePriceMode = deal.getPrice() >= deal.getRetailPrice();
                final Drawable storeDrawable = storesRepository.getStoreIconDrawableForId(deal.getStoreID());

                return new GamesInfoDealsViewModel.Builder().setSinglePriceMode(singlePriceMode)
                                                            .setSavingPercentage(saving)
                                                            .setDealPrice(dealPrice)
                                                            .setStoreImage(storeDrawable)
                                                            .setRetailPrice(retailPrice)
                                                            .setHasSavings(hasSavings)
                                                            .setStoreName(storeName)
                                                            .setSavingPercentage(saving)
                                                            .build();
            }).toList();
        });
    }

    public String getUrlForIndex(Integer integer) {
        return dealRepository.loadDealUrl(listModels.get(integer).getDealID());
    }
}
