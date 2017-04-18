package com.rockspin.apputils.cache.interfaces;

import java.io.Serializable;
import rx.Single;

/**
 * Cache request.
 */
public interface ICacheReader{

    /**
     * retrieves values stored in cache.
     * @return results form server.
     * @param pKey key value to fetch.
     * @param pClass class type to fetch.
     */
    <T extends Serializable> Single<T> runRequest(final String pKey, Class<T> pClass);

}
