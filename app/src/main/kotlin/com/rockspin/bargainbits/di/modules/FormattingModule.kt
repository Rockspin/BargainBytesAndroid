package com.rockspin.bargainbits.di.modules

import android.content.Context
import android.support.annotation.StringRes
import com.rockspin.apputils.di.annotations.ApplicationScope
import com.rockspin.bargainbits.util.ResourceProvider
import com.rockspin.bargainbits.util.format.BBPriceFormatter
import com.rockspin.bargainbits.util.format.PriceFormatter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provides dependencies related to formatting.
 */
@Module
class FormattingModule {

    companion object {
        private val DEFAULT_CURRENCY_ISO_CODE = "USD"
    }

    @Provides
    @Singleton
    internal fun providesResourceProvider(@ApplicationScope context: Context): ResourceProvider {
        return object: ResourceProvider {
            override fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
                return context.getString(id, *formatArgs)
            }
        }
    }

    @Provides
    @Singleton
    internal fun providesPriceFormatter(): PriceFormatter {
        return BBPriceFormatter(DEFAULT_CURRENCY_ISO_CODE)
    }
}