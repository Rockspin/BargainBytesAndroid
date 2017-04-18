package com.rockspin.apputils.cache.helper;

import com.rockspin.apputils.cache.LruDiscCache;
import com.rockspin.apputils.cache.LruMemoryCache;
import com.rockspin.test.TestHelper;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleCacheWriterTest {
    private TestHelper mTestHelper = new TestHelper();
    private LruDiscCache mLruDiscCache;
    private LruMemoryCache mLruMemoryCache;
    private SimpleCacheWriter mCacheWriter;

    @Before public void setUp() throws Exception {
        final File cacheFile = new File("test");
        mLruDiscCache = new LruDiscCache(cacheFile, 1, 1);
        mLruMemoryCache = new LruMemoryCache(0.1f);
        mCacheWriter = new SimpleCacheWriter(mLruMemoryCache, mLruDiscCache);
    }

    @After public void tearDown() throws Exception {
        mLruDiscCache = null;
        mLruMemoryCache = null;
    }

    @Test public void testCacheResult() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = cacheValueWithKey(key);
        assertValueInCaches(key, value);
    }

    @Test() public void testCacheResultDeleteMemory() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = cacheValueWithKey(key);
        assertValueInCaches(key, value);
        mCacheWriter.deleteEntry(key, String.class).toBlocking().value();
        assertThat(mLruMemoryCache.get(key, String.class)
                                  .isPresent()).isEqualTo(false);
    }

    @Test() public void testCacheResultDeleteDisc() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = cacheValueWithKey(key);
        assertValueInCaches(key, value);
        mCacheWriter.deleteEntry(key, String.class).toBlocking().value();
    }

    private String cacheValueWithKey(String key) throws java.io.IOException {
        final String value = mTestHelper.stringWithRandomLength(1000);
        mCacheWriter.cacheValue(key, value).toBlocking()
                    .value();

        assertValueInCaches(key, value);
        return value;
    }

    private void assertValueInCaches(String key, String value) throws java.io.IOException {
        assertThat(mLruMemoryCache.get(key, String.class)
                                  .get()).isEqualTo(value);
        assertThat(mLruDiscCache.get(key, String.class).mValue).isEqualTo(value);
    }
}