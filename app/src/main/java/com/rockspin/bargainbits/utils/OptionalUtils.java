package com.rockspin.bargainbits.utils;

import com.fernandocejas.arrow.optional.Optional;
import com.rockspin.apputils.Preconditions;
import rx.functions.Func0;

public class OptionalUtils {

    public static <T> T or(Optional<T> optional, Func0<T> func0) {
        if (!optional.isPresent()) {
            return Preconditions.checkNotNull(func0.call());
        }
        return optional.get();
    }
}
