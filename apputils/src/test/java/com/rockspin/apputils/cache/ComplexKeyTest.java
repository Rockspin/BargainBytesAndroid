package com.rockspin.apputils.cache;

import com.rockspin.test.TestHelper;
import org.junit.Test;

public class ComplexKeyTest {
    private TestHelper mTestHelper = new TestHelper();
    @Test
    public void testGetComplexTypeSafeKey() throws Exception {
          ComplexKey.getComplexTypeSafeKey(mTestHelper.stringWithRandomLength(1000), String.class);
    }

    @Test(expected = NullPointerException.class)
    public void testGetComplexTypeSafeKeyNull() throws Exception {
        ComplexKey.getComplexTypeSafeKey(null, null);
    }
}