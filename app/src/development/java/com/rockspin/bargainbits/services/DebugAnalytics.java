package com.rockspin.bargainbits.services;

import android.util.Log;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;

/**
 * Production Analytics.
 */
public final class DebugAnalytics implements IAnalytics {

    private static final String TAG = "DebugAnalytics";

    @Override public void onGameClicked(final String pGameName) {
        Log.d(TAG, "onGameClicked");
    }

    @Override public void onStoreToggled(final String pStoreID, final String pName, final boolean pActive) {
        Log.d(TAG, "saveStoreEnabledState");
    }

    @Override public void onAllStoreToggled(final boolean pActive) {
        Log.d(TAG, "onAllStoreToggled");
    }

    @Override public void onStoreSliderToggled(final boolean pOpen) {
        Log.d(TAG, "sendDrawerToggledAnalytic");
    }

    @Override public void onClickedStoresFilterOptions() {
        Log.d(TAG, "onClickedStoresFilterOptions");
    }

    @Override public void onClickedCurrencyOptions() {
        Log.d(TAG, "onClickedCurrencyOptions");
    }

    @Override public void onSelectedCurrency(final String pCurrencyID) {
        Log.d(TAG, "onSelectedCurrency");
    }

    @Override public void onGameSearchedFor(final String searchString) {
        Log.d(TAG, "onGameSearchedFor");
    }

    @Override public void onBannerAdClicked() {
        Log.d(TAG, "onBannerAdClicked");
    }

    @Override public void onClickedShareAppOptions() {
        Log.d(TAG, "onClickedShareAppOptions");
    }

    @Override public void onSharedGame(String pGameName) {
        Log.d(TAG, "onSharedGame");
    }

    @Override public void onGameAddedToWatchList(String pGameName, boolean pCustomPrice) {
        Log.d(TAG, "onGameAddedToWatchList");
    }
}
