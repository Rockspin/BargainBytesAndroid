package com.rockspin.bargainbits.util.format

/**
 * Helper interface to help format prices.
 */
interface PriceFormatter {
    fun formatPrice(price: Double): String
}