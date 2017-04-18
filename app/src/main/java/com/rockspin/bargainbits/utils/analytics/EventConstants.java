package com.rockspin.bargainbits.utils.analytics;

/**
 * events that are sent from the eventing system
 */
public final class EventConstants {
    // analytics categories
    public static final String CATEGORY_GAME_SEARCH = "CATEGORY_GAME_SEARCH";
    public static final String CATEGORY_STORE_FILTER = "CATEGORY_STORE_FILTER";
    public static final String CATEGORY_CURRENCY = "CATEGORY_CURRENCY";
    public static final String CATEGORY_ADS = "CATEGORY_ADS";
    public static final String CATEGORY_SHARE = "CATEGORY_SHARE";
    public static final String CATEGORY_WATCH_LIST = "CATEGORY_WATCH_LIST";

    // store slider events.
    public static final String EVENT_STORE_SLIDER_OPENED = "EVENT_STORE_SLIDER_OPENED";
    public static final String EVENT_STORE_SLIDER_CLOSED = "EVENT_STORE_SLIDER_CLOSED";

    // all store events.
    public static final String EVENT_ALL_STORE_ACTIVATED = "EVENT_ALL_STORE_ACTIVATED";
    public static final String EVENT_ALL_STORE_DEACTIVATED = "EVENT_ALL_STORE_DEACTIVATED";
    public static final String EVENT_STORE_ACTIVATED = "EVENT_STORE_ACTIVATED";
    public static final String EVENT_STORE_DEACTIVATED = "EVENT_STORE_DEACTIVATED";

    // options menu events.
    public static final String EVENT_CLICKED_ON_STORE_FILTER_OPTIONS = "EVENT_CLICKED_ON_STORE_FILTER_OPTIONS";
    public static final String EVENT_CLICKED_ON_CURRENCY_OPTIONS = "EVENT_CLICKED_ON_CURRENCY_OPTIONS";

    // currency events.
    public static final String EVENT_SELECTED_CURRENCY = "EVENT_SELECTED_CURRENCY";

    // search events.
    public static final String EVENT_SEARCH_FOR_GAME = "EVENT_SEARCH_FOR_GAME";
    public static final String EVENT_SEARCH_GAME_CLICKED = "EVENT_SEARCH_GAME_CLICKED";

    // ad events
    public static final String EVENT_CLICKED_ON_BANNER_AD = "EVENT_CLICKED_ON_BANNER_AD";

    // shareDeal events
    public static final String EVENT_CLICKED_ON_SHARE_APP_OPTIONS = "EVENT_CLICKED_ON_SHARE_APP_OPTIONS";
    public static final String EVENT_SHARED_GAME = "EVENT_SHARED_GAME";

    // watch list
    public static final String EVENT_GAME_ADDED_TO_WATCH_LIST = "EVENT_GAME_ADDED_TO_WATCH_LIST";
}
