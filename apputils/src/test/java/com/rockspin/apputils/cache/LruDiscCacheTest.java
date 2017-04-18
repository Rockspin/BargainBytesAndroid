package com.rockspin.apputils.cache;

import com.rockspin.test.TestHelper;
import java.io.File;
import java.util.Random;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LruDiscCacheTest {

    private LruDiscCache mLruDiscCache;
    private TestHelper mTestHelper = new TestHelper();

    @Before public void setUp() throws Exception {
        final File cacheFile = new File("test");
        mLruDiscCache = new LruDiscCache(cacheFile, 1, 1);
    }

    @After public void tearDown() throws Exception {
        mLruDiscCache.clearAllData();
    }

    @Test public void testGet() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = mTestHelper.stringWithRandomLength(1000);
        mLruDiscCache.save(key, value);
        final ObjectAndSize<String> objectAndSizeSingle = mLruDiscCache.get(key, String.class);
        Assert.assertEquals(objectAndSizeSingle.mValue, value);
    }

    @Test public void testSave() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = mTestHelper.stringWithRandomLength(1000);
        mLruDiscCache.save(key, value);
    }

    @Test(expected = NullPointerException.class) public void testSaveNull() throws Exception {
        final String key = null;
        final String value = null;
        mLruDiscCache.save(key, value);
    }

    @Test(expected = IllegalStateException.class) public void testClearAllData() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = mTestHelper.stringWithRandomLength(1000);
        final ObjectAndSize<String> retrieveFuture = mLruDiscCache.get(key, String.class);
        Assert.assertEquals(retrieveFuture.mValue, value);
        mLruDiscCache.clearAllData();

        // should fail because no editor present.
        mLruDiscCache.get(key, String.class);
    }

    @Test public void testTypeSafety() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = mTestHelper.stringWithRandomLength(1000);
        final Integer valueInt = new Random().nextInt();

        mLruDiscCache.save(key, value);
        mLruDiscCache.save(key, valueInt);

        final ObjectAndSize<String> retrieveFuture = mLruDiscCache.get(key, String.class);
        Assert.assertEquals(retrieveFuture.mValue, value);

        final ObjectAndSize<Integer> retrieveIntegerFuture = mLruDiscCache.get(key, Integer.class);
        Assert.assertEquals(retrieveIntegerFuture.mValue, valueInt);
    }
}