package com.rockspin.bargainbits;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public final class CustomOperators {
    
    private CustomOperators() { }

    /**
     * Example of terminal operator, that reduces stream to calculate sum on integer elements.
     */
    public static class ToList <T> implements Function<Stream<T>, ArrayList<T>> {

        @Override public ArrayList<T> apply(Stream<T> stream) {
            return stream.reduce(new ArrayList<T>(), (value1, value2) -> {
                value1.add(value2);
                return value1;
            });
        }
    }
}