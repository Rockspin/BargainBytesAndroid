package com.rockspin.bargainbits.utils.environment;

import android.content.Context;

import com.rockspin.bargainbits.utils.analytics.IAnalytics;

/**
 * services for the app.
 */
public interface IServices {

    /**
     * viewWillShow bugsense.
     *
     * @param pContext Context to use when starting services
     */
    void start(Context pContext);

    /**
     * viewWillShow fabric services.
     *
     * @param context android context.
     */
    void startFabric(Context context);

    /**
     * create a
     */
    IAnalytics getAnalytics();

    /**
     * Get an environment.
     *
     * @return an environment.
     */
    BBEnvironment getEnvironment();
}
