package com.rockspin.bargainbits.di.modules

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
    internal fun providesPriceFormatter(): PriceFormatter {
        return BBPriceFormatter(DEFAULT_CURRENCY_ISO_CODE)
    }
}