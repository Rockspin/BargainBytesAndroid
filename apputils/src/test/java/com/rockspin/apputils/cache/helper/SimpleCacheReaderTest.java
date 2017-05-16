package com.rockspin.apputils.cache.helper;

import com.rockspin.apputils.cache.LruDiscCache;
import com.rockspin.apputils.cache.LruMemoryCache;
import com.rockspin.test.TestHelper;
import java.io.File;
import java.util.NoSuchElementException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rx.Single;

public class SimpleCacheReaderTest {
    private TestHelper mTestHelper = new TestHelper();
    private LruDiscCache mLruDiscCache;
    private LruMemoryCache mLruMemoryCache;
    private SimpleCacheWriter mCacheWriter;
    private SimpleCacheReader mCacheReader;

    @Before
    public void setUp() throws Exception {
        final File cacheFile = new File("test");
        mLruDiscCache = new LruDiscCache(cacheFile, 1, 1);
        mLruMemoryCache = new LruMemoryCache(0.1f);
        mCacheWriter = new SimpleCacheWriter(mLruMemoryCache, mLruDiscCache);
        mCacheReader = new SimpleCacheReader(mLruMemoryCache, mLruDiscCache);
    }

    @After
    public void tearDown() throws Exception {
        mLruDiscCache = null;
        mLruMemoryCache = null;
        mCacheWriter = null;
        mCacheReader = null;
    }

    @Test
    public void testGetItem() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = mTestHelper.stringWithRandomLength(1000);
        final Single<String> result = mCacheWriter.cacheValue(key, value);
        result.toObservable().toBlocking().first();
        final io.reactivex.Single<String> lookupFuture = mCacheReader.getItem(key, String.class);
        Assert.assertEquals(lookupFuture.blockingGet(), value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetItemNotExist_throwsNoSuchElementException() throws Exception {
        mCacheReader.getItem("blabla", String.class).blockingGet();
    }
}