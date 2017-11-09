package com.rockspin.bargainbits.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fernandocejas.arrow.checks.Preconditions;
import com.jakewharton.rxbinding.view.RxView;
import com.rockspin.bargainbits.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Layouts out views with even spacing if no space adds an overflow menu. Probably way too complicated.
 */
public class OptionsLayout extends LinearLayout {

    public static class Option {
        private final int drawableResource;

        public Option(final int drawableResource) {
            this.drawableResource = drawableResource;
        }
    }

    private Observable<Integer> mClickOptionObservable;

    public OptionsLayout(final Context context) {
        this(context, null);
    }

    public OptionsLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OptionsLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) public OptionsLayout(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOptions(final List<Option> options) {
        int index = 0;
        List<Observable<Integer>> observables = new ArrayList<>(options.size());
        for (final Option option : options) {
            final int buttonIndex = index;
            final ImageButton btn = new ImageButton(getContext());
            btn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.deal_selector));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            btn.setLayoutParams(params);
            btn.setImageResource(option.drawableResource);
            btn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            observables.add(RxView.clicks(btn).map(o -> buttonIndex));
            addView(btn);
            ++index;
        }

        mClickOptionObservable = Observable.merge(observables);
    }

    @NonNull public Observable<Integer> getClickOptionObservable() {
        Preconditions.checkNotNull(mClickOptionObservable, "must call set options before we can listen for clicks");
        return mClickOptionObservable;
    }

    public void toggleVisibility() {
        setVisibility(getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
    }
}