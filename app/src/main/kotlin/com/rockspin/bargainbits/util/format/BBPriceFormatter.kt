package com.rockspin.bargainbits.util.format

import java.text.NumberFormat
import java.util.*


/**
 * Created by valentin.hinov on 23/04/2017.
 */
class BBPriceFormatter (val currencyIsoCode: String): PriceFormatter {

    private val numberFormat: NumberFormat by lazy {
        val numberFormat = NumberFormat.getCurrencyInstance()
        numberFormat.currency = Currency.getInstance(currencyIsoCode)
        numberFormat
    }

    override fun formatPrice(price: Double): String {
        return numberFormat.format(price)
    }
}