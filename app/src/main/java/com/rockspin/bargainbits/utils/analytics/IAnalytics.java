package com.rockspin.bargainbits.utils.analytics;

/**
 * Analytics platform.
 */
public interface IAnalytics {

    /**
     * on a particular game clicked.
     *
     * @param pGameName name of the game.
     */
    void onGameClicked(String pGameName);

    /**
     * a store has been manually toggled on or off.
     *
     * @param pStoreID store ID.
     * @param pName name of the store activated.
     * @param pActive if store was turned manually on or off
     */
    void onStoreToggled(String pStoreID, String pName, boolean pActive);

    /**
     * on all store toggled.
     *
     * @param pActive If the store is active or inactive.
     */
    void onAllStoreToggled(boolean pActive);

    /**
     * if the stores slider is opened or closed.
     *
     * @param pOpen is the store slider is open or closed.
     */
    void onStoreSliderToggled(boolean pOpen);

    /**
     * Clicked on the stores file in the options menu.
     */
    void onClickedStoresFilterOptions();

    /**
     * Clicked on the currency in the options menu.
     */
    void onClickedCurrencyOptions();

    /**
     * Called when a particular currency is selected.
     *
     * @param pCurrencyID a particular currency that was selected.
     */
    void onSelectedCurrency(String pCurrencyID);

    /**
     * called when a particular game is searched for.
     *
     * @param pSearchString the game that is searched for.
     */
    void onGameSearchedFor(String pSearchString);

    /**
     * Called when a banner ad was clicked on.
     */
    void onBannerAdClicked();

    /**
     * Clicked on the shareDeal app in the options menu.
     */
    void onClickedShareAppOptions();

    /**
     * Called when a game is shared.
     *
     * @param pGameName Name of the game that was shared.
     */
    void onSharedGame(String pGameName);

    /**
     * Called when a game is added to watch list
     *
     * @param pGameName Name of the game that was added.
     * @param pCustomPrice If game is being watched at a custom price.
     */
    void onGameAddedToWatchList(String pGameName, boolean pCustomPrice);
}
