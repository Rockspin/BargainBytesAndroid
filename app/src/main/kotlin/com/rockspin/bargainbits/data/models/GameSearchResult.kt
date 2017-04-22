/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.models

import com.google.gson.annotations.SerializedName

/**
 * GameSearchResult object returned from the API.
 */
data class GameSearchResult(val gameID: String,
    val steamAppID: String? = null,
    @SerializedName("cheapest")
    val cheapestPrice: Float, // cheapestPrice price the game has ever been, in USD
    val cheapestDealID: String? = null,
    @SerializedName("external")
    val name: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String
    )
