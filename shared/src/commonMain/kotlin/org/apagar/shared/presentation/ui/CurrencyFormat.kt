package org.apagar.shared.presentation.ui

import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * Pure Kotlin Multiplatform USD currency formatter (e.g., 8334.88 -> "$8,334.88").
 * Avoids JVM-only String.format / DecimalFormat so it works identically on Android, iOS, JVM, and Wasm.
 */
fun formatUsd(amount: Double, showSign: Boolean = false): String {
    val isNegative = amount < 0
    val totalCents = abs(amount * 100.0).roundToLong()
    val dollars = totalCents / 100
    val cents = totalCents % 100

    val dollarsStr = dollars.toString()
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()

    val centsStr = cents.toString().padStart(2, '0')
    val prefix = when {
        isNegative -> "-$"
        showSign -> "+$"
        else -> "$"
    }
    return "$prefix$dollarsStr.$centsStr"
}

