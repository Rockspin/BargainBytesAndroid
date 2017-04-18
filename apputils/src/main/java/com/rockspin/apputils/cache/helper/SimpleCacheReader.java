package com.rockspin.apputils.cache.helper;

import com.fernandocejas.arrow.checks.Preconditions;
import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.apputils.cache.ObjectAndSize;
import com.rockspin.apputils.cache.interfaces.ICacheReader;
import com.rockspin.apputils.cache.interfaces.IDiscDatabase;
import com.rockspin.apputils.cache.interfaces.IMemoryCache;
import java.io.IOException;
import java.io.Serializable;
import rx.Single;

/**
 * Caches result.
 */
public class SimpleCacheReader implements ICacheReader {

    private final IMemoryCache mMemoryCache;
    private final IDiscDatabase mDiscDatabase;

    /**
     * Caches result.
     */
    public SimpleCacheReader(final IMemoryCache mMemoryCache, final IDiscDatabase mDiscDatabase) {
        Preconditions.checkNotNull(mMemoryCache, "mMemoryCache cannot be null");
        Preconditions.checkNotNull(mDiscDatabase, "mMemoryCache cannot be null");
        this.mMemoryCache = mMemoryCache;
        this.mDiscDatabase = mDiscDatabase;
    }

    @Override public <T extends Serializable> Single<T> runRequest(final String pKey, final Class<T> pClass) {
        Preconditions.checkNotNull(pKey, "key cannot be null");
        Preconditions.checkNotNull(pClass, "pClass cannot be null");
        // fetch from memory first then fallback to disc.
        final Optional<T> optionalMemory = mMemoryCache.get(pKey, pClass);
        if (optionalMemory.isPresent()) {
            return Single.just(optionalMemory.get());
        }

        return Single.create(singleSubscriber -> {
            try {
                final ObjectAndSize<T> discResult = mDiscDatabase.get(pKey, pClass);
                mMemoryCache.save(pKey, new ObjectAndSize<>(discResult.mValue, discResult.mSizeInBytes));
                singleSubscriber.onSuccess(discResult.mValue);
            } catch (IOException e) {
                singleSubscriber.onError(e);
            }
        });
    }
}
