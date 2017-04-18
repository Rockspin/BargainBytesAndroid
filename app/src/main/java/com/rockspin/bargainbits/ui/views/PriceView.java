/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rockspin.bargainbits.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Represents a layout holding a price and an optional discount price.
 */
public class PriceView extends LinearLayout {

    @Bind(R.id.retailPrice) TextView mRetailPrice;
    @Bind(R.id.salePrice) TextView mSalePrice;

    public PriceView(final Context context) {
        super(context);
        init(context);
    }

    public PriceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);

        final TypedArray a = context.getTheme()
                                    .obtainStyledAttributes(attrs, R.styleable.PriceView, 0, 0);

        try {
            final boolean singlePriceMode = a.getBoolean(R.styleable.PriceView_singlePrice, false);
            if (singlePriceMode) {
                setSinglePriceMode();
            }
        } finally {
            a.recycle();
        }
    }

    private void init(final Context context) {
        final View rootView = LayoutInflater.from(context)
                                            .inflate(R.layout.price_view, this, true);
        ButterKnife.bind(this, rootView);

        mRetailPrice.setPaintFlags(mRetailPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void setRetailPriceText(final String price) {
        mRetailPrice.setText(price);
        mRetailPrice.setVisibility(View.VISIBLE);
    }

    public void setSalePriceText(final String price) {
        mSalePrice.setText(price);
    }

    public void setSinglePriceMode() {
        mRetailPrice.setVisibility(View.GONE);
    }
}
