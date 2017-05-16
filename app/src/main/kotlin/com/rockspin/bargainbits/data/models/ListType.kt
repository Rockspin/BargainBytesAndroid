package com.rockspin.bargainbits.data.models

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A type to use with GSON for representing array of items
 */
class ListType<T>(private val wrapped: Class<T>): ParameterizedType {

    override fun getRawType(): Type {
        return List::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(wrapped)
    }
}