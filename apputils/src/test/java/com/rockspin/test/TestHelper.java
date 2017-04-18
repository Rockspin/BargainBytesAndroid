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
    static {
        Timber.plant(new TestingTree());
    }

    public TestHelper(){ /* not used */}

    public String stringWithRandomLength(int max){
        final int keySize = 1 + rand.nextInt(max);
        return RandomStringUtils.randomAlphanumeric(keySize);
    }

    /** A tree which logs important information for crash reporting. */
    private static class TestingTree extends Timber.HollowTree { private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$");
        private static final ThreadLocal<String> NEXT_TAG = new ThreadLocal<String>();


        private static String createTag() {
            String tag = NEXT_TAG.get();
            if (tag != null) {
                NEXT_TAG.remove();
                return tag;
            }

            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length < 6) {
                throw new IllegalStateException(
                        "Synthetic stacktrace didn't have enough elements: are you using proguard?");
            }
            tag = stackTrace[4].getClassName();
            Matcher m = ANONYMOUS_CLASS.matcher(tag);
            if (m.find()) {
                tag = m.replaceAll("");
            }
            return tag.substring(tag.lastIndexOf('.') + 1);
        }
        @Override public void v(String message, Object... args) {
            System.out.print(createTag() + " " +  message + "\n");
        }

        @Override public void v(Throwable t, String message, Object... args) {
            System.out.print(createTag() + " " + message + "\n");
        }

        @Override public void d(String message, Object... args) {
            System.out.print(createTag() + " " + message + "\n");
        }

        @Override public void d(Throwable t, String message, Object... args) {
            System.out.print(createTag() + " " + message + "\n");
        }

        @Override public void i(String message, Object... args) {
            System.out.print(createTag() + " " + message + "\n");
        }

        @Override public void i(Throwable t, String message, Object... args) {
            System.out.print(createTag() + " " + message + "\n");
        }

        @Override public void w(String message, Object... args) {
            System.out.print(createTag() + " " +message + "\n");
        }

        @Override public void w(Throwable t, String message, Object... args) {
            System.out.print(createTag() + " " +message + "\n");
        }

        @Override public void e(String message, Object... args) {
            System.out.print(createTag() + " " + message + "\n");
        }

        @Override public void e(Throwable t, String message, Object... args) {
            System.out.print(createTag() + " " + message + "\n");
        }
    }
}
