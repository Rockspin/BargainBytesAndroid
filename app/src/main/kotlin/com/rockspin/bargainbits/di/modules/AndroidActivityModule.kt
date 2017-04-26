package com.rockspin.bargainbits.di.modules

import android.app.Activity
import com.rockspin.apputils.di.BaseActivityComponentBuilder
import com.rockspin.apputils.di.modules.activities.ActivityModule
import com.rockspin.bargainbits.ui.activities.WatchListActivity
import com.rockspin.bargainbits.ui.activities.main.MainActivity
import com.rockspin.bargainbits.ui.activities.search.GamesSearchActivity
import com.rockspin.bargainbits.ui.search.SearchActivity
import com.rockspin.bargainbits.ui.search.SearchDetailActivity
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
    AndroidActivityModule.GamesSearchActivityComponent::class,
    AndroidActivityModule.SearchActivityComponent::class,
    AndroidActivityModule.WatchListActivityComponent::class,
    AndroidActivityModule.MainActivityComponent::class,
    AndroidActivityModule.SearchDetailActivityComponent::class
))
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
    @ActivityKey(SearchActivity::class)
    abstract fun bindSearchActivityInjectorFactory(builder: SearchActivityComponent.Builder): AndroidInjector.Factory<out Activity>

    @Subcomponent(modules = arrayOf(ActivityModule::class, AndroidFragmentModule::class))
    interface SearchActivityComponent : AndroidInjector<SearchActivity> {

        @Subcomponent.Builder
        abstract class Builder : BaseActivityComponentBuilder<SearchActivity>()
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

    @Binds
    @IntoMap
    @ActivityKey(SearchDetailActivity::class)
    abstract fun bindSearchDetailActivityInjectorFactory(builder: SearchDetailActivityComponent.Builder): AndroidInjector.Factory<out Activity>

    @Subcomponent(modules = arrayOf(ActivityModule::class, AndroidFragmentModule::class))
    interface SearchDetailActivityComponent : AndroidInjector<SearchDetailActivity> {

        @Subcomponent.Builder
        abstract class Builder : BaseActivityComponentBuilder<SearchDetailActivity>()
    }
}
