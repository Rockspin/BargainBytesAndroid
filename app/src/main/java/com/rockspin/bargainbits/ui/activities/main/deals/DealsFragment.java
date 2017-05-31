/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.activities.main.deals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.jakewharton.rxbinding2.view.RxView;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.repository.DealRepository;
import com.rockspin.bargainbits.ui.views.deallist.DealsListView;
import com.rockspin.bargainbits.ui.views.deallist.view.DealsListPresenter;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

import io.reactivex.Observable;

public final class DealsFragment extends Fragment implements DealFragmentPresenter.View {
    private static final String DEALS_SORTING_KEY = "DEALS_SORTING_KEY";

    public @Bind(R.id.deals_view) DealsListView dealsListView;
    @Bind(R.id.standardProgressBar) ProgressBar mProgressBar;
    @Bind(R.id.emptyView) android.view.View mDealsEmptyView;

    @Inject DealFragmentPresenter dealFragmentPresenter;
    @Inject DealsListPresenter dealsListPresenter;

    private DealRepository.EDealsSorting dealSorting;

    public static DealsFragment create(final DealRepository.EDealsSorting dealTabType) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(DEALS_SORTING_KEY, dealTabType);
        final DealsFragment fragment = new DealsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealSorting = (DealRepository.EDealsSorting) getArguments().getSerializable(DEALS_SORTING_KEY);
    }

    @Override public void onStart() {
        super.onStart();
        dealsListView.viewWillShow();
        dealsListView.loadDealsWithSorting(dealSorting);
    }

    @Override public void onStop() {
        super.onStop();
        dealsListView.viewWillHide();
    }

    @Override public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deals, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dealFragmentPresenter.onViewCreated(this);
        dealsListView.setPresenter(dealsListPresenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dealFragmentPresenter.onViewDestroyed();
    }

    @Override public Observable<Boolean> onListLoading() {
        return dealsListView.onListLoading();
    }

    @Override public Observable<Object> onReloadList() {
        return RxView.clicks(mDealsEmptyView);
    }

    @Override public void refreshList() {
        dealsListView.loadDealsWithSorting(dealSorting);
    }

    @Override public void showListLoading(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);

        if (loading) {
            mDealsEmptyView.setVisibility(View.INVISIBLE);
        } else {
            mDealsEmptyView.setVisibility(dealsListView.isListEmpty() ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
