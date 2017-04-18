package com.rockspin.bargainbits.watch_list.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import javax.inject.Inject;
import javax.inject.Provider;

import timber.log.Timber;

public class WatchListCheckerJobCreator implements JobCreator {

    @Inject Provider<WatchListCheckJob> watchListCheckJobProvider;
    @Inject WatchListCheckerJobCreator() { /* not used */ }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case WatchListCheckJob.TAG:
                Timber.d("WatchListCheckJob created.");
                return watchListCheckJobProvider.get();
            default:
                return null;
        }
    }
}
