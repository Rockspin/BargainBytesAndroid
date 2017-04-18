package com.rockspin.apputils.cache;

import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.test.TestHelper;
import java.util.Random;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LruMemoryCacheTest {
    TestHelper mTestHelper = new TestHelper();
    private LruMemoryCache mLruMemoryCache;

    @Before
    public void setUp() throws Exception {
        mLruMemoryCache = new LruMemoryCache(1);
    }

    @After
    public void tearDown() throws Exception {
        mLruMemoryCache.clearAllData();
    }

    @Test
    public void testSave() throws Exception {
        for (int i = 0; i < 1000; i++) {
            final String key = mTestHelper.stringWithRandomLength(100);
            final String value = mTestHelper.stringWithRandomLength(1000);
            Assert.assertNotNull(mLruMemoryCache.save(key, new ObjectAndSize<>(value, value.length() * 4)));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSaveNull() throws Exception {
        final String keyNull = null;
        final String valueNull = null;
        mLruMemoryCache.save(keyNull, new ObjectAndSize<>(valueNull, 10));
    }

    @Test
    public void testGet() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(100);
        final String value = mTestHelper.stringWithRandomLength(1000);
        mLruMemoryCache.save(key, new ObjectAndSize<>(value,  value.length() * 4));
        Assert.assertEquals(mLruMemoryCache.get(key, String.class).get(), value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNull() throws Exception {
        final String key = null;
        mLruMemoryCache.get(key, String.class);
    }

    @Test()
    public void testEviction() throws Exception {
        for (int i = 0; i < 10; i++) {
            final String key = mTestHelper.stringWithRandomLength(10);
            final String value = mTestHelper.stringWithRandomLength(1000);
            mLruMemoryCache.save(key, new ObjectAndSize<>(value,  value.length() * 4));
            Assert.assertEquals(mLruMemoryCache.get(key, String.class).get(), value);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testClearAllData() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(100);
        final String value = mTestHelper.stringWithRandomLength(1000);
        mLruMemoryCache.save(key, new ObjectAndSize<>(value,  value.length() * 4));
        Assert.assertEquals(mLruMemoryCache.get(key, String.class).get(), value);
        mLruMemoryCache.clearAllData();
        Assert.assertEquals(mLruMemoryCache.get(key, String.class).get(), value);
    }

    @Test
    public void testTypeSafety() throws Exception {
        final String key = mTestHelper.stringWithRandomLength(1000);
        final String value = mTestHelper.stringWithRandomLength(1000);
        final Integer valueInt = new Random().nextInt();

        final ObjectAndSize<String> savingFuture = mLruMemoryCache.save(key, new ObjectAndSize<>(value, value.length()));
        final ObjectAndSize<Integer> savingIntegerFuture = mLruMemoryCache.save(key, new ObjectAndSize<>(valueInt, 8));

        final Optional<String> retrieveFuture = mLruMemoryCache.get(key, String.class);
        Assert.assertEquals(retrieveFuture.get(), value);

        final Optional<Integer> retrieveIntegerFuture = mLruMemoryCache.get(key, Integer.class);
        Assert.assertEquals(retrieveIntegerFuture.get(), valueInt);
    }
}