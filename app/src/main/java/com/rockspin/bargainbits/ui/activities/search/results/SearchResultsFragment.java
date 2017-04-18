/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.activities.search.results;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.rockspin.bargainbits.BargainBytesApp;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.cheapshark.Game;
import com.rockspin.bargainbits.ui.activities.search.gameinfo.GameInfoFragment;
import com.rockspin.bargainbits.ui.activities.search.results.recycler.SearchResultsAdapter;
import com.rockspin.bargainbits.ui.activities.search.results.recycler.SearchResultsAdapter.SearchResultsAdapterViewHolder;
import com.rockspin.bargainbits.ui.activities.search.results.recycler.SearchResultsAdapterModel;
import com.rockspin.bargainbits.ui.views.PriceView;
import com.rockspin.bargainbits.ui.views.WebImageView;
import dagger.android.support.AndroidSupportInjection;
import icepick.Icepick;
import icepick.State;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Fragment that displays a list of game search results.
 */

public final class SearchResultsFragment extends Fragment implements SearchResultsPresenter.IView {

    @Bind(R.id.gamesListView) RecyclerView gamesRecyclerView;
    @Bind(R.id.noResultsView) View noResultsView;
    @Bind(R.id.standardProgressBar) ProgressBar progressBar;
    @Bind(R.id.backButton) Button backButton;

    @Inject SearchResultsPresenter presenter;
    @Inject SearchResultsAdapter searchResultsListAdapter;

    @State String searchQuery;

    public SearchResultsFragment() { /* not used */ }

    public static SearchResultsFragment newInstance(final String query) {
        final SearchResultsFragment fragment = new SearchResultsFragment();
        fragment.searchQuery = query;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.view_search_results, container, false);
        ButterKnife.bind(this, view);

        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gamesRecyclerView.setAdapter(searchResultsListAdapter);

        return view;
    }

    @Override public void onStart() {
        super.onStart();
        presenter.start(searchQuery, this);
    }

    @Override public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    // SearchResultsPresenter.IView implementation
    @Override public void setActionBarSearchTitle(final String query) {
        final String titleString = "\"" + query + "\" " + getString(R.string.search_results);
        getActivity().setTitle(titleString);
    }

    @Override public void checkForEmptyList() {
        if (!searchResultsListAdapter.isEmpty()) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override public void updateList(List<SearchResultsAdapterModel> result) {
        searchResultsListAdapter.clear();
        searchResultsListAdapter.addAll(result);
        searchResultsListAdapter.notifyDataSetChanged();

        setResultsFound(!result.isEmpty());
        progressBar.setVisibility(View.GONE);
    }

    @Override public void showError() {
        progressBar.setVisibility(View.GONE);
        setResultsFound(false);
    }

    @Override public void setTitle(String searchQuery) {
        getActivity().setTitle(searchQuery);
    }

    @Override public final void displayInfoForGame(final Game game, final Integer index) {
        final GameInfoFragment gameInfoFragment = GameInfoFragment.newInstance(game);
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.fragmentHolder, gameInfoFragment).addToBackStack(null).setBreadCrumbTitle(game.getExternal());

        SearchResultsAdapterViewHolder vh = (SearchResultsAdapterViewHolder) gamesRecyclerView.findViewHolderForAdapterPosition(index);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // set up the shared elements
            final WebImageView gameThumb = vh.getGameThumbImage();
            ft.addSharedElement(gameThumb, getString(R.string.game_info_image_trans));

            TextView textView = vh.getTitle();
            ft.addSharedElement(textView, getString(R.string.game_info_title_trans));

            PriceView priceView = vh.getPriceView();
            ft.addSharedElement(priceView, getString(R.string.game_info_price_trans));

            // set up the animation the shared elements will use
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
            transitionSet.addTransition(new ChangeTransform()).addTransition(new ChangeImageTransform()).addTransition(new ChangeBounds());

            gameInfoFragment.setSharedElementEnterTransition(transitionSet);
            setSharedElementReturnTransition(transitionSet);

            // set the transition the rest of the view will use
            final Transition transition = new Fade();

            gameInfoFragment.setEnterTransition(transition);
            gameInfoFragment.setExitTransition(transition);

            setExitTransition(transition);
            setEnterTransition(transition);
        }

        ft.commit();
    }

    @Override public Observable<Integer> onItemSelected() {
        return searchResultsListAdapter.onItemSelected();
    }

    @Override public Observable<Void> onBackButtonPressed() {
        return RxView.clicks(backButton);
    }

    @Override public void closeView() {
        getActivity().onBackPressed();
    }

    private void setResultsFound(final boolean resultsFound) {
        gamesRecyclerView.setVisibility(resultsFound ? View.VISIBLE : View.GONE);
        noResultsView.setVisibility(resultsFound ? View.GONE : View.VISIBLE);
    }
}
