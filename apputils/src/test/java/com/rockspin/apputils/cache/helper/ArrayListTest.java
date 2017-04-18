package com.rockspin.apputils.cache.helper;

import com.rockspin.apputils.cache.LruDiscCache;
import com.rockspin.apputils.cache.LruMemoryCache;
import com.rockspin.test.TestHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rx.Single;

public class ArrayListTest {

    private static final String TEST_KEY = "TEST_KEY";
    private TestHelper mTestHelper = new TestHelper();
    private LruDiscCache mLruDiscCache;
    private LruMemoryCache mLruMemoryCache;
    private SimpleCacheWriter mCacheWriter;
    private SimpleCacheReader mCacheReader;
    private ArrayList<String> mArrayList = new ArrayList<>(Arrays.asList("My balls", "your Balls", "Our balls"));

    @Before
    public void setUp() throws Exception {
        final File cacheFile = new File("test");
        mLruDiscCache = new LruDiscCache(cacheFile, 1, 1);
        mLruMemoryCache = new LruMemoryCache(2000);
        mCacheWriter = new SimpleCacheWriter(mLruMemoryCache, mLruDiscCache);
        mCacheReader = new SimpleCacheReader(mLruMemoryCache, mLruDiscCache);
    }

    @After
    public void tearDown() throws Exception {
        mLruDiscCache.clearAllData();
        mLruDiscCache = null;
        mLruMemoryCache = null;
        mCacheWriter = null;
        mCacheReader = null;
    }

    @Test
    public void testWritingArrayList() throws Exception {
        final Single<ArrayList<String>> cacheResult = mCacheWriter.cacheValue(TEST_KEY, mArrayList);
        cacheResult.toObservable().toBlocking().first();

        final Single<? extends ArrayList> result = mCacheReader.runRequest(TEST_KEY,  mArrayList.getClass());

        Assert.assertEquals(mArrayList, result.toObservable().toBlocking().first());
    }

}
