/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits

import android.app.Activity
import android.app.Application
import com.evernote.android.job.JobManager
import com.rockspin.apputils.di.modules.application.ApplicationModule
import com.rockspin.bargainbits.di.components.DaggerApplicationComponent
import com.rockspin.bargainbits.utils.environment.IServices
import com.rockspin.bargainbits.watch_list.job.WatchListCheckJob
import com.rockspin.bargainbits.watch_list.job.WatchListCheckerJobCreator
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BargainBytesApp : Application(), HasActivityInjector {

    @Inject lateinit internal var iServices: IServices

    @Inject lateinit internal var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject lateinit internal var watchListCheckerJobCreator: WatchListCheckerJobCreator

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build().inject(this)

        iServices.start(this)

        // Set up timber utils.
        TimberUtils.start()

        JobManager.create(this).addJobCreator(watchListCheckerJobCreator)
        WatchListCheckJob.schedule()
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}
