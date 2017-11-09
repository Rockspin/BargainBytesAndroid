package com.rockspin.bargainbits.cache.interfaces;

import io.reactivex.Single;
import java.io.Serializable;

/**
 * Cache request.
 */
public interface ICacheReader {

    /**
     * retrieves values stored in cache.
     * @return results from cache
     * @param key key value to fetch.
     * @param clazz class type to fetch.
     */
    <T extends Serializable> Single<T> getItem(final String key, Class<T> clazz);
}
