package com.rockspin.bargainbits.ui.deals

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.rockspin.bargainbits.di.annotations.ActivityScope
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

        val gridView = holder.binding.storeGridView
        gridView.isEnabled = false

        gridView.adapter = StoreImageAdapter(parent.context)
        gridView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val viewHeight = gridView.height
                if (gridView.childCount > 0) {
                    val childHeight = gridView.getChildAt(0).height
                    val maxRows = viewHeight / childHeight

                    val maxItems = maxRows * gridView.numColumns
                    val adapter = gridView.adapter as StoreImageAdapter
                    adapter.itemLimit = maxItems

                    gridView.viewTreeObserver.removeOnGlobalLayoutListener(this)
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

        val adapter = binding.storeGridView.adapter as StoreImageAdapter
        adapter.clear()
        adapter.addAll(viewModel.storeImageUrls)
    }

    override fun getItemCount(): Int {
        return viewModels.size
    }

    class ViewHolder(val binding: ItemGameDealBinding): RecyclerView.ViewHolder(binding.root)
}