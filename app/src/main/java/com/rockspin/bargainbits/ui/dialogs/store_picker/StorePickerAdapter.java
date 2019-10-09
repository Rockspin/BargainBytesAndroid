/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.dialogs.store_picker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rockspin.bargainbits.R;

import java.util.List;

import auto.parcel.AutoParcel;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;
import me.grantland.widget.AutofitHelper;

/**
 * List Adapter for displaying an array of store choices
 */
public class StorePickerAdapter extends RecyclerView.Adapter<StorePickerAdapter.StoreViewHolder> {

    private final List<StorePickerData> storeList;
    private final LayoutInflater layoutInflater;
    private PublishSubject<Integer> selectedDealIndex;

    StorePickerAdapter(final Context context, final List<StorePickerData> storeList, final PublishSubject<Integer> selectedDealIndex) {
        layoutInflater = LayoutInflater.from(context);
        this.storeList = storeList;
        this.selectedDealIndex = selectedDealIndex;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final StoreViewHolder holder = new StoreViewHolder(layoutInflater.inflate(R.layout.store_picker_item_view, parent, false));
        final AutofitHelper autofitHelper = AutofitHelper.create(holder.salePrice);
        autofitHelper.setMaxLines(1);
        autofitHelper.setMinTextSize(12);

        holder.itemView.setOnClickListener(v -> selectedDealIndex.onNext(holder.getAdapterPosition()));

        return holder;

    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        StorePickerData storePickerDataId = storeList.get(position);
        holder.storeName.setText(storePickerDataId.storeName());
        holder.storeImage.setImageDrawable(storePickerDataId.storeDrawable());
        holder.salePrice.setText(storePickerDataId.salePrice());
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.storeImage) ImageView storeImage;
        @Bind(R.id.storeName) TextView storeName;
        @Bind(R.id.salePrice) TextView salePrice;

        public StoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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