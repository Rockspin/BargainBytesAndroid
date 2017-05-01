package com.rockspin.apputils.cache.helper;

import com.fernandocejas.arrow.checks.Preconditions;
import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.apputils.cache.ObjectAndSize;
import com.rockspin.apputils.cache.interfaces.ICacheReader;
import com.rockspin.apputils.cache.interfaces.IDiscDatabase;
import com.rockspin.apputils.cache.interfaces.IMemoryCache;
import io.reactivex.Single;
import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

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

    @Override public <T extends Serializable> Single<T> getItem(final String key, final Class<T> clazz) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(clazz, "pClass cannot be null");
        // fetch from memory first then fallback to disc.
        final Optional<T> optionalMemory = mMemoryCache.get(key, clazz);
        if (optionalMemory.isPresent()) {
            return Single.just(optionalMemory.get());
        }

        return Single.create(singleSubscriber -> {
            try {
                final Optional<ObjectAndSize<T>> diskResult = mDiscDatabase.getSync(key, clazz);
                if (diskResult.isPresent()) {
                    final ObjectAndSize<T> result = diskResult.get();
                    mMemoryCache.save(key, new ObjectAndSize<>(result.mValue, result.mSizeInBytes));
                    singleSubscriber.onSuccess(result.mValue);
                } else {
                    singleSubscriber.onError(new NoSuchElementException("Could not find item of key " + key + " in cache."));
                }
            } catch (IOException e) {
                singleSubscriber.onError(e);
            }
        });
    }
}
