package com.rockspin.apputils.cache.helper;

import com.fernandocejas.arrow.checks.Preconditions;
import com.rockspin.apputils.cache.ObjectAndSize;
import com.rockspin.apputils.cache.interfaces.ICacheWriter;
import com.rockspin.apputils.cache.interfaces.IDiscDatabase;
import com.rockspin.apputils.cache.interfaces.IMemoryCache;
import java.io.IOException;
import java.io.Serializable;
import rx.Single;

/**
 * Caches result.
 */
public class SimpleCacheWriter implements ICacheWriter {
    private final IMemoryCache mMemoryCache;
    private final IDiscDatabase mDiscDatabase;

    /**
     * Caches result.
     *
     * @param mMemoryCache memory cache we are writing to.
     * @param mDiscDatabase disc cache we are writing to.
     */
    public SimpleCacheWriter(final IMemoryCache mMemoryCache, final IDiscDatabase mDiscDatabase) {
        Preconditions.checkNotNull(mMemoryCache, "mMemoryCache cannot be null");
        Preconditions.checkNotNull(mDiscDatabase, "mMemoryCache cannot be null");
        this.mMemoryCache = mMemoryCache;
        this.mDiscDatabase = mDiscDatabase;
    }

    @Override public <T extends Serializable> Single<T> cacheValue(final String pKey, final T pValueToCache) {
        Preconditions.checkNotNull(pKey, "key cannot be null");
        Preconditions.checkNotNull(pValueToCache, "pResult cannot be null");

        return Single.create(singleSubscriber -> {
            try {
                Integer size = mDiscDatabase.save(pKey, pValueToCache);
                // save it in the memory cache with correct size
                mMemoryCache.save(pKey, new ObjectAndSize<>(pValueToCache, size));
                singleSubscriber.onSuccess(pValueToCache);
            } catch (IOException e) {
                singleSubscriber.onError(e);
            }
        });
    }

    @Override public Single<Boolean> deleteEntry(final String pKey, final Class pClass) {
        mMemoryCache.delete(pKey, pClass);
        return Single.create(singleSubscriber -> {
            try {
                singleSubscriber.onSuccess(mDiscDatabase.delete(pKey, pClass));
            } catch (IOException e) {
                singleSubscriber.onError(e);
            }
        });
    }
}
