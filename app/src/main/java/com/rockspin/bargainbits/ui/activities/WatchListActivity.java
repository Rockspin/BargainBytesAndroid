/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.ui.activities.main.BaseActivity;
import com.rockspin.bargainbits.ui.views.deallist.DealsListView;
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListPresenter;
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListViewImpl;
import com.rockspin.bargainbits.utils.environment.BBEnvironment;
import com.rockspin.bargainbits.utils.environment.IServices;
import javax.inject.Inject;
import rx.Observable;

public class WatchListActivity extends BaseActivity implements DealsListViewImpl.DealsListContainer, WatchListPresenter.View {
    private static final int DEBUG_MENU_ITEM_ID = 1;

    @Bind(R.id.dealList) DealsListView dealsListView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.noResultsView) View mWatchListEmptyView;
    @Bind(R.id.goBackButton) View backButton;
    @Bind(R.id.standardProgressBar) View progressBar;

    @Inject IServices services;
    @Inject WatchListPresenter watchListPresenter;
    @Inject DealsListPresenter dealsListPresenter;

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        dealsListView.setPresenter(dealsListPresenter);
    }

    @Override protected void onStart() {
        super.onStart();
        watchListPresenter.start(this);
        dealsListView.viewWillShow(this);
        dealsListView.loadDealsInWatchList();
    }

    @Override protected void onStop() {
        super.onStop();
        dealsListView.viewWillHide();
        watchListPresenter.stop();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.watch_list, menu);
        if (services.getEnvironment() == BBEnvironment.DEBUG) {
            menu.add(Menu.NONE, DEBUG_MENU_ITEM_ID, Menu.NONE, "Run Notification Check");
        }
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == DEBUG_MENU_ITEM_ID) {
            Toast.makeText(getApplicationContext(), "Implement manual watch list check using new Job.", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void closeView() {
        finish();
    }

    @Override public Observable<Boolean> onListLoading() {
        return dealsListView.onListLoading();
    }

    @Override public Observable<Void> onBackButtonPressed() {
        return RxView.clicks(backButton);
    }

    @Override public void closeWatchList() {
        finish();
    }

    @Override public void showListLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);

        if (loading) {
            mWatchListEmptyView.setVisibility(View.INVISIBLE);
        } else {
            mWatchListEmptyView.setVisibility(dealsListView.isListEmpty() ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
