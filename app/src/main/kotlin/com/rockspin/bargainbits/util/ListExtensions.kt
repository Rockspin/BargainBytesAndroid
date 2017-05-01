package com.rockspin.bargainbits.util

/**
 * Created by valentin.hinov on 07/05/2017.
 */

fun <T> List<T>.withUpdatedItemAtIndex(index: Int, item: T): List<T> {
    val mutableList = this.toMutableList()
    mutableList[index] = item
    return mutableList
}