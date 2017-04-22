package com.rockspin.bargainbits.ui.activities.search.results.recycler;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.rockspin.apputils.di.annotations.ActivityScope;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.ui.views.PriceView;
import com.rockspin.bargainbits.ui.views.WebImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.subjects.PublishSubject;

public class OldSearchResultsAdapter extends RecyclerView.Adapter<OldSearchResultsAdapter.SearchResultsAdapterViewHolder> {

    private final List<SearchResultsAdapterModel> dealList = new ArrayList<>();
    private final PublishSubject<Integer> itemSelected = PublishSubject.create();

    private final LayoutInflater layoutInflater;

    @Inject
    OldSearchResultsAdapter(@ActivityScope LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override public SearchResultsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = layoutInflater.inflate(R.layout.view_search_result, parent, false);
        return new SearchResultsAdapterViewHolder(rootView);
    }

    @Override public int getItemCount() {
        return dealList.size();
    }

    @Override public void onBindViewHolder(SearchResultsAdapterViewHolder holder, int position) {
        final SearchResultsAdapterModel searchResultsAdapterModel = dealList.get(position);

        holder.mGameThumbImage.clear();

        holder.mGameThumbImage.loadImageFromUrl(searchResultsAdapterModel.thumb);
        holder.mGameTitle.setText(searchResultsAdapterModel.title);
        holder.mPriceView.setSalePriceText(searchResultsAdapterModel.cheapestPrice);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Context context = holder.itemView.getContext();
            holder.mGameThumbImage.setTransitionName(context.getString(R.string.game_info_image_trans) + position);
            holder.mGameTitle.setTransitionName(context.getString(R.string.game_info_title_trans) + position);
            holder.mPriceView.setTransitionName(context.getString(R.string.game_info_price_trans) + position);
        }

        RxView.clicks(holder.itemView).takeUntil(RxView.detaches(holder.itemView))
            .doOnNext(aVoid -> itemSelected.onNext(position))
            .subscribe();
    }

    public void clear() {
        dealList.clear();
    }

    public void addAll(List<SearchResultsAdapterModel> gameInfo) {
        clear();
        dealList.addAll(gameInfo);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return dealList.isEmpty();
    }

    public PublishSubject<Integer> onItemSelected() {
        return itemSelected;
    }

    public static class SearchResultsAdapterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.gameTitle) TextView mGameTitle;
        @Bind(R.id.gameThumb) WebImageView mGameThumbImage;
        @Bind(R.id.cheapestPrice) PriceView mPriceView;
        // unsubscribe on cell reuse

        public SearchResultsAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public WebImageView getGameThumbImage() {
            return mGameThumbImage;
        }

        public TextView getTitle() {
            return mGameTitle;
        }

        public PriceView getPriceView() {
            return mPriceView;
        }
    }

}
