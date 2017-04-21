package com.rockspin.bargainbits.ui.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class BaseMvpPresenter<View : BaseMvpView> {

    var view: View? = null
        private set

    private var lifetimeDisposable: CompositeDisposable? = null

    protected fun addLifetimeDisposable(disposable: Disposable) {
        if (lifetimeDisposable == null) {
            throw IllegalStateException("Adding a lifetimeDisposable before calling onViewCreated")
        }

        lifetimeDisposable?.add(disposable)
    }

    fun onViewCreated(view: View) {
        this.view = view
        lifetimeDisposable = CompositeDisposable()
    }

    fun onViewDestroyed() {
        view = null
        lifetimeDisposable?.dispose()
        lifetimeDisposable = null
    }
}