package com.rockspin.bargainbits.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Use [BaseMvpActivity] if you want to use MVP.
 */
abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject internal lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return fragmentInjector
    }
}
