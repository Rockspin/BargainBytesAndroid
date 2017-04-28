/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.activities.main.storesdrawer.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.rockspin.apputils.di.annotations.ActivityScope;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.cheapshark.StoresData;
import java.util.ArrayList;
import javax.inject.Inject;
import rx.subjects.PublishSubject;

/**
 * List adapter for displaying an array of Store objects.
 */
public final class StoreDrawerListAdapter extends ArrayAdapter<StoreEnabled> {

    private final PublishSubject<StoreEnabled>  storePositionPublishSubject = PublishSubject.create();
    private final LayoutInflater inflater;
    private boolean allStoresChecked;

    @Inject public StoreDrawerListAdapter(@ActivityScope LayoutInflater inflater, @ActivityScope Context context) {
        super(context, 0, new ArrayList<>());
        this.inflater = inflater;
    }

    @Override public boolean hasStableIds() {
        return true;
    }

    @Override public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.store_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StoreEnabled storePosition = getItem(position);

        holder.storeImage.setImageDrawable(StoresData.getStoreIconDrawableForId(storePosition.store.getStoreId(), convertView.getContext()));

        if (!allStoresChecked) {
            holder.storeSwitch.setVisibility(View.VISIBLE);
            holder.storeName.setVisibility(View.GONE);
            final boolean storeIdState = storePosition.checked;
            holder.storeSwitch.setOnCheckedChangeListener(null);
            holder.storeSwitch.setText(storePosition.store.getStoreName());

            // We have to surround the set checked with remove/add view otherwise it would appear to animate
            // when it enters the view
            final ViewGroup switchParent = (ViewGroup) convertView;
            switchParent.removeView(holder.storeSwitch);
            holder.storeSwitch.setChecked(storeIdState);
            switchParent.addView(holder.storeSwitch);

            holder.storeSwitch.setOnCheckedChangeListener((compoundButton, checked) ->{
                storePosition.setChecked(checked);
                storePositionPublishSubject.onNext(storePosition);
            });
        } else {
            holder.storeSwitch.setVisibility(View.GONE);
            holder.storeName.setText(storePosition.store.getStoreName());
            holder.storeName.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override public long getItemId(int position) {
        return getItem(position).store.getStoreId().hashCode();
    }

    public PublishSubject<StoreEnabled> onStoreEnabled() {
        return storePositionPublishSubject;
    }

    public void setAllStoresChecked(Boolean allStoresChecked) {
        this.allStoresChecked = allStoresChecked;
    }

    public static class ViewHolder {
        @Bind(R.id.storeImage) ImageView storeImage;
        @Bind(R.id.storeTitle) TextView storeName;
        @Bind(R.id.storeSwitch) SwitchCompat storeSwitch;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
