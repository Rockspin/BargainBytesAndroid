package com.rockspin.bargainbits.data.repository.storage

/**
 * Class that deals with storing objects on disk.
 */
interface ObjectStore {

    fun <T> storeList(key: String, items: List<T>)

    /**
     * Returns a list of item stored with the given key or an empty list
     * if none exist
     */
    fun <T> getStoredList(key: String, itemClass: Class<T>): List<T>
}