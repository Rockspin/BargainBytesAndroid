package com.rockspin.bargainbits.di.modules

import android.app.Activity
import com.rockspin.apputils.di.BaseActivityComponentBuilder
import com.rockspin.apputils.di.modules.activities.ActivityModule
import com.rockspin.bargainbits.ui.search.SearchActivity
import com.rockspin.bargainbits.ui.search.detail.SearchDetailActivity
import com.rockspin.bargainbits.ui.top_navigation.TopNavigationActivity
import com.rockspin.bargainbits.ui.watch_list.WatchListActivity
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
    AndroidActivityModule.SearchActivityComponent::class,
    AndroidActivityModule.WatchListActivityComponent::class,
    AndroidActivityModule.TopNavigationActivityComponent::class,
    AndroidActivityModule.SearchDetailActivityComponent::class
))
abstract class AndroidActivityModule {

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
    @ActivityKey(TopNavigationActivity::class)
    abstract fun bindTopNavigationActivityInjectorFactory(builder: TopNavigationActivityComponent.Builder): AndroidInjector.Factory<out Activity>

    @Subcomponent(modules = arrayOf(ActivityModule::class, AndroidFragmentModule::class))
    interface TopNavigationActivityComponent : AndroidInjector<TopNavigationActivity> {

        @Subcomponent.Builder
        abstract class Builder : BaseActivityComponentBuilder<TopNavigationActivity>()
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
