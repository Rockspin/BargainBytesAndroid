package com.rockspin.apputils.cache.interfaces;

import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.apputils.cache.ObjectAndSize;
import java.io.Serializable;

/**
 * Store Objects in a memory backed Key value database.
 */
public interface IMemoryCache {

    /**
     * Retrieve an item from the memory cache.
     *
     * @param pKey the key we want to cache this object with.
     * @param pClass the type of object we want to retrieve. used to compute keys that ensure type safety.
     * * @return the object we want to retrieve.
     * @return an optional of the object in the cache. or absent.
     */
    <T extends Serializable> Optional<T> get(final String pKey, final Class<T> pClass);

    /**
     * Save an object to the memory cache.
     *
     * @param pKey the key we want to cache this object with.
     * @param pValue the value we want to cache.
     * @param <T> the type of object we are saving.
     * @return the object that has just been cached.
     */
    <T extends Serializable> ObjectAndSize<T> save(final String pKey, ObjectAndSize<T> pValue);

    /**
     * Clear all data.
     */
    void clearAllData();

    /**
     * Delete a key from the database. The type is needed as to ensure type saftey in the database we create complex keys from the class and the type of object we are caching.
     *
     * @param pKey key to delete
     * @param pClass type of object we are deleting
     */
    <T extends Serializable> void delete(String pKey, final Class<T> pClass);
}
