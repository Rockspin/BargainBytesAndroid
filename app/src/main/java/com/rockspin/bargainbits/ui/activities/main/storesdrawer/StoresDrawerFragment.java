package com.rockspin.bargainbits.ui.activities.main.storesdrawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.adapter.StoreDrawerListAdapter;
import com.rockspin.bargainbits.ui.activities.main.storesdrawer.adapter.StoreEnabled;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import rx.Observable;

public class StoresDrawerFragment extends Fragment implements StoresDrawerPresenter.IView{

    private Observable<Boolean> allStoresSwitchObservable;
    private Observable<Void> reloadStores;
    private View rootView;

    protected SwitchCompat allStoresSwitch;
    @BindView(R.id.drawerListview) ListView storeListView;
    @BindView(R.id.loadingStoresProgressBar) ProgressBar loadingStoresProgressBar;
    @BindView(R.id.cantLoadStores) TextView cantLoadStores;

    @Inject StoresDrawerPresenter presenter;
    @Inject StoreDrawerListAdapter mStoreDrawerListAdapter;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.all_stores_drawer, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        final View storeListHeader = inflater.inflate(R.layout.stores_list_header, storeListView, false);

        allStoresSwitch = (SwitchCompat) storeListHeader.findViewById(R.id.useAllStoresSwitch);
        storeListView.addHeaderView(storeListHeader);
        storeListView.setAdapter(mStoreDrawerListAdapter);

        allStoresSwitchObservable = RxCompoundButton.checkedChanges(allStoresSwitch);
        reloadStores = RxView.clicks(cantLoadStores);
        return rootView;
    }

    public void onStart() {
        super.onStart();
        presenter.start(this);
    }

    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //StoresDrawerPresenter.View
    @Override public Observable<StoreEnabled> onStoreSelected() {
        return mStoreDrawerListAdapter.onStoreEnabled();
    }

    @Override
    public void setListContent(List<StoreEnabled> stores) {
        mStoreDrawerListAdapter.clear();
        mStoreDrawerListAdapter.addAll(stores);
        mStoreDrawerListAdapter.notifyDataSetChanged();
    }

    @Override public void updateStoreList(Boolean allStoresChecked) {
        mStoreDrawerListAdapter.setAllStoresChecked(allStoresChecked);
        mStoreDrawerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void failedToLoadStores(Throwable throwable) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.error)
               .setMessage(R.string.error_no_stores)
               .setPositiveButton(R.string.ok, (dialog, id) -> {
               })
               .setCancelable(false);
        builder.create()
               .show();
        loadingStoresProgressBar.setVisibility(View.INVISIBLE);
        cantLoadStores.setVisibility(View.VISIBLE);
    }

    @Override
    public void setStoresLoading(boolean visible) {
        if (visible) {
            //when we have data do nothing
            if (mStoreDrawerListAdapter.isEmpty()) {
                cantLoadStores.setVisibility(View.INVISIBLE);
                loadingStoresProgressBar.setVisibility(View.VISIBLE);
            }
        } else {
            loadingStoresProgressBar.setVisibility(View.GONE);

            if (mStoreDrawerListAdapter.isEmpty()) {
                cantLoadStores.setVisibility(View.VISIBLE);
            } else {
                cantLoadStores.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override public Observable<Boolean> onAllStoresSwitched() {
        return allStoresSwitchObservable;
    }

    @Override public Observable<Void> onClickReloadStores() {
        return reloadStores.mergeWith(RxView.clicks(rootView));
    }

    @Override public void setAllStoresChecked(boolean allStoresEnabled) {
        allStoresSwitch.setChecked(allStoresEnabled);
    }
}
