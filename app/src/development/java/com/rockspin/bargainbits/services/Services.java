package com.rockspin.bargainbits.services;

import android.content.Context;
import android.util.Log;
import com.rockspin.bargainbits.utils.analytics.IAnalytics;
import com.rockspin.bargainbits.utils.environment.BBEnvironment;
import com.rockspin.bargainbits.utils.environment.IServices;

/**
 * Conditionally starts analytics.
 */
public final class Services implements IServices {

    DebugAnalytics mAnalytics;

    /**
     * conditionally viewWillShow and viewWillHide services.
     */
    public Services() {
    }

    private static final String TAG = Services.class.getSimpleName();

    @Override public void start(final Context pContext) {
        startFabric(pContext);
        mAnalytics = new DebugAnalytics();
    }

    @Override public void startFabric(final Context context) {
        Log.d(TAG, "We are in development mode so don't viewWillShow fabric services");
    }

    @Override public IAnalytics getAnalytics() {
        return mAnalytics;
    }

    @Override public BBEnvironment getEnvironment() {
        return BBEnvironment.DEBUG;
    }
}
