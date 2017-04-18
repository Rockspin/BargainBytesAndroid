package com.rockspin.bargainbits.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import com.rockspin.apputils.di.annotations.ApplicationScope;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.cheapshark.Store;
import com.rockspin.bargainbits.data.rest_client.ICheapsharkAPIService;
import com.rockspin.bargainbits.utils.Constants;
import com.rockspin.bargainbits.utils.RxSuccessCache;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.subjects.BehaviorSubject;

@Singleton
public class StoresRepository {

    private final RxSuccessCache<List<Store>> stores = RxSuccessCache.create();
    private final BehaviorSubject<Boolean> useAllStoresSubject;
    private final SharedPreferences mPreferences;

    @Inject @ApplicationScope Context context;
    @Inject ICheapsharkAPIService iCheapsharkAPIService;

    @Inject StoresRepository(SharedPreferences preferences) {
        mPreferences = preferences;
        useAllStoresSubject = BehaviorSubject.create(getUseAllStoresState());
    }

    public Observable<List<Store>> getStores() {
        return stores.get(() -> iCheapsharkAPIService.getStores());
    }

    private Observable<Map<String, Store>> getStoresMap() {
        return stores.get(() -> iCheapsharkAPIService.getStores())
                     .flatMap(Observable::from)
                     .toMap(Store::getStoreID);
    }

    public Observable<Store> getStoreForId(String storeID) {
        return getStoresMap().map(stores -> stores.get(storeID));
    }

    public Observable<String> getStoreNameForID(String storeID) {
        return getStoresMap().map(stores -> stores.get(storeID))
                             .map(Store::getStoreName);
    }

    /**
     * @return an observable that emits every time the filter string changes.
     */
    public Observable<String> onSelectedStoresChanged() {
        return useAllStoresSubject.switchMap(useAllstores -> useAllstores ? Observable.just("") : getEnabledStoreString());
    }

    private Observable<String> getEnabledStoreString() {
        return getStoresMap().map(Map::keySet)
                             .flatMap(Observable::from)
                             .filter(storeID -> mPreferences.getBoolean(Constants.SP_STORE_ID_PREFIX + storeID, false))
                             .reduce(new StringBuilder(), (stringBuilder, value) -> stringBuilder.length() == 0 ? stringBuilder.append(value) : stringBuilder.append(",")
                                                                                                                                                             .append(value))
                             .map(StringBuilder::toString);
    }

    public void setUseAllStoresState(final boolean state) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(Constants.SP_STORES_USE_ALL_STORES, state);
        editor.apply();
        checkState();
    }

    public void setStoreIdState(final String storeId, final boolean state) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(Constants.SP_STORE_ID_PREFIX + storeId, state);
        editor.apply();
        checkState();
    }

    public boolean isStoreEnabled(final String storeId) {
        return mPreferences.getBoolean(Constants.SP_STORE_ID_PREFIX + storeId, false);
    }

    public boolean getUseAllStoresState() {
        return mPreferences.getBoolean(Constants.SP_STORES_USE_ALL_STORES, true);
    }

    private void checkState() {
        useAllStoresSubject.onNext(getUseAllStoresState());
    }

    /**
     * Retrieves the proper store drawable for an ID
     *
     * @param id Store id.
     * @return string.
     */
    public Drawable getStoreIconDrawableForId(final String id) {
        final int idInt = Integer.valueOf(id);
        switch (idInt) {
            // Steam
            case 1:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_steam);
            // Gamer's Gate
            case 2:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_gamersgate);
            // GreenManGaming
            case 3:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_gmg);
            // Amazon
            case 4:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_amazon);
            // GameStop
            case 5:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_gamestop);
            // Direct2Drive
            case 6:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_direct2drive);
            // GoG
            case 7:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_gog);
            // Origin
            case 8:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_origin);
            // Get Games
            case 9:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_getgames);
            // Shiny Loot
            case 10:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_shinyloot);
            // Humble Store
            case 11:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_hum);
            // Desura
            case 12:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_desura);
            // Uplay
            case 13:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_uplay);
            // IndieGameStand
            case 14:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_indie);
            // Bundle Stars
            case 15:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_bundle_stars);
            // Games rocket
            case 16:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_games_rocket);
            // Games republic
            case 17:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_game_repub);
            default:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_generic);
        }
    }
}
