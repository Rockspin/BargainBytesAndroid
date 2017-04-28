package com.rockspin.bargainbits.test_utils

import io.reactivex.subjects.PublishSubject

/**
 * Calls onNext, immediately followed by onComplete on this PublishSubject
 * Useful when testing Singles for example
 */
fun <T> PublishSubject<T>.completeWithValue(value: T) {
    onNext(value)
    onComplete()
}