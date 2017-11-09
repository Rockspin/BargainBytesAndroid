package com.rockspin.bargainbits.ui.store_filter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.rockspin.bargainbits.di.annotations.ActivityScope
import com.rockspin.bargainbits.databinding.ItemStoreBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by valentin.hinov on 03/05/2017.
 */
class StoreFilterAdapter @Inject internal constructor(@ActivityScope private val layoutInflater: LayoutInflater)
    : RecyclerView.Adapter<StoreFilterAdapter.ViewHolder>() {

    private val itemToggled = PublishSubject.create<Pair<Int, Boolean>>()
    val onItemToggled: Observable<Pair<Int, Boolean>> = itemToggled

    private var storeList = emptyList<StoreViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StoreFilterAdapter.ViewHolder {
        val binding = ItemStoreBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder(binding)

        binding.storeNameSwitch.checkedChanges()
            .skipInitialValue()
            .subscribe {
                itemToggled.onNext(Pair(viewHolder.adapterPosition, it))
            }

        return viewHolder
    }

    override fun onBindViewHolder(holder: StoreFilterAdapter.ViewHolder, position: Int) {
        val binding = holder.binding
        val store = storeList[position]

        binding.storeImage.clear()
        binding.storeImage.loadImageFromUrl(store.imageUrl)
        binding.storeImage.setImageContentDescription(store.name)

        binding.storeNameSwitch.text = store.name
        binding.storeNameSwitch.isChecked = store.isUsed
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    fun showStoreList(newList: List<StoreViewModel>) {
        storeList = newList
        notifyDataSetChanged()
    }

    fun updateStoreAtIndex(index: Int, store: StoreViewModel) {
        val mutableList = storeList.toMutableList()
        mutableList[index] = store
        storeList = mutableList
    }

    class ViewHolder(val binding: ItemStoreBinding) : RecyclerView.ViewHolder(binding.root)
}