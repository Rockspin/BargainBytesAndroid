/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rockspin.apputils.di.annotations.ActivityScope;
import com.rockspin.bargainbits.BargainBytesApp;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.repository.CurrencyRepository;
import com.rockspin.bargainbits.data.models.currency.CurrencyNamesAndISOCodes;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;

import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

/**
 * Displays a list of currencies to the user to pick from.
 */
public final class CurrencyPickerDialogFragment extends DialogFragment {

    private static final String CURRENCY_OBJECT = "CURRENCY_OBJECT";
    private CurrencyNamesAndISOCodes mCurrencyNamesAndISOCodes;
    @Inject CurrencyRepository currencyRepository;
    @Inject IAnalytics iAnalytics;
    @Inject @ActivityScope Resources resources;

    public CurrencyPickerDialogFragment() { /* not used */ }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @NonNull @Override public MaterialDialog onCreateDialog(final Bundle savedInstanceState) {
        mCurrencyNamesAndISOCodes = (CurrencyNamesAndISOCodes) getArguments().getSerializable(CURRENCY_OBJECT);

        // TODO - fix dialog

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.choose_currency_dialog_title)
               .dividerColorRes(R.color.primary_color)
               .titleColorRes(R.color.primary_color)
               .negativeText(R.string.cancel)               // Optional.
               .negativeColorRes(R.color.primary_color);
               /*.itemColorRes(R.color.primary_text_color)
               .items(mCurrencyNamesAndISOCodes.currencyList)
               .itemsCallback((dialog, view, which, text) -> {
                   final String currencyCodePicked = mCurrencyNamesAndISOCodes.isoList[which];
                   currencyRepository.setCurrencyIsoCode(currencyCodePicked);
                   iAnalytics.onSelectedCurrency(currencyCodePicked);
               });*/
        return builder.build();
    }

    public static CurrencyPickerDialogFragment instantiate(CurrencyNamesAndISOCodes currencyNamesAndISOCodes) {
        CurrencyPickerDialogFragment currencyPickerDialogFragment = new CurrencyPickerDialogFragment();

        final Bundle bundle = new Bundle();
        bundle.putSerializable(CurrencyPickerDialogFragment.CURRENCY_OBJECT, currencyNamesAndISOCodes);
        currencyPickerDialogFragment.setArguments(bundle);
        return currencyPickerDialogFragment;
    }
}
