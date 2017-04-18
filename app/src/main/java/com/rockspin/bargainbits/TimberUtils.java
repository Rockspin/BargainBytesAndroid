package com.rockspin.bargainbits;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

/**
 * Wrapper For Timber calls.
 */
public class TimberUtils {

    public static void start() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override public void i(final String message, final Object... args) { /* not used */ }

        @Override public void i(final Throwable t, final String message, final Object... args) {
        }

        @Override public void e(final String message, final Object... args) {/* not used */ }

        @Override public void e(final Throwable t, final String message, final Object... args) {
            Crashlytics.log(message);
            Crashlytics.logException(t);
        }

        @Override protected void log(int priority, String tag, String message, Throwable t) { /* not used */}
    }

}
