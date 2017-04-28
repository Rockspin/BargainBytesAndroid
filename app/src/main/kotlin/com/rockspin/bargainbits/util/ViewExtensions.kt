package com.rockspin.bargainbits.util

import android.view.View

/**
 * Created by valentin.hinov on 22/04/2017.
 */

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }