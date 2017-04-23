package com.rockspin.bargainbits.di.modules;

import android.support.v4.app.Fragment;
import com.rockspin.bargainbits.ui.activities.main.deals.DealsFragment;
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.StoresDrawerFragment;
import com.rockspin.bargainbits.ui.activities.search.gameinfo.GameInfoFragment;
import com.rockspin.bargainbits.ui.activities.search.results.SearchResultsFragment;
import com.rockspin.bargainbits.ui.dialogs.store_picker.StorePickerDialogFragment;
import com.rockspin.bargainbits.ui.dialogs.watchlist.EditWatchListEntryDialogFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
    AndroidFragmentModule.StorePickerDialogFragmentComponent.class,
    AndroidFragmentModule.EditWatchListEntryDialogFragmentComponent.class,
    AndroidFragmentModule.GameInfoFragmentComponent.class,
    AndroidFragmentModule.StoresDrawerFragmentComponent.class,
    AndroidFragmentModule.SearchResultsFragmentComponent.class,
    AndroidFragmentModule.DealsFragmentComponent.class
})
public abstract class AndroidFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(StorePickerDialogFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment>
    bindStorePickerDialogFragmentInjectorFactory(StorePickerDialogFragmentComponent.Builder builder);

    @Subcomponent
    public interface StorePickerDialogFragmentComponent extends AndroidInjector<StorePickerDialogFragment> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<StorePickerDialogFragment> {
        }
    }

    @Binds
    @IntoMap
    @FragmentKey(EditWatchListEntryDialogFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment>
    bindEditWatchListEntryDialogFragmentInjectorFactory(EditWatchListEntryDialogFragmentComponent.Builder builder);

    @Subcomponent
    public interface EditWatchListEntryDialogFragmentComponent extends AndroidInjector<EditWatchListEntryDialogFragment> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<EditWatchListEntryDialogFragment> {
        }
    }

    @Binds
    @IntoMap
    @FragmentKey(SearchResultsFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment>
    bindSearchResultsFragmentInjectorFactory(SearchResultsFragmentComponent.Builder builder);

    @Subcomponent
    public interface SearchResultsFragmentComponent extends AndroidInjector<SearchResultsFragment> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<SearchResultsFragment> {
        }
    }

    @Binds
    @IntoMap
    @FragmentKey(GameInfoFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment>
    bindGameInfoFragmentInjectorFactory(GameInfoFragmentComponent.Builder builder);

    @Subcomponent
    public interface GameInfoFragmentComponent extends AndroidInjector<GameInfoFragment> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<GameInfoFragment> {
        }
    }

    @Binds
    @IntoMap
    @FragmentKey(StoresDrawerFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment>
    bindStoresDrawerFragmentInjectorFactory(StoresDrawerFragmentComponent.Builder builder);

    @Subcomponent
    public interface StoresDrawerFragmentComponent extends AndroidInjector<StoresDrawerFragment> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<StoresDrawerFragment> {
        }
    }

    @Binds
    @IntoMap
    @FragmentKey(DealsFragment.class)
    public abstract AndroidInjector.Factory<? extends Fragment>
    bindDealsFragmentInjectorFactory(DealsFragmentComponent.Builder builder);

    @Subcomponent
    public interface DealsFragmentComponent extends AndroidInjector<DealsFragment> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<DealsFragment> {
        }
    }
}
