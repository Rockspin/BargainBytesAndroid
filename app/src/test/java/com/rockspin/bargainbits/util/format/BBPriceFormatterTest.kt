package com.rockspin.bargainbits.util.format

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Created by valentin.hinov on 23/04/2017.
 */
class BBPriceFormatterTest {

    private lateinit var priceFormatter: BBPriceFormatter

    private lateinit var defaultLocale: Locale

    @Before
    fun setUp() {
        defaultLocale = Locale.getDefault()
    }

    @After
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun roundsPricesCorrectly() {
        Locale.setDefault(Locale.US)
        priceFormatter = BBPriceFormatter("USD")

        assertThat(priceFormatter.formatPrice(2.001)).isEqualTo("$2.00")
        assertThat(priceFormatter.formatPrice(15.789999)).isEqualTo("$15.79")
        assertThat(priceFormatter.formatPrice(1.5)).isEqualTo("$1.50")
    }

    @Test
    fun formatsUSDPriceInUKLocale() {
        Locale.setDefault(Locale.UK)
        priceFormatter = BBPriceFormatter("USD")

        assertThat(priceFormatter.formatPrice(2.00)).isEqualTo("USD2.00")
    }

    @Test
    fun formatsUKPriceInUKLocale() {
        Locale.setDefault(Locale.UK)
        priceFormatter = BBPriceFormatter("GBP")

        assertThat(priceFormatter.formatPrice(2.00)).isEqualTo("£2.00")
    }

    @Test
    fun formatsUKPriceInUSDLocale() {
        Locale.setDefault(Locale.US)
        priceFormatter = BBPriceFormatter("GBP")

        assertThat(priceFormatter.formatPrice(2.00)).isEqualTo("GBP2.00")
    }
}