package com.rockspin.bargainbits.cache;

import android.annotation.SuppressLint;
import com.fernandocejas.arrow.checks.Preconditions;
import com.fernandocejas.arrow.optional.Optional;
import com.jakewharton.disklrucache.DiskLruCache;
import com.rockspin.bargainbits.cache.interfaces.IDiscDatabase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Memory database backed by disc cache
 */
@SuppressLint("NewApi")
public final class LruDiscCache implements IDiscDatabase {
    private final File mCacheDirectory;
    private final int mCacheSize;
    private final int mDatabaseVersion;

    /**
     * A disc cache that store serializable items in an LRU cache.
     *
     * @param pDirectoryName The file where we store the database.
     * @param pDiscCacheSizeMB the maximum size of the cache in MB.
     * @param pDatabaseVersion the database version of the app. when this is upgraded the database is deleted and recreated.
     */
    public LruDiscCache(final File pDirectoryName, final int pDiscCacheSizeMB, final int pDatabaseVersion) {
        mCacheSize = pDiscCacheSizeMB * 1024 * 1024;
        mCacheDirectory = pDirectoryName;
        mDatabaseVersion = pDatabaseVersion;
    }

    @Override public <T extends Serializable> ObjectAndSize<T> get(final String pKeyToFetch, final Class<T> pClass) throws IOException {
        Preconditions.checkNotNull(pKeyToFetch, "You cannot save a null key");
        Preconditions.checkNotNull(pClass, "You cannot save a null value");
        return getSync(pKeyToFetch, pClass).get();
    }

    @Override public <T extends Serializable> Optional<ObjectAndSize<T>> getSync(final String pKeyToFetch, final Class<T> pClass) throws IOException {
        Preconditions.checkNotNull(pKeyToFetch, "You cannot save a null key");
        Preconditions.checkNotNull(pClass, "You cannot save a null value");
        final String complexKey = ComplexKey.getComplexTypeSafeKey(pKeyToFetch, pClass);
        try (DiskLruCache diskLruCache = DiskLruCache.open(mCacheDirectory, mDatabaseVersion, 1, mCacheSize);
             DiskLruCache.Snapshot editor = diskLruCache.get(complexKey)) {

            if (editor == null) {
                return Optional.absent();
            }

            try(final InputStream in = editor.getInputStream(0)) {

                final byte[] byteArray = IOUtils.toByteArray(in);
                return Optional.of(new ObjectAndSize<>((T) SerializationUtils.deserialize(byteArray), byteArray.length));
            }
        }
    }

    @Override public <T extends Serializable> Integer save(final String pKey, final T pValue) throws IOException {
        Preconditions.checkNotNull(pKey, "You cannot save a null key");
        Preconditions.checkNotNull(pValue, "You cannot save a null value");
        final String complexKey = ComplexKey.getComplexTypeSafeKey(pKey, pValue.getClass());

        try (final DiskLruCache diskLruCache = DiskLruCache.open(mCacheDirectory, mDatabaseVersion, 1, mCacheSize)){
            final Optional<DiskLruCache.Editor> editor = Optional.of(diskLruCache.edit(complexKey));
            Preconditions.checkNotNull("could not create editor entry for " + pKey, editor);
            final OutputStream out = editor.get()
                                                           .newOutputStream(0);
            final byte[] byteArray = SerializationUtils.serialize(pValue);
            IOUtils.write(byteArray, out);
            editor.get()
                  .commit();
            return byteArray.length;
        }
    }

    @Override public void clearAllData() throws IOException {
        try (final DiskLruCache diskLruCache = DiskLruCache.open(mCacheDirectory, mDatabaseVersion, 1, mCacheSize)) {
            diskLruCache.delete();
        }
    }

    @Override public Boolean delete(final String pKey, final Class pClass) throws IOException {
        Preconditions.checkNotNull(pKey, "You cannot save a null key");
        final String complexKey = ComplexKey.getComplexTypeSafeKey(pKey, pClass);

        try (final DiskLruCache diskLruCache = DiskLruCache.open(mCacheDirectory, mDatabaseVersion, 1, mCacheSize)){
            return diskLruCache.remove(complexKey);
        }
    }
}
