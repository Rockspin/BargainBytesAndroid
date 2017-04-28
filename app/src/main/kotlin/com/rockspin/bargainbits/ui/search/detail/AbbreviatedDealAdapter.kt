package com.rockspin.bargainbits.ui.search.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.apputils.di.annotations.ActivityScope
import com.rockspin.bargainbits.databinding.ItemAbbreviatedDealBinding
import com.rockspin.bargainbits.util.visible
import com.rockspin.bargainbits.utils.DealUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AbbreviatedDealAdapter @Inject internal constructor(@ActivityScope private val layoutInflater: LayoutInflater)
    : RecyclerView.Adapter<AbbreviatedDealAdapter.ViewHolder>() {

    private val itemSelected = PublishSubject.create<Int>()

    val onItemClicked: Observable<Int> = itemSelected

    var viewModels = emptyList<AbbreviatedDealViewModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemAbbreviatedDealBinding.inflate(layoutInflater, parent, false)
        val holder = ViewHolder(itemBinding)

        holder.itemView.clicks()
            .subscribe { itemSelected.onNext(holder.adapterPosition) }

        return holder
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewModel = viewModels[position]

        val binding = holder.binding

        binding.storeName.text = viewModel.storeName

        binding.storeImage.clear()
        binding.storeImage.loadImageFromUrl(viewModel.storeImageUrl)

        val hasDeal = viewModel.hasDeal

        binding.savingPercentage.visible = hasDeal

        if (hasDeal) {
            binding.priceHolder.setRetailPriceText(viewModel.normalPrice)
            binding.savingPercentage.text = DealUtils.getFormattedSavingsString(holder.itemView.context, viewModel.savingPercentage!!)
        } else {
            binding.priceHolder.setSinglePriceMode()
        }

        binding.priceHolder.setSalePriceText(viewModel.dealPrice)
    }

    class ViewHolder(val binding: ItemAbbreviatedDealBinding) : RecyclerView.ViewHolder(binding.root)
}
