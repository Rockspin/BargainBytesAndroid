package com.rockspin.bargainbits.di.modules

import android.app.Activity
import com.rockspin.apputils.di.BaseActivityComponentBuilder
import com.rockspin.apputils.di.modules.activities.ActivityModule
import com.rockspin.bargainbits.ui.activities.WatchListActivity
import com.rockspin.bargainbits.ui.activities.main.MainActivity
import com.rockspin.bargainbits.ui.activities.search.GamesSearchActivity
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(AndroidActivityModule.GamesSearchActivityComponent::class, AndroidActivityModule.WatchListActivityComponent::class,
    AndroidActivityModule.MainActivityComponent::class))
abstract class AndroidActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(GamesSearchActivity::class)
    abstract fun bindGamesSearchActivityInjectorFactory(builder: GamesSearchActivityComponent.Builder): AndroidInjector.Factory<out Activity>

    @Subcomponent(modules = arrayOf(ActivityModule::class, AndroidFragmentModule::class))
    interface GamesSearchActivityComponent : AndroidInjector<GamesSearchActivity> {

        @Subcomponent.Builder
        abstract class Builder : BaseActivityComponentBuilder<GamesSearchActivity>()
    }

    @Binds
    @IntoMap
    @ActivityKey(WatchListActivity::class)
    abstract fun bindWatchListActivityInjectorFactory(builder: WatchListActivityComponent.Builder): AndroidInjector.Factory<out Activity>

    @Subcomponent(modules = arrayOf(ActivityModule::class, AndroidFragmentModule::class))
    interface WatchListActivityComponent : AndroidInjector<WatchListActivity> {

        @Subcomponent.Builder
        abstract class Builder : BaseActivityComponentBuilder<WatchListActivity>()
    }

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindMainActivityInjectorFactory(builder: MainActivityComponent.Builder): AndroidInjector.Factory<out Activity>

    @Subcomponent(modules = arrayOf(ActivityModule::class, AndroidFragmentModule::class))
    interface MainActivityComponent : AndroidInjector<MainActivity> {

        @Subcomponent.Builder
        abstract class Builder : BaseActivityComponentBuilder<MainActivity>()
    }
}
