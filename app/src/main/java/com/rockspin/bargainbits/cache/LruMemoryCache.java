package com.rockspin.bargainbits.cache;

import android.support.v4.util.LruCache;
import com.fernandocejas.arrow.checks.Preconditions;
import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.bargainbits.cache.interfaces.IMemoryCache;
import java.io.Serializable;

/**
 * Memory Backed Database.
 */
public final class LruMemoryCache implements IMemoryCache {

    private final LruCache<String, ObjectAndSize<Serializable>> mLruCache;

    public LruMemoryCache(final float pSizeInMB) {
        mLruCache = new LruCache<String, ObjectAndSize<Serializable>>((int) (1024 * 1024 * pSizeInMB)) {

            @Override protected int sizeOf(String key, ObjectAndSize<Serializable> value) {
               return value.mSizeInBytes;
            }
        };
    }

    @Override public synchronized <T extends Serializable> Optional<T> get(final String pKey, final Class<T> pClass) {
        Preconditions.checkNotNull(pKey, "Can't fetch use null key");
        Preconditions.checkNotNull(pClass, "Can't fetch use null pClass");
        final ObjectAndSize<Serializable> serializable = mLruCache.get(ComplexKey.getComplexTypeSafeKey(pKey, pClass));
        final Optional<ObjectAndSize<Serializable>> value = Optional.fromNullable(serializable);
        if (value.isPresent()) {
            // type safety should be guaranteed by the keys see @Class ComplexKey.
            return Optional.of((T) value.get().mValue);
        }
        return Optional.absent();
    }

    @Override public synchronized <T extends Serializable> ObjectAndSize<T> save(final String pKey, final ObjectAndSize<T> pValue) {
        Preconditions.checkNotNull(pKey, "Can't use null key");
        Preconditions.checkNotNull(pValue, "Can't use null value");
        Preconditions.checkNotNull(pValue.mValue, "Can't use null in value");
        Preconditions.checkArgument(pValue.mSizeInBytes > 0, "Can't save object of zero size");
        // type safety should be guaranteed by the keys.
        mLruCache.put(ComplexKey.getComplexTypeSafeKey(pKey, pValue.mValue.getClass()), (ObjectAndSize<Serializable>) pValue);
        return pValue;
    }

    public void clearAllData() {
        mLruCache.evictAll();
    }

    @Override public <T extends Serializable> void delete(String pKey, final Class<T> pClass) {
        mLruCache.remove(ComplexKey.getComplexTypeSafeKey(pKey, pClass));
    }

}
