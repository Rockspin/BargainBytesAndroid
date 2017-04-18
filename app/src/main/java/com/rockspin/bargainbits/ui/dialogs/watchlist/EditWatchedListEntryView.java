/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.dialogs.watchlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxSeekBar;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Represents a view containing the various options available when modifying a watch list item.
 */
public class EditWatchedListEntryView extends LinearLayout implements EditWatchListEntryPresenter.IView, EditWatchListEntryDialogFragment.IView {

    private final Observable<Integer> selectedPrice;

    @Bind(R.id.watch_type_radio_group) RadioGroup mWatchTypeRadioGroup;
    @Bind(R.id.price_picker_title) TextView mPricePickerTitleTV;
    @Bind(R.id.price_picker_description) TextSwitcher mPricePickerDescriptionTV;
    @Bind(R.id.price_number_picker) SeekBar seekBar;

    private EditWatchListEntryPresenter presenter;

    private final BehaviorSubject<Boolean> onDialogDimissed = BehaviorSubject.create();
    private final Observable<EditWatchListEntryPresenter.WatchedState> radioGroupObservable;

    public EditWatchedListEntryView(Context context) {
        this(context, null);
    }

    public EditWatchedListEntryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditWatchedListEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.watched_item_options_view, this);
        ButterKnife.bind(this);

        mPricePickerDescriptionTV.setFactory(() -> new TextView(getContext()));

        AlphaAnimation inAnimation = new AlphaAnimation(0, 1);
        inAnimation.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        AlphaAnimation outAnimation = new AlphaAnimation(1, 0);
        outAnimation.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));

        mPricePickerDescriptionTV.setInAnimation(inAnimation);
        mPricePickerDescriptionTV.setOutAnimation(outAnimation);
        radioGroupObservable = RxRadioGroup.checkedChanges(mWatchTypeRadioGroup)
                                           .map(this::getSaleStateFromButtonId);

        selectedPrice = RxSeekBar.userChanges(seekBar);
    }

    @Override
    public void setPresenter(EditWatchListEntryPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull private EditWatchListEntryPresenter.WatchedState getSaleStateFromButtonId(Integer buttonId) {
        if (buttonId == R.id.radio_any_sale) {
            return EditWatchListEntryPresenter.WatchedState.ANY;
        } else if(buttonId == R.id.radio_custom_price){
            return EditWatchListEntryPresenter.WatchedState.PRICE;
        }
        throw new IllegalStateException("unknown button Id");
    }

    @Override public void start(WatchedItem itemToEditOrRemove) {
        presenter.start(itemToEditOrRemove, this);
    }

    @Override public void stop() {
        presenter.stop();
    }

    @Override public boolean isItemInWatchList() {
        return presenter.isInWatchList();
    }

    @Override public void onDismissed(boolean dismissed) {
        onDialogDimissed.onNext(dismissed);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPricePickerTitleTV.animate()
                           .setListener(null);
    }

    @Override public Observable<EditWatchListEntryPresenter.WatchedState> onWatchStateChanged() {
        return radioGroupObservable;
    }

    @RxLogObservable
    @Override public Observable<Integer> onSelectedPercentagePrice() {
        return selectedPrice;
    }

    @Override public void setSeekbarProgress(int currentValue) {
        seekBar.setProgress(currentValue);
    }

    @Override public void setGameName(String gameName) {
        mPricePickerTitleTV.setText(gameName);
    }

    @Override public void showAnySaleText() {
        mPricePickerDescriptionTV.setText(getContext().getString(R.string.track_game_at_any_price));
    }

    @Override public void showCustomSaleText(String activeCurrencySymbol, float price) {
        final String priceFormatted = "<b>" + activeCurrencySymbol+ " " + Math.round(price) + "</b>";
        mPricePickerDescriptionTV.setText(Html.fromHtml(getContext().getString(R.string.set_price_to_track_game_at, priceFormatted)));
    }

    @Override public void setSelectionIntervals(int maxPercentage) {
        seekBar.setMax(maxPercentage);
    }

    @Override public void showWatchStateSelected(EditWatchListEntryPresenter.WatchedState watchedState) {
        switch (watchedState){
            case ANY:
                mWatchTypeRadioGroup.check(R.id.radio_any_sale);
                seekBar.setVisibility(View.GONE);
                break;
            case PRICE:
                mWatchTypeRadioGroup.check(R.id.radio_custom_price);
                seekBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public Observable<Boolean> onDialogDismissObservable() {
        return onDialogDimissed;
    }
}