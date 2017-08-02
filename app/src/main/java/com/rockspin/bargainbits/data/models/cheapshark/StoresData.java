package com.rockspin.bargainbits.data.models.cheapshark;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.support.v4.content.ContextCompat;
import com.rockspin.bargainbits.R;

import com.rockspin.bargainbits.data.models.Store;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class StoresData {
    private final Set<Store> mStoreSet = new LinkedHashSet<>();
    private final HashMap<String, String> mStoreNameMap = new HashMap<>();

    public StoresData(final List<Store> pStoresList) {
        mStoreSet.addAll(pStoresList);
        mStoreNameMap.clear();
        for (final Store store : pStoresList) {
            mStoreNameMap.put(store.getStoreId(), store.getStoreName());
        }
    }

    /**
     * Retrieves the proper store drawable for an ID
     *
     * @param id Store id.
     * @return string.
     */
    public static Drawable getStoreIconDrawableForId(final String id, final Context context) {
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
            // Sila Games
            case 18:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_silagames);
            default:
                return ContextCompat.getDrawable(context, R.drawable.ic_store_generic);
        }
    }
}