package com.rockspin.bargainbits.services;

import android.content.Context;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.utils.analytics.EventConstants;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;

/**
 * Production Analytics.
 */
public final class ProductionAnalytics implements IAnalytics {

//    private final Tracker mTracker;

    public ProductionAnalytics(Context context) {
//        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);

        // uncomment these lines to debug
        //analytics.setDryRun(true);
        //analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);

//        mTracker = analytics.newTracker(R.xml.app_tracker);

        // Enable Advertising
        //
        // Features.
//        mTracker.enableAdvertisingIdCollection(true);
    }

    @Override public void onGameClicked(String pGameName) {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_GAME_SEARCH)
//                                                    .setAction(EventConstants.EVENT_SEARCH_GAME_CLICKED)
//                                                    .setLabel(pGameName)
//                                                    .build());
    }

    @Override public void onStoreToggled(String storeID, String name, boolean activated) {
//        String action = activated ? EventConstants.EVENT_STORE_ACTIVATED : EventConstants.EVENT_STORE_DEACTIVATED;
//
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_STORE_FILTER)
//                                                    .setAction(action)
//                                                    .build());
    }

    @Override public void onAllStoreToggled(boolean pActive) {
//        String action = pActive ? EventConstants.EVENT_ALL_STORE_ACTIVATED : EventConstants.EVENT_ALL_STORE_DEACTIVATED;
//
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_STORE_FILTER)
//                                                    .setAction(action)
//                                                    .build());
    }

    @Override public void onStoreSliderToggled(boolean pOpen) {
//        String action = pOpen ? EventConstants.EVENT_STORE_SLIDER_OPENED : EventConstants.EVENT_STORE_SLIDER_CLOSED;
//
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_STORE_FILTER)
//                                                    .setAction(action)
//                                                    .build());
    }

    @Override public void onClickedStoresFilterOptions() {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_STORE_FILTER)
//                                                    .setAction(EventConstants.EVENT_CLICKED_ON_STORE_FILTER_OPTIONS)
//                                                    .build());
    }

    @Override public void onClickedCurrencyOptions() {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_CURRENCY)
//                                                    .setAction(EventConstants.EVENT_CLICKED_ON_CURRENCY_OPTIONS)
//                                                    .build());
    }

    @Override public void onSelectedCurrency(String pCurrencyID) {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_CURRENCY)
//                                                    .setAction(EventConstants.EVENT_SELECTED_CURRENCY)
//                                                    .setLabel(pCurrencyID)
//                                                    .build());
    }

    @Override public void onGameSearchedFor(String searchString) {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_GAME_SEARCH)
//                                                    .setAction(EventConstants.EVENT_SEARCH_FOR_GAME)
//                                                    .setLabel(searchString)
//                                                    .build());

//        Answers.getInstance()
//               .logSearch(new SearchEvent().putQuery(searchString));
    }

    @Override public void onBannerAdClicked() {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_ADS)
//                                                    .setAction(EventConstants.EVENT_CLICKED_ON_BANNER_AD)
//                                                    .build());
    }

    @Override public void onClickedShareAppOptions() {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_SHARE)
//                                                    .setAction(EventConstants.EVENT_CLICKED_ON_SHARE_APP_OPTIONS)
//                                                    .build());
    }

    @Override public void onSharedGame(String pGameName) {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_SHARE)
//                                                    .setAction(EventConstants.EVENT_SHARED_GAME)
//                                                    .setLabel(pGameName)
//                                                    .build());
    }

    @Override public void onGameAddedToWatchList(String pGameName, boolean pCustomPrice) {
//        mTracker.send(new HitBuilders.EventBuilder().setCategory(EventConstants.CATEGORY_WATCH_LIST)
//                                                    .setAction(EventConstants.EVENT_GAME_ADDED_TO_WATCH_LIST)
//                                                    .setLabel(pGameName)
//                                                    .set("CUSTOM_PRICE", pCustomPrice ? "YES" : "NO")
//                                                    .build());
    }
}
