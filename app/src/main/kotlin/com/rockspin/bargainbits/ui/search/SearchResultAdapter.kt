package com.rockspin.bargainbits.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.rockspin.apputils.di.annotations.ActivityScope
import com.rockspin.bargainbits.databinding.ItemSearchResultBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SearchResultAdapter @Inject internal constructor(@ActivityScope private val layoutInflater: LayoutInflater)
    : RecyclerView.Adapter<SearchResultAdapter.SearchResultsAdapterViewHolder>() {

    private val itemSelected = PublishSubject.create<Int>()

    val onItemSelected: Observable<Int> = itemSelected

    var viewModels = emptyList<ResultViewModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsAdapterViewHolder {
        val itemBinding = ItemSearchResultBinding.inflate(layoutInflater, parent, false)
        val holder = SearchResultsAdapterViewHolder(itemBinding)

        holder.itemView.clicks()
            .subscribe { itemSelected.onNext(holder.adapterPosition) }

        return holder
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    override fun onBindViewHolder(holder: SearchResultsAdapterViewHolder, position: Int) {
        val viewModel = viewModels[position]

        val binding = holder.binding

        binding.gameThumb.clear()
        binding.gameThumb.loadImageFromUrl(viewModel.thumbUrl)
        binding.cheapestPrice.setSalePriceText(viewModel.price)
        binding.gameTitle.text = viewModel.name
    }

    class SearchResultsAdapterViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root)
}
