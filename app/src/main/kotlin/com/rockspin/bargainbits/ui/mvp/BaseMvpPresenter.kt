package com.rockspin.bargainbits.ui.mvp

import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseMvpPresenter<View : BaseMvpView> {

    protected var view: View? = null
        private set

    private var lifetimeDisposable: CompositeDisposable? = null

    protected fun addLifetimeDisposable(disposable: Disposable) {
        if (lifetimeDisposable == null) {
            throw IllegalStateException("Adding a lifetimeDisposable before calling onViewCreated")
        }

        lifetimeDisposable?.add(disposable)
    }

    @CallSuper
    open fun onViewCreated(view: View) {
        this.view = view
        lifetimeDisposable = CompositeDisposable()
    }

    @CallSuper
    open fun onViewDestroyed() {
        view = null
        lifetimeDisposable?.dispose()
        lifetimeDisposable = null
    }
}