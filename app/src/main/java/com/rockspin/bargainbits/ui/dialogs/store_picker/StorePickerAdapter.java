/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.dialogs.store_picker;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.di.annotations.ActivityScope;

import java.util.ArrayList;

import javax.inject.Inject;

import auto.parcel.AutoParcel;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitHelper;

import static com.rockspin.bargainbits.utils.OptionalUtils.or;

/**
 * List Adapter for displaying an array of store choices
 */
public class StorePickerAdapter extends ArrayAdapter<StorePickerAdapter.StorePickerData> {
    @Inject @ActivityScope LayoutInflater layoutInflater;

    @Inject public StorePickerAdapter(final Activity context) {
        super(context, 0, new ArrayList<>());
    }

    @Override public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View rootView = or(Optional.fromNullable(convertView), () -> layoutInflater.inflate(R.layout.store_picker_item_view, parent, false));

        final ViewHolder holder = or(Optional.fromNullable((ViewHolder) rootView.getTag()), () -> {
                                              final ViewHolder viewHolder = new ViewHolder(rootView);

                                              final AutofitHelper autofitHelper = AutofitHelper.create(viewHolder.salePrice);
                                              autofitHelper.setMaxLines(1);
                                              autofitHelper.setMinTextSize(12);

                                              rootView.setTag(viewHolder);
                                              return viewHolder;
                                          });

        StorePickerData storePickerDataId = getItem(position);
        holder.storeName.setText(storePickerDataId.storeName());
        holder.storeImage.setImageDrawable(storePickerDataId.storeDrawable());
        holder.salePrice.setText(storePickerDataId.salePrice());
        return rootView;
    }

    public static class ViewHolder {
        @BindView(R.id.storeImage) ImageView storeImage;
        @BindView(R.id.storeName) TextView storeName;
        @BindView(R.id.salePrice) TextView salePrice;

        public ViewHolder(View view) {
            super();
            ButterKnife.bind(this, view);
        }
    }

    @AutoParcel public static abstract class StorePickerData implements Parcelable {
        abstract String storeName();

        abstract Drawable storeDrawable();

        abstract String salePrice();

        public static StorePickerData create(String storeName, Drawable storeDrawable, String salePrice) {
            return new AutoParcel_StorePickerAdapter_StorePickerData(storeName, storeDrawable, salePrice);
        }
    }
}