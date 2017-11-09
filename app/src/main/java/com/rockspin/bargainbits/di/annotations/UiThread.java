package com.rockspin.bargainbits.di.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the activity to be memoized in the
 * correct component.
 */
@Qualifier @Documented @Retention(RUNTIME) public @interface UiThread {
}