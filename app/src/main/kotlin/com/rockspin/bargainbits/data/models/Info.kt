package com.rockspin.bargainbits.data.models

import com.google.gson.annotations.SerializedName

data class Info (
    @SerializedName("title")
    val gameName: String,
    @SerializedName("thumb")
    val thumbnailURL: String? = null
) {
    var gameId: String? = null
}
