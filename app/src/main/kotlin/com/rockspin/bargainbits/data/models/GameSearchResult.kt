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
data class GameSearchResult(
    @SerializedName("gameID")
    val gameID: String,
    @SerializedName("steamAppID")
    val steamAppID: String? = null,
    @SerializedName("cheapest")
    val cheapestPrice: Double, // cheapestPrice price the game has ever been, in USD
    @SerializedName("cheapestDealID")
    val cheapestDealID: String? = null,
    @SerializedName("external")
    val name: String,
    @SerializedName("thumb")
    val thumbnailUrl: String
    )
