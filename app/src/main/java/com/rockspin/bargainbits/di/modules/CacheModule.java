package com.rockspin.bargainbits.di.modules;

import android.content.Context;
import com.rockspin.apputils.cache.LruDiscCache;
import com.rockspin.apputils.cache.LruMemoryCache;
import com.rockspin.apputils.cache.helper.SimpleCacheReader;
import com.rockspin.apputils.cache.helper.SimpleCacheWriter;
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

    public IDiscDatabase getDiscCache(final Context pContext) {
        final File file = new File(pContext.getFilesDir()
                                           .getPath() + DATABASE_NAME);
        return new LruDiscCache(file, DATABASE_SIZE, DATABASE_VERSION);
    }

    @Provides @Singleton public SimpleCacheReader getCacheReader(@ApplicationScope Context pContext) {
        return new SimpleCacheReader(mMemoryCache, getDiscCache(pContext));
    }

    @Provides @Singleton public SimpleCacheWriter getCacheWriter(@ApplicationScope Context pContext) {
        return new SimpleCacheWriter(mMemoryCache, getDiscCache(pContext));
    }
}
