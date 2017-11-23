package com.rockspin.bargainbits.deals


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView

import com.rockspin.bargainbits.R

class DealViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup?
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_deal, parent)) {
    @BindView(R.id.deal_item_name) lateinit var nameTextView: TextView
    @BindView(R.id.deal_item_rating) lateinit var ratingTextView: TextView
    @BindView(R.id.deal_item_was) lateinit var wasTextView: TextView
    @BindView(R.id.deal_item_now) lateinit var nowTextView: TextView

    fun bind(game: Game) {
        val res = itemView.context.resources
        nameTextView.text = game.title
        ratingTextView.text = res.getString(R.string.deal_percent, game.deals[0].dealRating)
        wasTextView.text = res.getString(R.string.amount, game.deals[0].wasPrice)
        nowTextView.text = res.getString(R.string.amount, game.deals[0].nowPrice)
    }
}
