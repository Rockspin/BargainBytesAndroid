/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.ui.dialogs.watchlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import butterknife.Bind;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fernandocejas.arrow.checks.Preconditions;
import com.rockspin.bargainbits.BargainBytesApp;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

/**
 * Dialog that allows users to customise a watch_list entry.
 */
public class EditWatchListEntryDialogFragment extends DialogFragment {
    private static final String WATCHED_ITEM_KEY = "WATCHED_ITEM_KEY";

    @Bind(R.id.edit_watchlist_view) IView view;
    private WatchedItem itemToEditOrRemove;
    private View customView;
    @Inject WatchListRepository watchListRepository;
    @Inject EditWatchListEntryPresenter presenter;

    public static EditWatchListEntryDialogFragment newInstance(WatchedItem watchedItem) {
        Bundle args = new Bundle(1);
        args.putParcelable(WATCHED_ITEM_KEY, watchedItem);
        EditWatchListEntryDialogFragment fragment = new EditWatchListEntryDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemToEditOrRemove = Preconditions.checkNotNull(getArguments().getParcelable(WATCHED_ITEM_KEY), "must call setWatchedItem before show");
        EditWatchedListEntryView editWatchedListEntryView = new EditWatchedListEntryView(getContext());
        view = editWatchedListEntryView;
        customView = editWatchedListEntryView;

        view.setPresenter(presenter);
    }

    @Override public void onAttach(Context activity) {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    @Override public void onStart() {
        super.onStart();
        Preconditions.checkNotNull(itemToEditOrRemove, " created without passed in WatchedItem");
        view.start(itemToEditOrRemove);
    }

    @Override public void onStop() {
        super.onStop();
        view.stop();
    }

    @NonNull @Override public MaterialDialog onCreateDialog(final Bundle savedInstanceState) {
        //TODO: Look for a better way to do this
        boolean itemCurrentlyWatched = watchListRepository.isItemInWatchList(itemToEditOrRemove);

        final String title = itemCurrentlyWatched ? getString(R.string.edit_watch_list) : getString(R.string.add_to_watch_list);
        final String positiveButtonText = itemCurrentlyWatched ? getString(R.string.edit) : getString(R.string.add);
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        if (itemCurrentlyWatched) {
            builder.neutralColorRes(R.color.destructive_color);
            builder.neutralText(R.string.remove);
        }

        builder.title(title)
               .dividerColorRes(R.color.primary_color)
               .titleColorRes(R.color.primary_color)
               .positiveText(positiveButtonText)
               .negativeText(R.string.cancel)
               .positiveColorRes(R.color.primary_color)
               .negativeColorRes(R.color.primary_color)
               .customView(customView, false)
               .autoDismiss(true)
               .callback(new MaterialDialog.ButtonCallback() {
                   @Override public void onPositive(MaterialDialog dialog) {
                       view.onDismissed(true);
                   }

                   @Override public void onNeutral(MaterialDialog dialog) {
                       super.onNeutral(dialog);
                       view.onDismissed(false);
                   }
               });

        return builder.build();
    }

    public interface IView {

        void onDismissed(boolean b);

        boolean isItemInWatchList();

        void stop();

        void start(WatchedItem itemToEditOrRemove);

        void setPresenter(EditWatchListEntryPresenter presenter);
    }
}
