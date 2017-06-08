package com.rockspin.bargainbits.ui.deals

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rockspin.bargainbits.databinding.ItemDealStoreChoiceBinding

class StoreOptionsRecyclerAdapter(private val layoutInflater: LayoutInflater): RecyclerView.Adapter<StoreOptionsRecyclerAdapter.ViewHolder>() {

    var storeUrls = emptyList<String>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var itemLimit = -1
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        val baseCount = storeUrls.size
        return if (itemLimit != -1 && itemLimit < baseCount) itemLimit else baseCount
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemBinding = ItemDealStoreChoiceBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.storeImage.loadImageFromUrl(storeUrls[position])
    }

    class ViewHolder(val binding: ItemDealStoreChoiceBinding): RecyclerView.ViewHolder(binding.root)
}