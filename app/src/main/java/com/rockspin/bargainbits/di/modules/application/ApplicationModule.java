package com.rockspin.bargainbits.di.modules.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.rockspin.bargainbits.di.annotations.ApplicationScope;
import dagger.Module;
import dagger.Provides;

import static com.rockspin.bargainbits.Preconditions.checkNotNull;

@Module public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Application context) {
        this.context = checkNotNull(context, "Context cannot be null");
    }

    @Provides @ApplicationScope
    Application providesApplication() {
        return (Application) context;
    }

    @Provides @ApplicationScope
    Context providesContext() {
        return context;
    }

    @Provides
    SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
