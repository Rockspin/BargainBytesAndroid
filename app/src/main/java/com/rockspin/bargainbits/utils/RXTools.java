package com.rockspin.bargainbits.utils;

import rx.functions.Func1;

public final class RXTools {

    private RXTools() { /* not used */ }

    public static <T> Func1<T, Void> toVoid(){
        return t -> null;
    }
}
