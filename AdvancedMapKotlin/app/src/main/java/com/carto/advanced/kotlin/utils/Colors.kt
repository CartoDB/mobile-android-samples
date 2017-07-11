package com.carto.advanced.kotlin.utils

import android.graphics.Color

/**
 * Created by aareundo on 03/07/2017.
 */
class Colors {

    companion object {
        @JvmField val appleBlue = Color.rgb(14, 122, 254)

        @JvmField val appleBlueInt = Color.parseColor("#0E7AFE")

        @JvmField val darkGrayInt = Color.parseColor("#5A5A5A")

        @JvmField val green = Color.parseColor("#73C86B")

        @JvmField val locationRed = Color.rgb(215, 82, 75)

        @JvmField val transparentLocationRed = Color.argb(150, 215, 82, 75)

        @JvmField val navy = Color.rgb(22, 41, 69)

        @JvmField val transparentGray = Color.argb(150, 50, 50, 50)
    }
}
