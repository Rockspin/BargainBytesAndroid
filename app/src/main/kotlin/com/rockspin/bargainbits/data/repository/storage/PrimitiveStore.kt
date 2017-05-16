package com.rockspin.bargainbits.data.repository.storage

/**
 * Class that deals with storing primitives on disk
 */
interface PrimitiveStore {

    fun storeString(key: String, item: String)
    fun getStoredString(key: String): String?

    fun storeStringSet(key: String, set: Set<String>)
    fun getStoredStringSet(key: String): Set<String>
}