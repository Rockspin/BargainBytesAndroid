package com.rockspin.bargainbits.di.modules;

import android.content.Context;
import com.rockspin.apputils.cache.LruDiscCache;
import com.rockspin.apputils.cache.LruMemoryCache;
import com.rockspin.apputils.cache.helper.SimpleCacheReader;
import com.rockspin.apputils.cache.helper.SimpleCacheWriter;
import com.rockspin.apputils.cache.interfaces.ICacheReader;
import com.rockspin.apputils.cache.interfaces.ICacheWriter;
import com.rockspin.apputils.cache.interfaces.IDiscDatabase;
import com.rockspin.apputils.di.annotations.ApplicationScope;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Singleton;

@Module public class CacheModule {

    private static final int DATABASE_VERSION = 1;
    private static final int DATABASE_SIZE = 10;
    private static final String DATABASE_NAME = "DATABASE_NAME";

    private final LruMemoryCache mMemoryCache = new LruMemoryCache(1);

    @Provides @Singleton ICacheReader getCacheReader(@ApplicationScope Context pContext) {
        return new SimpleCacheReader(mMemoryCache, getDiscCache(pContext));
    }

    @Provides @Singleton ICacheWriter getCacheWriter(@ApplicationScope Context pContext) {
        return new SimpleCacheWriter(mMemoryCache, getDiscCache(pContext));
    }

    private IDiscDatabase getDiscCache(final Context pContext) {
        final File file = new File(pContext.getFilesDir()
            .getPath() + DATABASE_NAME);
        return new LruDiscCache(file, DATABASE_SIZE, DATABASE_VERSION);
    }
}
