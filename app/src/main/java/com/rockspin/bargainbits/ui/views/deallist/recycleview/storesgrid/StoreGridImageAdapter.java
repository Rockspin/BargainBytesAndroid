/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.views.deallist.recycleview.storesgrid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.rockspin.bargainbits.data.models.cheapshark.StoresData;

import java.util.List;

/**
 * Adapter for store grid view.
 */
public class StoreGridImageAdapter extends ArrayAdapter<StoreEntryViewModel> {

    private int mItemLimit = -1;

    public StoreGridImageAdapter(final Context context, final List<StoreEntryViewModel> viewModels) {
        super(context, 0, viewModels);
    }

    public void setItemLimit(int maxItems) {
        mItemLimit = maxItems;
    }

    @Override public int getCount() {
        if (mItemLimit != -1 && mItemLimit < super.getCount()) {
            return mItemLimit;
        } else {
            return super.getCount();
        }
    }

    @Override public View getView(final int position, final View convertView, final ViewGroup parent) {
        final ImageView storeImageView;
        if (convertView == null) {
            storeImageView = new ImageView(getContext());
            storeImageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            storeImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            storeImageView.setAdjustViewBounds(true);
            storeImageView.setClickable(false);
        } else {
            storeImageView = (ImageView) convertView;
        }

        final StoreEntryViewModel storeEntryViewModel = getItem(position);
        final String storedId = storeEntryViewModel.getStoreId();
        storeImageView.setImageDrawable(StoresData.getStoreIconDrawableForId(storedId, getContext()));

        final float alpha = storeEntryViewModel.isActive() ? 1.0f : 0.3f;
        storeImageView.setAlpha(alpha);

        return storeImageView;
    }
}
