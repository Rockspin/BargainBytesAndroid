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
     * Example of intermediate operator, that produces reversed stream.
     * 
     * @param <T>
     */
    public static class Reverse<T> implements Function<Stream<T>, Stream<T>> {

        @Override
        public Stream<T> apply(Stream<T> stream) {
            final Iterator<? extends T> iterator = stream.getIterator();
            final ArrayDeque<T> deque = new ArrayDeque<T>();
            while (iterator.hasNext()) {
                deque.addFirst(iterator.next());
            }
            return Stream.of(deque.iterator());
        }
    }
    
    /**
     * Example of combining {@code Stream} operators.
     * 
     * @param <T>
     */
    public static class SkipAndLimit<T> implements Function<Stream<T>, Stream<T>> {
        
        private final int skip, limit;

        public SkipAndLimit(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }
        
        @Override
        public Stream<T> apply(Stream<T> stream) {
            return stream.skip(skip).limit(limit);
        }
    }
    
   /**
     * Example of terminal operator, that reduces stream to calculate sum on integer elements.
     */
    public static class Sum implements Function<Stream<Integer>, Integer> {

        @Override
        public Integer apply(Stream<Integer> stream) {
            return stream.reduce(0, (value1, value2) -> value1 + value2);
        }
    }

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

    public static class ToSortedList <T> implements Function<Stream<T>, ArrayList<T>> {

        private final Comparator<? super T> comparator;

        public ToSortedList(final Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

        @Override public ArrayList<T> apply(Stream<T> stream) {
            return stream.reduce(new ArrayList<T>(), (value1, value2) -> {
                value1.add(value2);
                Collections.sort(value1, comparator);
                return value1;
            });
        }
    }


    /**
     * Example of terminal forEach operator.
     * 
     * @param <T>
     */
    public static class ForEach<T> implements Function<Stream<T>, Void> {
        
        private final Consumer<? super T> action;

        public ForEach(Consumer<? super T> action) {
            this.action = action;
        }
        
        @Override
        public Void apply(Stream<T> stream) {
            final Iterator<? extends T> iterator = stream.getIterator();
            while (iterator.hasNext()) {
                action.accept(iterator.next());
            }
            return null;
        }
    }
}