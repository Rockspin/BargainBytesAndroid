/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.evernote.android.job.JobManager;
import com.rockspin.apputils.di.modules.application.ApplicationModule;
import com.rockspin.bargainbits.di.components.DaggerApplicationComponent;
import com.rockspin.bargainbits.utils.environment.IServices;
import com.rockspin.bargainbits.watch_list.job.WatchListCheckJob;
import com.rockspin.bargainbits.watch_list.job.WatchListCheckerJobCreator;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;
import javax.inject.Inject;

public class BargainBytesApp extends Application implements HasDispatchingActivityInjector {

    @Inject IServices iServices;

    @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject WatchListCheckerJobCreator watchListCheckerJobCreator;

    @Override public final void onCreate() {
        super.onCreate();

        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build().inject(this);

        // viewWillShow services associated with app
        iServices.start(this);

        // Set up timber utils.
        TimberUtils.start();

        JobManager.create(this).addJobCreator(watchListCheckerJobCreator);
        WatchListCheckJob.schedule();
    }

    /**
     * With dagger this shouldn't be necessary
     */
    @Deprecated
    public static BargainBytesApp from(Context context) {
        if (context instanceof BargainBytesApp) {
            return (BargainBytesApp) context;
        } else {
            return (BargainBytesApp) context.getApplicationContext();
        }
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
