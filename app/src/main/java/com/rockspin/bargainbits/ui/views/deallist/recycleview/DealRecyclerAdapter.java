package com.rockspin.bargainbits.ui.views.deallist.recycleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.ui.views.OptionsLayout;
import com.rockspin.bargainbits.ui.views.PriceView;
import com.rockspin.bargainbits.ui.views.WebImageView;
import com.rockspin.bargainbits.ui.views.deallist.recycleview.storesgrid.StoreGridImageAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public final class DealRecyclerAdapter extends RecyclerView.Adapter<DealRecyclerAdapter.DealViewHolder> {

    private final List<DealAdapterModel> dealModelList = new ArrayList<>();
    private final PublishSubject<Integer> onClickShare = PublishSubject.create();
    private final PublishSubject<Integer> onClickEdit = PublishSubject.create();
    private final PublishSubject<Integer> onClickOpen = PublishSubject.create();

    private int currentSelectedIndex = -1;
    private int lastRemovedIndex = -1;

    private final LayoutInflater layoutInflater;

    public DealRecyclerAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override public DealViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = layoutInflater.inflate(R.layout.deal_view, parent, false);
        return new DealViewHolder(view);
    }

    @Override public void onBindViewHolder(final DealViewHolder holder, final int position) {
        final DealAdapterModel data = dealModelList.get(position);

        holder.setSelected(position == currentSelectedIndex);

        //get the release date
        holder.releaseDate.setVisibility(data.hasReleaseDate() ? View.VISIBLE : View.GONE);

        final Context context = holder.itemView.getContext();

        if (data.hasReleaseDate()){
            // if release date before current time, use "released". Use "releases" if in the future
            final String releaseStr = data.isReleased() ? context.getString(R.string.released) : context.getString(R.string.releases);
            holder.releaseDate.setText(String.format("%s : %s ",releaseStr, data.getReleaseDateString()));
        }

        // get the pricing
        holder.gameTitle.setText(data.getGameName());
        // set up the stores grid

        final StoreGridImageAdapter storeImageAdapter = (StoreGridImageAdapter) holder.storeGridView.getAdapter();


        if (storeImageAdapter != null) {
            storeImageAdapter.clear();
            storeImageAdapter.addAll(data.getStoreEntryViewModels());
        }

        holder.gameThumbImage.loadImageFromUrl(data.getThumbnailURL());

        holder.priceView.setSalePriceText(data.getLowestSalePriceString());
        holder.priceView.setRetailPriceText(data.getHighestNormalPrice());

        //TODO: look about moving that
        final String savingString = context.getString(R.string.saving_short);
        final String htmlString = String.format("<strong> %s%% </strong><br/><small>%s</small>", data.getSavingString(), savingString);
        holder.savingsView.setText(Html.fromHtml(htmlString));

        // these will auto unsubscribe when the view holder is detached.
        Observable<DealViewHolder.DealAction> optionsClickListener = holder.getOptionsLayoutClickListener().share().takeUntil(RxView.detaches(holder.itemView));

        optionsClickListener.filter(dealType -> dealType == DealViewHolder.DealAction.SHARE)
                            .map(dealAction -> holder.getAdapterPosition())
                            .doOnNext(onClickShare::onNext)
                            .subscribe();

        optionsClickListener.filter(dealType -> dealType == DealViewHolder.DealAction.EDIT)
                            .map(dealAction -> holder.getAdapterPosition())
                            .doOnNext(onClickEdit::onNext)
                            .subscribe();

        optionsClickListener.filter(dealType -> dealType == DealViewHolder.DealAction.OPEN)
                            .map(dealAction -> holder.getAdapterPosition())
                            .doOnNext(onClickOpen::onNext)
                            .subscribe();

        RxView.clicks(holder.itemView).doOnNext(aVoid -> setCellSelected(holder.getAdapterPosition())).subscribe();
    }

    @Override public int getItemCount() {
        return dealModelList.size();
    }

    public void addAll(List<DealAdapterModel> compactDeals) {
        dealModelList.addAll(compactDeals);
        notifyDataSetChanged();
    }

    public void add(DealAdapterModel dealCellModel) {
        int indexToAddAt = lastRemovedIndex > 0 ? lastRemovedIndex : 0;
        dealModelList.add(indexToAddAt, dealCellModel);
        notifyItemInserted(indexToAddAt);
    }

    public void clear() {
        dealModelList.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return dealModelList.isEmpty();
    }

    private void setCellSelected(int selectedIndex) {
        if (currentSelectedIndex == selectedIndex) {
            selectedIndex = -1;
        }

        int oldSelectedIndex = currentSelectedIndex;
        currentSelectedIndex = selectedIndex;
        notifyItemChanged(oldSelectedIndex);
        notifyItemChanged(selectedIndex);
    }

    public Observable<Integer> onClickShareDeal() {
        return onClickShare;
    }

    public Observable<Integer> onClickOpenDeal() {
        return onClickOpen;
    }

    public Observable<Integer> onClickEditDeal() {
        return onClickEdit;
    }

    public void remove(int integer) {
        // if we are removing the selected view unselect the view
        if(currentSelectedIndex == integer){
            currentSelectedIndex = -1;
        }

        lastRemovedIndex = integer;

        dealModelList.remove(integer);
        notifyItemRemoved(integer);
    }

    public static class DealViewHolder extends RecyclerView.ViewHolder implements IDealCell {
        private static final List<OptionsLayout.Option> OPTIONS =
            Arrays.asList(new OptionsLayout.Option(R.drawable.ic_storelink_grey600_36dp), new OptionsLayout.Option(R.drawable.ic_eye_grey600_36dp),
                new OptionsLayout.Option(R.drawable.ic_share_variant_grey600_36dp));

        @BindView(R.id.dealGameTitle) TextView gameTitle;
        @BindView(R.id.gameThumb) WebImageView gameThumbImage;
        @BindView(R.id.storeGridView) GridView storeGridView;
        @BindView(R.id.priceHolder) PriceView priceView;
        @BindView(R.id.releaseDate) TextView releaseDate;
        @BindView(R.id.savingPercentage) TextView savingsView;
        @BindView(R.id.dealContainer) LinearLayout dealContainer;
        @BindView(R.id.optionsLayout) OptionsLayout optionsLayout;

        private final int mSelectedColor;

        public DealViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, itemView);

            mSelectedColor = ContextCompat.getColor(view.getContext(), R.color.primary_color_light_alpha);
            optionsLayout.setVisibility(View.GONE);
            optionsLayout.setOptions(OPTIONS);

            // these lines allow the list click listener to work
            storeGridView.setClickable(false);
            storeGridView.setEnabled(false);
            storeGridView.setFocusable(false);
            storeGridView.setFocusableInTouchMode(false);
            storeGridView.setAdapter(new StoreGridImageAdapter(view.getContext(), new ArrayList<>()));

            storeGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") @Override public void onGlobalLayout() {
                    int viewHeight = storeGridView.getHeight();
                    if (storeGridView.getChildCount() > 0) {
                        int childHeight = storeGridView.getChildAt(0).getHeight();
                        int maxRows = viewHeight / childHeight;

                        int maxItems = maxRows * storeGridView.getNumColumns();
                        StoreGridImageAdapter imageAdapter = (StoreGridImageAdapter) storeGridView.getAdapter();
                        imageAdapter.setItemLimit(maxItems);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            storeGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            storeGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                }
            });
        }

        public void setSelected(boolean selected) {
            //TODO: Animation in a view holder aren't the greatest thing. If you create animations, recycler view will fail to keep it as a child but won't be able to recycle (transient state). Wrong things may happen. It is important to coordinate these with ItemAnimator if possible. Change animations: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ItemAnimator.html#setSupportsChangeAnimations(boolean) need to be turned off for this to work correctly

            optionsLayout.setVisibility(selected ? View.VISIBLE : View.GONE);
            if (optionsLayout.getAnimation() != null) {
                optionsLayout.getAnimation().cancel();
            }
            if (selected) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                optionsLayout.setAnimation(alphaAnimation);
                alphaAnimation.setDuration(250);
                alphaAnimation.start();
            }
            itemView.setBackgroundColor(selected ? mSelectedColor : 0);
        }

        Observable<DealAction> getOptionsLayoutClickListener() {
            return optionsLayout.getClickOptionObservable().map(integer -> {
                switch (integer) {
                    case 0:
                        return DealAction.OPEN;
                    case 1:
                        return DealAction.EDIT;
                    case 2:
                        return DealAction.SHARE;
                    default:
                        throw new IllegalStateException("unknown deal action");
                }
            });
        }

        @Override public Bitmap getSharingBitmap() {
            dealContainer.setBackgroundColor(ContextCompat.getColor(gameTitle.getContext(), R.color.share_background_color));
            dealContainer.buildDrawingCache();
            dealContainer.setBackgroundColor(0);
            return dealContainer.getDrawingCache();
        }

        public enum DealAction {
            SHARE,
            EDIT,
            OPEN
        }
    }

    public interface IDealCell {

        Bitmap getSharingBitmap();
    }
}
