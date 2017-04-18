package com.rockspin.bargainbits.ui.activities.search.gameinfo.recycler;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.rockspin.apputils.di.annotations.ActivityScope;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.ui.views.PriceView;
import com.rockspin.bargainbits.utils.DealUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;

public class GameInfoRecyclerAdapter extends RecyclerView.Adapter<GameInfoRecyclerAdapter.SearchResultsViewHolder> {

    private final List<GamesInfoDealsViewModel> dealList = new ArrayList<>();

    private final PublishSubject<Integer> itemSelected = PublishSubject.create();
    private final LayoutInflater layoutInflater;

    @Inject GameInfoRecyclerAdapter(@ActivityScope LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = layoutInflater.inflate(R.layout.abbreviated_deal_view, parent, false);
        return new SearchResultsViewHolder(rootView);
    }

    @Override public int getItemCount() {
        return dealList.size();
    }

    @Override public void onBindViewHolder(SearchResultsViewHolder holder, int position) {
        final GamesInfoDealsViewModel deal = dealList.get(position);

        holder.storeName.setText(deal.storeName);
        holder.priceView.setSalePriceText(deal.dealPrice);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && position == 0) {
            holder.priceView.setTransitionName(holder.itemView.getContext().getString(R.string.game_info_price_trans));
        }
        if (deal.singlePriceMode) {
            holder.priceView.setSinglePriceMode();
        } else{
            holder.priceView.setRetailPriceText(deal.retailPrice);
        }

        if (deal.hasSavings) {
            holder.savingsView.setText(DealUtils.getFormattedSavingsString(holder.savingsView.getContext(), deal.savingPercentage));
        } else {
            holder.savingsView.setVisibility(View.GONE);
        }

        holder.storeImage.setImageDrawable(deal.storeImage);

        RxView.clicks(holder.itemView).takeUntil(RxView.detaches(holder.itemView))
              .doOnNext(aVoid -> itemSelected.onNext(position))
              .subscribe();
    }

    public Observable<Integer> onItemSelected() {
        return itemSelected;
    }

    public void clear() {
        dealList.clear();
    }

    public void addAll(List<GamesInfoDealsViewModel> gameInfo) {
        clear();
        dealList.addAll(gameInfo);
        notifyDataSetChanged();
    }

    public static class SearchResultsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.priceHolder) PriceView priceView;
        @Bind(R.id.storeImage) ImageView storeImage;
        @Bind(R.id.storeName) TextView storeName;
        @Bind(R.id.savingPercentage) TextView savingsView;

        public SearchResultsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
