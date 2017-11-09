package com.rockspin.bargainbits.di.modules.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import com.rockspin.bargainbits.di.annotations.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    public @Provides Activity providesActivity() {
        return activity;
    }

    public @Provides @ActivityScope
    Context providesContext() {
        return activity;
    }

    public @Provides @ActivityScope
    LayoutInflater providesLayoutInflater() {
        return LayoutInflater.from(activity);
    }

    public @Provides @ActivityScope
    Resources providesResources() {
        return activity.getResources();
    }
}