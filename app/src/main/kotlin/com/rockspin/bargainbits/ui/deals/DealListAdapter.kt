package com.rockspin.bargainbits.ui.deals

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.rockspin.apputils.di.annotations.ActivityScope
import com.rockspin.bargainbits.R
import com.rockspin.bargainbits.databinding.ItemGameDealBinding
import com.rockspin.bargainbits.util.visible
import javax.inject.Inject

/**
 * Created by valentin.hinov on 26/05/2017.
 */
class DealListAdapter @Inject internal constructor(@ActivityScope private val layoutInflater: LayoutInflater)
    : RecyclerView.Adapter<DealListAdapter.ViewHolder>() {

    var viewModels = emptyList<DealViewEntry>()
        set(value) {
            val set = value.subtract(field)
            if (set.isEmpty()) {
                return
            }
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemGameDealBinding.inflate(layoutInflater, parent, false)
        val holder = ViewHolder(itemBinding)

        val storeOptionRV = holder.binding.storeOptionRecycler
        storeOptionRV.adapter = StoreOptionsRecyclerAdapter(layoutInflater)
        storeOptionRV.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (storeOptionRV.childCount > 0) {
                    // calculate max span
                    val childWidth  = storeOptionRV.getChildAt(0).width
                    val viewWidth = storeOptionRV.width
                    val spanCount = Math.floor(viewWidth / childWidth.toDouble()).toInt()
                    val layoutManager = storeOptionRV.layoutManager as GridLayoutManager
                    layoutManager.spanCount = spanCount

                    val viewHeight = storeOptionRV.height
                    val childHeight = storeOptionRV.getChildAt(0).height
                    val maxRows = viewHeight / childHeight
                    val maxItems = maxRows * spanCount
                    val adapter = storeOptionRV.adapter as StoreOptionsRecyclerAdapter
                    adapter.itemLimit = maxItems

                    storeOptionRV.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewModel = viewModels[position]
        val binding = holder.binding

        val context = binding.root.context

        binding.dealTitle.text = viewModel.title

        val hasSubtitle = viewModel.subtitle != null
        if (hasSubtitle) {
            binding.dealSubtitle.text = viewModel.subtitle
        }

        binding.dealSubtitle.visible = hasSubtitle
        binding.gameThumb.loadImageFromUrl(viewModel.imageUrl)

        binding.savingPercentage.text = Html.fromHtml(context.getString(R.string.deal_list_savings_percent, viewModel.savingsPercentage))
        binding.priceHolder.setRetailPriceText(viewModel.retailPrice)
        binding.priceHolder.setSalePriceText(viewModel.salePrice)

        val adapter = binding.storeOptionRecycler.adapter as StoreOptionsRecyclerAdapter
        adapter.storeUrls = viewModel.storeImageUrls
    }

    override fun getItemCount(): Int = viewModels.size

    class ViewHolder(val binding: ItemGameDealBinding): RecyclerView.ViewHolder(binding.root)
}