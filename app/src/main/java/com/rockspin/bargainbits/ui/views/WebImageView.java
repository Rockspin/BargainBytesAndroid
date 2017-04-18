/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.rockspin.bargainbits.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Represents a layout holding an image view that loads
 * a web image and a progress bar to show while loading.
 */
public final class WebImageView extends RelativeLayout {

    @Bind(R.id.imageView) ImageView mImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    /**
     * Represents a layout holding an image view that loads
     * a web image and a progress bar to show while loading.
     *
     * @param context andorid context.
     */
    public WebImageView(final Context context) {
        this(context, null);
    }

    /**
     * Represents a layout holding an image view that loads
     * a web image and a progress bar to show while loading.
     *
     * @param context andorid context.
     * @param attrs android attribs.
     */
    public WebImageView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Represents a layout holding an image view that loads
     * a web image and a progress bar to show while loading.
     *
     * @param context andorid context.
     * @param attrs android attribs.
     * @param defStyle android style.
     */
    public WebImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        final View rootView = LayoutInflater.from(context).inflate(R.layout.web_image_view, this, true);
        ButterKnife.bind(this, rootView);
    }

    /**
     * Load an image from a URL.
     *
     * @param url the url to load an image from.
     */
    public void loadImageFromUrl(final String url) {
        Glide.with(getContext()).load(url).fitCenter().dontAnimate().placeholder(R.drawable.ic_game_image_placeholder).into(new GlideDrawableImageViewTarget(mImageView) {

            @Override public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void clear() {
        Glide.clear(mImageView);
    }
}
