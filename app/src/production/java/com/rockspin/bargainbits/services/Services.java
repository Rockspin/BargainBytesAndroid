/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.services;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.utils.environment.BBEnvironment;
import com.rockspin.bargainbits.utils.environment.IServices;
import io.fabric.sdk.android.Fabric;

/**
 * Conditionally starts analytics.
 */
public final class Services implements IServices {

    private ProductionAnalytics mAnalytics;

    /**
     * Conditionally starts services that the app uses.
     */
    public Services() {
    }

    @Override public void start(Context pContext) {
        startFabric(pContext);
        mAnalytics = new ProductionAnalytics(pContext);
    }

    @Override public void startFabric(Context context) {
        Fabric.with(context, new Crashlytics());
    }

    @Override public IAnalytics getAnalytics() {
        return mAnalytics;
    }

    @Override public BBEnvironment getEnvironment() {
        return BBEnvironment.PRODUCTION;
    }
}
