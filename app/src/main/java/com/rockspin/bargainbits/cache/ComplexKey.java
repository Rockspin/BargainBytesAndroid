package com.rockspin.bargainbits.cache;

import com.fernandocejas.arrow.checks.Preconditions;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Utility that calculates complex type safe keys for a cache.
 */
public final class ComplexKey {

    private ComplexKey() { /* not used */ }

    public static String getComplexTypeSafeKey(final String pKey, final Class<?> pClassType) {
        Preconditions.checkNotNull(pKey, "You cannot save a null key");
        Preconditions.checkNotNull(pClassType, "You cannot save a null value");

        return new HashCodeBuilder(17, 37).append(pKey).append(pClassType.getName()).build().toString();
    }
}
