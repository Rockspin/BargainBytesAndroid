package com.rockspin.bargainbits.deals

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class DealsAdapter(private val inflater: LayoutInflater): RecyclerView.Adapter<DealViewHolder>() {
    private val items: MutableList<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DealViewHolder = DealViewHolder(inflater, parent)

    override fun onBindViewHolder(holder: DealViewHolder?, position: Int) {
        holder?.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<Game>) {
        this.items.addAll(items)
    }
}