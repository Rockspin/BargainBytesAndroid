/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.activities.search.gameinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.rockspin.apputils.di.annotations.ActivityScope;
import com.rockspin.bargainbits.BargainBytesApp;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.cheapshark.Game;
import com.rockspin.bargainbits.ui.activities.search.gameinfo.recycler.GameInfoRecyclerAdapter;
import com.rockspin.bargainbits.ui.activities.search.gameinfo.recycler.GamesInfoDealsViewModel;
import com.rockspin.bargainbits.ui.dialogs.watchlist.EditWatchListEntryDialogFragment;
import com.rockspin.bargainbits.ui.views.WebImageView;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import dagger.android.support.AndroidSupportInjection;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Fragment that displays the info for a particular searched game.
 */
public final class GameInfoFragment extends Fragment implements GameInfoPresenter.IView {
    private static final String GAME_KEY = "GAME_KEY";

    @Inject GameInfoPresenter presenter;
    @Inject Activity activity;
    @Inject GameInfoRecyclerAdapter mAbbreviatedDealAdapter;
    @Inject @ActivityScope Resources resources;

    @Bind(R.id.standardProgressBar) ProgressBar mProgressBar;
    @Bind(R.id.backButton) Button mGoBackButton;
    @Bind(R.id.gameImage) WebImageView webImageView;
    @Bind(R.id.infoGameTitle) TextView gameTitle;
    @Bind(R.id.gameDealsListView) RecyclerView gameDealsListView;

    private final EditWatchListEntryDialogFragment editWatchListEntryDialogFragment = new EditWatchListEntryDialogFragment();

    public static GameInfoFragment newInstance(final Game game) {
        final GameInfoFragment fragment = new GameInfoFragment();
        final Gson gson = new Gson();
        final Bundle bundle = new Bundle();
        bundle.putString(GAME_KEY, gson.toJson(game));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_game_info, container, false);
        ButterKnife.bind(this, view);
        gameDealsListView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameDealsListView.setAdapter(mAbbreviatedDealAdapter);
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        final String jsonSerializedGame = getArguments().getString(GAME_KEY);
        final Game game = new Gson().fromJson(jsonSerializedGame, Game.class);
        presenter.setData(game);
        presenter.start(this);
    }

    @Override public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.game_info, menu);
    }

    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_add_to_watch_list) {
            presenter.addGameToWatchList();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override public Observable<Integer> onItemSelected() {
        return mAbbreviatedDealAdapter.onItemSelected();
    }

    // GameInfoPresenter.View
    @Override public void onGameLoaded(final Game mGame) {
        webImageView.loadImageFromUrl(mGame.getThumb());
        gameTitle.setText(mGame.getExternal());
        setTitle(mGame.getExternal());
    }

    @Override public void refreshList(List<GamesInfoDealsViewModel> gameInfo) {
        mAbbreviatedDealAdapter.clear();
        mAbbreviatedDealAdapter.addAll(gameInfo);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override public void onRefreshListFailed(Throwable throwable) {
        if (mGoBackButton != null) {
            mGoBackButton.setVisibility(View.VISIBLE);
        }
        Snackbar.make(gameTitle, R.string.error_no_connection, Snackbar.LENGTH_SHORT)
                .setActionTextColor(ContextCompat.getColor(getContext(), R.color.primary_color))
                .show();
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override public void addItemToWatchList(WatchedItem watchedItem) {
        editWatchListEntryDialogFragment.setWatchedItem(watchedItem);
        editWatchListEntryDialogFragment.show(getFragmentManager(), "EditWatchListEntryDialogFragment");
    }

    @Override public void openDealUrl(String url) {
        boolean activityDestroyed = Build.VERSION.SDK_INT >= 17 && activity.isDestroyed();
        if (!activityDestroyed && !activity.isFinishing()) {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(browserIntent);
        }
    }

    private void setTitle(final String external) {
        activity.setTitle(external);
    }
}
