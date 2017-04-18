/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Global Glide Image loader module.
 */
public class GlobalGlideModule implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        int diskCacheSizeInBytes = 50000000; // 50 mb
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeInBytes));
    }

    @Override public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}
