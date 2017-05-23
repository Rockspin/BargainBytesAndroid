package com.rockspin.bargainbits.ui.deals

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import com.rockspin.bargainbits.ui.views.WebImageView

/**
 * Created by valentin.hinov on 28/05/2017.
 */
class StoreImageAdapter(context: Context): ArrayAdapter<String>(context, 0, mutableListOf<String>()) {

    var itemLimit = -1

    override fun getCount(): Int {
        val baseCount = super.getCount()
        return if (itemLimit != -1 && itemLimit < baseCount) itemLimit else baseCount
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val webImageView: WebImageView
        if (convertView == null) {
            webImageView = WebImageView(context)
            webImageView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            webImageView.isClickable = false
        } else {
            webImageView = convertView as WebImageView
        }

        val url = getItem(position)
        webImageView.loadImageFromUrl(url)

        return webImageView
    }
}