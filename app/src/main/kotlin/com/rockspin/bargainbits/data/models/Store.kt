/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.data.models

import android.support.annotation.VisibleForTesting
import com.google.gson.annotations.SerializedName

/**
 * Store object returned from API.
 */
data class Store (
    @SerializedName("storeID")
    val storeId: String,
    @SerializedName("storeName")
    val storeName: String,
    @SerializedName("isActive")
    private val isActiveInt: Int,
    @SerializedName("images")
    private val images: Images
) {

    val isActive: Boolean
    get() = isActiveInt == 1

    val imageUrl: String
    get() = images.logoUrl

    @VisibleForTesting data class Images(
        @SerializedName("banner")
        val bannerUrl: String,
        @SerializedName("logo")
        val logoUrl: String,
        @SerializedName("icon")
        val iconUrl: String
    )
}
