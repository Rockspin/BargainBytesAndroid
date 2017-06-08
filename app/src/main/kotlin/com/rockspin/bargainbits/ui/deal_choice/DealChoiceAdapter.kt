package com.rockspin.bargainbits.ui.deal_choice

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.apputils.di.annotations.ActivityScope
import com.rockspin.bargainbits.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_deal_store_option.view.*
import javax.inject.Inject

class DealChoiceAdapter @Inject internal constructor(@ActivityScope private val layoutInflater: LayoutInflater)
    : RecyclerView.Adapter<DealChoiceAdapter.ViewHolder>() {

    private val itemClicked = PublishSubject.create<Int>()
    val onItemClicked: Observable<Int> = itemClicked

    var modelList = emptyList<DealChoiceModel>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DealChoiceAdapter.ViewHolder {
        val rootView = layoutInflater.inflate(R.layout.item_deal_store_option, parent, false)
        val viewHolder = ViewHolder(rootView)

        rootView.clicks()
            .map { viewHolder.adapterPosition }
            .subscribe {
                itemClicked.onNext(it)
            }

        return viewHolder
    }

    override fun onBindViewHolder(holder: DealChoiceAdapter.ViewHolder, position: Int) {
        val model = modelList[position]
        val itemView = holder.itemView

        itemView.storeName.text = model.storeName

        itemView.storeImage.clear()
        itemView.storeImage.loadImageFromUrl(model.imageUrl)
        itemView.storeImage.setImageContentDescription(model.storeName)

        if (model.retailPrice == null) {
            itemView.priceHolder.setSinglePriceMode()
        } else {
            itemView.priceHolder.setRetailPriceText(model.retailPrice)
        }

        itemView.priceHolder.setSalePriceText(model.salePrice)

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    fun setDealChoiceModels(models: List<DealChoiceModel>) {
        modelList = models
        notifyDataSetChanged()
    }

    class ViewHolder(root: View) : RecyclerView.ViewHolder(root)
}