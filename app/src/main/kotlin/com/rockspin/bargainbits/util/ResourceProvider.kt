package com.rockspin.bargainbits.util

import android.support.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String
}