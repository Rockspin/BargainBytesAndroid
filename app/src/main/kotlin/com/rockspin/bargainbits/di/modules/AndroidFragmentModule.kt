package com.rockspin.bargainbits.di.modules

import com.rockspin.bargainbits.ui.activities.main.deals.DealsFragment
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.StoresDrawerFragment
import com.rockspin.bargainbits.ui.dialogs.store_picker.StorePickerDialogFragment
import com.rockspin.bargainbits.ui.dialogs.watchlist.EditWatchListEntryDialogFragment
import com.rockspin.bargainbits.ui.store_filter.StoreFilterDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeEditWatchListEntryDialogFragmentInjector(): EditWatchListEntryDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeStorePickerDialogFragmentInjector(): StorePickerDialogFragment

    @ContributesAndroidInjector
    internal abstract fun contributeStoresDrawerFragmentInjector(): StoresDrawerFragment

    @ContributesAndroidInjector
    internal abstract fun contributeDealsFragmentInjector(): DealsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeStoreFilterDialogFragmentInjector(): StoreFilterDialogFragment
}
