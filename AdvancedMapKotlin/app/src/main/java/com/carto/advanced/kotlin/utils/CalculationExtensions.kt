package com.carto.advanced.kotlin.utils

import java.math.BigInteger

/**
 * Created by aareundo on 28/07/2017.
 */

fun BigInteger.toMB(): Double {
    val bytesInMB = 1048576.0
    return Math.round(this.toDouble() / bytesInMB * 10.0) / 10.0
}