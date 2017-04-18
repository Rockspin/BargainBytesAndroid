package com.rockspin.bargainbits.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class SchedulersModule {
    public static final String MAIN = "MAIN";
    public static final String IO = "IO";

    @Provides @Named(MAIN) Scheduler main() {
        return AndroidSchedulers.mainThread();
    }

    @Provides @Named(IO) Scheduler io() {
        return Schedulers.io();
    }

}
