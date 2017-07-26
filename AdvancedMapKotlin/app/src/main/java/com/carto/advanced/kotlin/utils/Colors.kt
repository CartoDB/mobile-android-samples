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

        @JvmField val locationRed = Color.rgb(242, 68, 64)

        @JvmField val transparentLocationRed = Color.argb(150, 242, 68, 64)

        @JvmField val navy = Color.rgb(22, 41, 69)

        @JvmField val transparentNavy = Color.argb(150, 22, 41, 69)

        @JvmField val transparentGray = Color.argb(150, 50, 50, 50)

        @JvmField val darkTransparentGray = Color.argb(200, 50, 50, 50)
        @JvmField val lightTransparentGray = Color.argb(120, 50, 50, 50)

        @JvmField val nearWhite = Color.rgb(245, 245, 245)
    }
}
