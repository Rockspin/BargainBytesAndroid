package com.rockspin.bargainbits.ui.views.deallist.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.repository.DealRepository;
import com.rockspin.bargainbits.data.repository.WatchListRepository;
import com.rockspin.bargainbits.ui.dialogs.store_picker.StorePickerAdapter;
import com.rockspin.bargainbits.ui.dialogs.store_picker.StorePickerDialogFragment;
import com.rockspin.bargainbits.ui.dialogs.watchlist.EditWatchListEntryDialogFragment;
import com.rockspin.bargainbits.ui.views.deallist.DealShareModel;
import com.rockspin.bargainbits.ui.views.deallist.DealsListView;
import com.rockspin.bargainbits.ui.views.deallist.recycleview.DealAdapterModel;
import com.rockspin.bargainbits.ui.views.deallist.recycleview.DealRecyclerAdapter;
import com.rockspin.bargainbits.utils.DealUtils;
import com.rockspin.bargainbits.watch_list.WatchedItem;
import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class DealsListViewImpl extends RecyclerView implements DealsListPresenter.View, DealsListView {

    private static final String STORE_PICKER_DIALOG_TAG = "STORE_PICKER_DIALOG_TAG";
    private static final String WATCH_LIST_DIALOG_TAG = "WATCH_LIST_DIALOG_TAG";

    private final BehaviorSubject<Boolean> onLoadingSubject;
    private final DealRecyclerAdapter dealRecyclerAdapter;
    private DealsListContainer dealsListContainer;

    private DealsListPresenter presenter;

    public DealsListViewImpl(Context context) {
        this(context, null);
    }

    public DealsListViewImpl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DealsListViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {
            onLoadingSubject = BehaviorSubject.create(false);

            dealRecyclerAdapter = new DealRecyclerAdapter(LayoutInflater.from(context));
            setLayoutManager(new LinearLayoutManager(context));
            ((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
        } else {
            onLoadingSubject = null;
            dealRecyclerAdapter = null;
        }
    }

    @Override
    public void setPresenter(DealsListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override public void viewWillShow(DealsListContainer dealsListContainer) {
        this.dealsListContainer = dealsListContainer;
        presenter.start(this);
        setAdapter(dealRecyclerAdapter);
    }

    @Override public void loadDealsWithSorting(DealRepository.EDealsSorting dealsSorting) {
        presenter.loadDealsWithSorting(dealsSorting);
    }

    @Override public void loadDealsInWatchList() {
        presenter.loadDealsInWatchList();
    }

    @Override public void viewWillHide() {
        presenter.stop();
    }

    @Override public Observable<Boolean> onListLoading() {
        return onLoadingSubject.asObservable();
    }

    @Override public boolean isListEmpty() {
        return dealRecyclerAdapter.isEmpty();
    }

    //TODO: change this to a function that returns a bitmap
    @Override public void shareDeal(@NonNull DealShareModel dealShareModel) {
        DealRecyclerAdapter.IDealCell iDealCell = (DealRecyclerAdapter.IDealCell) findViewHolderForAdapterPosition(dealShareModel.getIndex());
        final Bitmap bitmap = iDealCell.getSharingBitmap();
        DealUtils.shareDeal(getContext(), bitmap, dealShareModel.getGameName(), dealShareModel.getSavingPercentage());
    }

    @Override public void showLoadingDealsError(Throwable throwable) {
        showToast(R.string.error_no_connection, Toast.LENGTH_LONG);
    }

    @Override public Observable<Void> onBackPressed() {
        return Observable.empty();// RxView.clicks(backButton);
    }

    @Override public Observable<Integer> onClickOpenDeal() {
        return dealRecyclerAdapter.onClickOpenDeal();
    }

    @Override public Observable<Integer> onClickEditDeal() {
        return dealRecyclerAdapter.onClickEditDeal();
    }

    @Override public Observable<Integer> onClickShareDeal() {
        return dealRecyclerAdapter.onClickShareDeal();
    }

    @Override public void updateListView(@NonNull List<DealAdapterModel> result) {
        // update the list.
        dealRecyclerAdapter.clear();
        dealRecyclerAdapter.addAll(result);
        dealRecyclerAdapter.notifyDataSetChanged();
        // let the rest of the app know when the list if updated.
    }

    private void showToast(int errorString, int length) {
        Toast.makeText(this.getContext(), errorString, length).show();
    }

    @Override public void setLoadingVisible(boolean visible) {
        onLoadingSubject.onNext(visible);
    }

    @Override public void openDealUrl(@NonNull String url) {
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getContext().startActivity(browserIntent);
    }

    @Override public void editOrAddToWatchList(WatchedItem watchedItem) {
        final EditWatchListEntryDialogFragment editWatchListEntryDialogFragment = EditWatchListEntryDialogFragment.newInstance(watchedItem);
        editWatchListEntryDialogFragment.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), WATCH_LIST_DIALOG_TAG);
    }

    @Override public void removeDealAtIndex(Integer integer) {
        dealRecyclerAdapter.remove(integer);
    }

    @Override public void addDealToList(DealAdapterModel dealAdapterModel) {
        dealRecyclerAdapter.add(dealAdapterModel);
    }

    @Override public void closeView() {
        dealsListContainer.closeView();
    }

    @Override public Observable<Integer> showStorePicker(List<StorePickerAdapter.StorePickerData> deals) {
        final StorePickerDialogFragment storePickerDialogFragment = StorePickerDialogFragment.instantiate(deals);
        storePickerDialogFragment.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), STORE_PICKER_DIALOG_TAG);
        return storePickerDialogFragment.onDealSelected();
    }

    public void showItemAddedToWatchList(WatchedItem watchedItem) {
        showSnackBar(getContext().getString(R.string.watchlist_item_added, watchedItem.getGameName()));
    }

    public void showItemRemovedFromWatchlist(WatchedItem watchedItem) {
        final String title = getContext().getString(R.string.watchlist_item_removed, watchedItem.getGameName());
        final String undo = getContext().getString(R.string.undo);
        showSnackBar(title, undo, v -> presenter.addItemToWatchList(watchedItem));
    }

    public void showGameInWatchlistEdited(WatchedItem watchedItem) {
        showSnackBar(getContext().getString(R.string.watchlist_item_edited, watchedItem.getGameName()));
    }

    public void showWatchListFull() {
        String maxItemsWarning = getContext().getString(R.string.max_watchlist_item_warning, WatchListRepository.MAX_WATCHED_ITEMS);
        showSnackBar(maxItemsWarning);
    }

    private void showSnackBar(@NonNull String string) {
        showSnackBar(string, null, null);
    }

    private void showSnackBar(@NonNull String mainText, @Nullable String buttonText, @Nullable android.view.View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(this, mainText, Snackbar.LENGTH_LONG).setActionTextColor(ContextCompat.getColor(getContext(), R.color.primary_color));
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);  // show multiple line
        snackbar.setAction(buttonText, onClickListener);
        snackbar.show();
    }

    public interface DealsListContainer {

        void closeView();
    }
}
