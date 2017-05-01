package com.rockspin.bargainbits.data.repository.storage

import com.google.gson.Gson
import com.rockspin.bargainbits.data.models.ListType

/**
 * Created by valentin.hinov on 07/05/2017.
 */
class BBObjectStore(private val gson: Gson, private val primitiveStore: PrimitiveStore) : ObjectStore {

    override fun <T> storeList(key: String, items: List<T>) {
        val json = gson.toJson(items)
        primitiveStore.storeString(key, json)
    }

    override fun <T> getStoredList(key: String, itemClass: Class<T>): List<T> {
        val json = primitiveStore.getStoredString(key) ?: return emptyList()
        val list = gson.fromJson<List<T>>(json, ListType(itemClass))
        return list
    }
}
