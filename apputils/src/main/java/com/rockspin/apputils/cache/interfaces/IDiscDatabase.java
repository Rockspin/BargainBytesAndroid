package com.rockspin.apputils.cache.interfaces;

import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.apputils.cache.ObjectAndSize;
import java.io.IOException;
import java.io.Serializable;

/**
 * A database that the app used.
 */
public interface IDiscDatabase {

    /**
     * Fetch an object from the disc cache.
     * @param pKeyToFetch key of object to fetch
     * @param pClass the type of object we want to retrieve. used to compute keys that ensure type safety.
     * @param <T> the type of object we are returning.
     * @return a future that will return the value we are caching + its size on disc in bytes. Completes after object is in cache.
     */
    <T extends Serializable> ObjectAndSize<T> get(final String pKeyToFetch, final Class<T> pClass) throws IOException;


    /**
     * Fetch an object from the disc cache.
     * @param pKeyToFetch key of object to fetch
     * @param pClass the type of object we want to retrieve. used to compute keys that ensure type safety.
     * @return a future that will return the value we are caching + its size on disc in bytes. Completes after object is in cache.
     */
    <T extends Serializable> Optional<ObjectAndSize<T>> getSync(final String pKeyToFetch, final Class<T> pClass) throws IOException;
    /**
     *
     * Save the object with key
     *
     * @param pKey key to save.
     * @param pValue value to save.
     * @param <T> the type of object we are saving.
     * @return the size in bytes of the object one it is written to disc.
     */
    <T extends Serializable> Integer save(final String pKey, final T pValue) throws IOException;

    /**
     * Delete all data stored in this cache.
     */
    void clearAllData() throws IOException;

    /**
     * Delete an item from the disc cache.
     * @param pKey key to delete.
     * @return a futrue that will complete when item is deleted.
     */
    Boolean delete(final String pKey, final Class pClass) throws IOException;
}
