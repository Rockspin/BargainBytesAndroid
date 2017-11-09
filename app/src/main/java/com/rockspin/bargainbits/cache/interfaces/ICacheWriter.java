package com.rockspin.bargainbits.cache.interfaces;

import java.io.Serializable;
import rx.Single;

/**
 * Interface used to write to cache.
 */
public interface ICacheWriter{

    /**
     * Cache the result from a future.
     *
     * @param pKey@return future with server result.
     */
    <T extends Serializable> Single<T> cacheValue(final String pKey, final T pServerResult);

    /**
     * Delete an entry form the cache.
     * @param pKey key to delete.
     * @return a future that will complete when item is deleted from cache.
     */
    Single<Boolean> deleteEntry(final String pKey, final Class pClass);
}
