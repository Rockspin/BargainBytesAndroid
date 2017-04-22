package com.rockspin.bargainbits.ui

import android.os.Bundle
import com.rockspin.bargainbits.ui.mvp.BaseMvpPresenter
import com.rockspin.bargainbits.ui.mvp.BaseMvpView

//TODO Once all activities use BaseMvpActivity - rename it to BaseActivity
abstract class BaseMvpActivity<U : BaseMvpView, T : BaseMvpPresenter<U>> : BaseActivity(), BaseMvpView {

    abstract var presenter: T

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter.onViewCreated(this as U)
    }

    override fun onDestroy() {
        presenter.onViewDestroyed()
        super.onDestroy()
    }
}
