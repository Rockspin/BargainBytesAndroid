package com.rockspin.test;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * Created by kungfu_rorykelly on 07/02/15.
 */
public class TestHelper {
    final Random rand = new Random();

    public TestHelper(){ /* not used */}

    public String stringWithRandomLength(int max){
        final int keySize = 1 + rand.nextInt(max);
        return RandomStringUtils.randomAlphanumeric(keySize);
    }

}
