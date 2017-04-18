package com.rockspin.bargainbits.di.modules;

import android.app.Activity;
import com.rockspin.apputils.di.BaseActivityComponentBuilder;
import com.rockspin.apputils.di.modules.activities.ActivityModule;
import com.rockspin.bargainbits.ui.activities.WatchListActivity;
import com.rockspin.bargainbits.ui.activities.main.MainActivity;
import com.rockspin.bargainbits.ui.activities.search.GamesSearchActivity;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
    AndroidActivityModule.GamesSearchActivityComponent.class,
    AndroidActivityModule.WatchListActivityComponent.class,
    AndroidActivityModule.MainActivityComponent.class
})
public abstract class AndroidActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(GamesSearchActivity.class)
    public abstract AndroidInjector.Factory<? extends Activity>
    bindGamesSearchActivityInjectorFactory(GamesSearchActivityComponent.Builder builder);

    @Subcomponent(modules = {
        ActivityModule.class,
        AndroidFragmentModule.class
    })
    public interface GamesSearchActivityComponent extends AndroidInjector<GamesSearchActivity> {

        @Subcomponent.Builder
        abstract class Builder extends BaseActivityComponentBuilder<GamesSearchActivity> {
        }
    }

    @Binds
    @IntoMap
    @ActivityKey(WatchListActivity.class)
    public abstract AndroidInjector.Factory<? extends Activity>
    bindWatchListActivityInjectorFactory(WatchListActivityComponent.Builder builder);

    @Subcomponent(modules = {
        ActivityModule.class,
        AndroidFragmentModule.class
    })
    public interface WatchListActivityComponent extends AndroidInjector<WatchListActivity> {

        @Subcomponent.Builder
        abstract class Builder extends BaseActivityComponentBuilder<WatchListActivity> {
        }
    }

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    public abstract AndroidInjector.Factory<? extends Activity>
    bindMainActivityInjectorFactory(MainActivityComponent.Builder builder);

    @Subcomponent(modules = {
        ActivityModule.class,
        AndroidFragmentModule.class
    })
    public interface MainActivityComponent extends AndroidInjector<MainActivity> {

        @Subcomponent.Builder
        abstract class Builder extends BaseActivityComponentBuilder<MainActivity> {
        }
    }
}
