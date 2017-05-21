package com.rockspin.bargainbits.data.repository.storage

/**
 * Class that deals with storing primitives on disk
 */
interface PrimitiveStore {

    fun storeString(key: String, item: String)
    fun getStoredString(key: String): String?

    fun storeLong(key: String, long: Long)

    /**
     * Returns a stored Long or 0 if not found
     */
    fun getStoredLong(key: String): Long

    fun storeStringSet(key: String, set: Set<String>)
    fun getStoredStringSet(key: String): Set<String>
}