/*
 * Copyright (c) Rockspin 2015.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.watch_list

import android.os.Parcel
import android.os.Parcelable
import com.rockspin.bargainbits.data.models.GameSearchResult
import com.rockspin.bargainbits.data.models.cheapshark.CompactDeal
import paperparcel.PaperParcel
import java.io.Serializable

@PaperParcel
data class WatchedItem(
    val gameName: String,
    val gameId: String,
    // TODO - change to Double without breaking old watch list entries?
    val watchedPrice: Float
) : Parcelable, Serializable {

    val hasCustomWatchPrice: Boolean
        get() = watchedPrice >= 0

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelWatchedItem.writeToParcel(this, dest, flags)
    }

    companion object {
        @JvmField val CREATOR = PaperParcelWatchedItem.CREATOR

        internal const val serialVersionUID = 1L

        fun from(game: GameSearchResult): WatchedItem {
            return WatchedItem(game.name, game.gameID, game.cheapestPrice.toFloat())
        }

        fun from(compactDeal: CompactDeal): WatchedItem {
            return WatchedItem(compactDeal.gameName, compactDeal.gameId, compactDeal.lowestSalePrice)
        }
    }
}
