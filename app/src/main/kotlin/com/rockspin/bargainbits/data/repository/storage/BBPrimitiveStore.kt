package com.rockspin.bargainbits.data.repository.storage

import android.content.SharedPreferences

class BBPrimitiveStore(private val sharedPreferences: SharedPreferences): PrimitiveStore {

    override fun storeString(key: String, item: String) {
        sharedPreferences.edit().putString(key, item).apply()
    }

    override fun getStoredString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun storeStringSet(key: String, set: Set<String>) {
        sharedPreferences.edit().putStringSet(key, set).apply()
    }

    override fun getStoredStringSet(key: String): Set<String> {
        return sharedPreferences.getStringSet(key, emptySet())
    }
}