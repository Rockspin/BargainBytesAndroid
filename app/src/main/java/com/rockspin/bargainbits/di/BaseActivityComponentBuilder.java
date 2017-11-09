package com.rockspin.bargainbits.di;

import android.app.Activity;
import com.rockspin.bargainbits.di.modules.activities.ActivityModule;
import dagger.android.AndroidInjector;

public abstract class BaseActivityComponentBuilder<T extends Activity> extends AndroidInjector.Builder<T> {

    public abstract BaseActivityComponentBuilder activityModule(ActivityModule myActivityModule);

    @Override
    public void seedInstance(T instance) {
        activityModule(new ActivityModule(instance));
    }
}
