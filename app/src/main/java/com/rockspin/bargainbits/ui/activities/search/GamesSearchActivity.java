/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.activities.search;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.rockspin.apputils.di.annotations.ActivityScope;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.ui.activities.main.BaseActivity;
import com.rockspin.bargainbits.ui.activities.search.results.SearchResultsFragment;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Activity that displays game search results.
 */
public class GamesSearchActivity extends BaseActivity implements GamesSearchPresenter.IView {

    private final PublishSubject<Void> backClickedSubject = PublishSubject.create();
    private String query;

    @Nullable private Snackbar networkSnackbar;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.fragmentHolder) View fragmentHolder;

    @Inject GamesSearchPresenter presenter;
    @Inject @ActivityScope Resources resources;

    @Override protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_games_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        boolean isSearch = intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEARCH);
        if (isSearch) {
            query = intent.getStringExtra(SearchManager.QUERY);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                               .replace(R.id.fragmentHolder, SearchResultsFragment.newInstance(query))
                               .setBreadCrumbTitle(toolbar.getTitle())
                               .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                               .addToBackStack(null)
                               .commit();
            }
        }
    }

    @Override protected void onStart() {
        super.onStart();
        presenter.start(query, this);
    }

    @Override protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        backClickedSubject.onNext(null);
        return super.onOptionsItemSelected(item);
    }

    // GamesSearchPresenter.View
    @Override public void onNetworkChanged(Boolean networkAvailable) {
        if (!networkAvailable) {
            networkSnackbar = Snackbar.make(fragmentHolder, "Your Snackbar", Snackbar.LENGTH_INDEFINITE).setAction("Your Action", null);
            networkSnackbar.show();
        } else {
            if (networkSnackbar != null) {
                networkSnackbar.dismiss();
            }
        }
    }

    @Override public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override public void removeFragmentOrCloseActivity() {
        onBackPressed();
    }

    @Override public void onBackPressed() {
        // if we are displaying one fragments close the activity
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override public Observable<Void> onBackClicked() {
        return RxToolbar.navigationClicks(toolbar);
    }

    @Override public void watchListEdited(WatchedItem watchedItem) {
        showSnackBar(getString(R.string.watchlist_item_edited, watchedItem.gameName()));
    }

    @Override public void watchListAdded(WatchedItem watchedItem) {
        showSnackBar(getString(R.string.watchlist_item_added, watchedItem.gameName()));
    }

    @Override public void watchListFull() {
        String maxItemsWarning = getString(R.string.max_watchlist_item_warning, WatchListRepository.MAX_WATCHED_ITEMS);
        showSnackBar(maxItemsWarning);
    }

    @Override public void onWatchListItemRemoved(WatchedItem itemRemoved, View.OnClickListener clickListener) {
        final String snackbarText = getString(R.string.watchlist_item_removed, itemRemoved.gameName());
        showSnackBar(snackbarText, clickListener);
    }

    private void showSnackBar(String snackbarText, View.OnClickListener action) {
        Snackbar.make(toolbar, snackbarText, Snackbar.LENGTH_LONG).setAction(R.string.undo, action).setActionTextColor(ContextCompat.getColor(this, R.color.primary_color)).show();
    }

    private void showSnackBar(String maxItemsWarning) {
        Snackbar.make(toolbar, maxItemsWarning, Snackbar.LENGTH_LONG).setActionTextColor(ContextCompat.getColor(this, R.color.primary_color)).show();
    }
}
