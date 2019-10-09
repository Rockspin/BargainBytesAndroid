/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.dialogs.store_picker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rockspin.bargainbits.R;
import dagger.android.support.AndroidSupportInjection;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Displays a list of stores to choose from for a deal.
 */
public final class StorePickerDialogFragment extends DialogFragment {
    private static final String LIST_KEY = "LIST_KEY";
    private final PublishSubject<Integer> dealPublishSubject = PublishSubject.create();
    private StorePickerAdapter storePickerAdapter;

    public static StorePickerDialogFragment instantiate(List<StorePickerAdapter.StorePickerData> deals){
        StorePickerDialogFragment storePickerDialogFragment = new StorePickerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LIST_KEY, new ArrayList<>(deals));
        storePickerDialogFragment.setArguments(bundle);
        return storePickerDialogFragment;
    }

    @Override public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        ArrayList<StorePickerAdapter.StorePickerData> parcelableList = getArguments().getParcelableArrayList(LIST_KEY);
        storePickerAdapter = new StorePickerAdapter(context, parcelableList, dealPublishSubject);
    }

    @Override @NonNull public MaterialDialog onCreateDialog(final Bundle savedInstanceState) {
        // TODO - fix adapter here
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.choose_store_dialog_title)
               .titleColorRes(R.color.primary_color)
               .dividerColorRes(R.color.primary_color)
               .adapter(storePickerAdapter, new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false))
               .negativeText(R.string.cancel)
               .negativeColorRes(R.color.primary_color);

        return builder.build();
    }

    public Observable<Integer> onDealSelected() {
        return dealPublishSubject;
    }

}
