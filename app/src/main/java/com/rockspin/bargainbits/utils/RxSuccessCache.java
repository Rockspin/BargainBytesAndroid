package com.rockspin.bargainbits.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.fernandocejas.arrow.checks.Preconditions;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.functions.Func0;

/**
 * Caches Cold observable i.e Sequences that are passive and start producing notifications on request (when subscribed
 * to), Observables must implement onComplete or this class will not cache results/will behave in unexpected ways.
 */
public final class RxSuccessCache<ReturnType> {

    private static final String DEFAULT_KEY = "DEFAULT_KEY";

    @NonNull
    private final Map<String, Observable<ReturnType>> cachedSourceObservable = Collections
        .synchronizedMap(new HashMap<>());
    @NonNull
    private final Map<String, ReturnType> cachedValue = Collections
        .synchronizedMap(new HashMap<>());
    @Nullable
    private final File cacheDir;
    @Nullable
    private final Type returnType;

    public static <T> RxSuccessCache<T> create() {
        return new RxSuccessCache<>();
    }

    public static <T> RxSuccessCache<T> create(@NonNull final File dir, @NonNull final Type type) {
        return new RxSuccessCache<>(dir, type);
    }

    private RxSuccessCache() {
        cacheDir = null;
        returnType = null;
    }

    private RxSuccessCache(@NonNull final File dir, @NonNull final Type type) {
        cacheDir = dir;
        returnType = type;
    }

    /**
     * Caches Cold observable i.e Sequences that are passive and start producing notifications on request (when
     * subscribed
     * to), source observables must implement onComplete or this class will not cache results/will behave in unexpected
     * ways.
     *
     * Source observable will be subscribed on the appropriate thread, do not add onSubscribe to this observables as it
     * may delay the emission of cached values.
     *
     * @param sourceObservableFunc function that returns the observable we want to cache ( function do that we do not
     *                             always create the observable)
     * @return Observable returning from memory, cache or sourceObservableFunc depending on cache status
     */
    public Observable<ReturnType> get(Func0<Observable<ReturnType>> sourceObservableFunc) {
        return get(DEFAULT_KEY, sourceObservableFunc);
    }

    /**
     * Caches Cold observable i.e Sequences that are passive and start producing notifications on request (when
     * subscribed
     * to), source observables must implement onComplete or this class will not cache results/will behave in unexpected
     * ways.
     *
     * Source observable will be subscribed on the appropriate thread, do not add onSubscribe to this observables as it
     * may delay the emission of cached values.
     *
     * @param key                  key for request, each request will be cached using this as identifier.
     * @param sourceObservableFunc function that returns the observable we want to cache ( function do that we do not
     *                             always create the observable)
     * @return Observable returning from memory, cache or sourceObservableFunc depending on cache status
     */
    public Observable<ReturnType> get(String key, Func0<Observable<ReturnType>> sourceObservableFunc) {
        Preconditions.checkNotNull(key, "key cannot be null");
        // will only subscribe to observables if required, see:
        // http://blog.danlew.net/2015/06/22/loading-data-from-multiple-sources-with-rxjava/
        return Observable.defer(() ->Observable
            .concat(getCachedValue(key),
                Observable.defer(() -> getRunning(key)),
                Observable.defer(sourceObservableFunc::call)
                          .compose(saveRunning(key))
                          .compose(writeToMemory(key)))
            .first());
    }

    private Observable<ReturnType> getCachedValue(String key) {
        if (cachedValue.containsKey(key)) {
            return Observable.just(cachedValue.get(key));
        } else {
            return Observable.empty();
        }
    }

    private Observable<ReturnType> getRunning(String key) {
        if (cachedSourceObservable.containsKey(key)) {
            return cachedSourceObservable.get(key);
        } else {
            return Observable.empty();
        }
    }

    @NonNull
    private Observable.Transformer<ReturnType, ReturnType> saveRunning(String key) {
        return observable -> {
            Observable<ReturnType> cachedObservable = observable.cache();
            return cachedObservable.doOnSubscribe(() -> cachedSourceObservable.put(key, cachedObservable))
                                   .doOnTerminate(() -> cachedSourceObservable.remove(key));
        };
    }

    private Observable.Transformer<ReturnType, ReturnType> writeToMemory(String key) {
        return observable -> observable.doOnNext(type -> cachedValue.put(key, type));
    }

    public void clearCache() {
        cachedSourceObservable.clear();
        cachedValue.clear();
    }

}
