package com.rockspin.bargainbits.di.modules;

import android.content.Context;
import com.rockspin.apputils.di.annotations.ApplicationScope;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;

@Module public class RestClientModule {
    // Cache
    private static final String HTTP_CACHE_NAME = "BB_HTTP_CACHE";
    private static final int DISC_CACHE_SIZE_MB = 10;

    @Provides @Singleton Cache getCache(@ApplicationScope Context pContext) {
        return new Cache(new File(pContext.getFilesDir()
                                          .getPath() + HTTP_CACHE_NAME), DISC_CACHE_SIZE_MB * 1024 * 1024);
    }

    @Provides @Singleton OkHttpClient getHttpClient(Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        return okHttpClient;
    }
}
