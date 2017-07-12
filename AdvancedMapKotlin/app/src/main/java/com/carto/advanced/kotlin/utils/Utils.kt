package com.carto.advanced.kotlin.utils

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.carto.graphics.Bitmap
import com.carto.utils.BitmapUtils

/**
 * Created by aareundo on 12/07/2017.
 */
class Utils {
    companion object {
        fun resourceToBitmap(resources: Resources, id: Int): Bitmap {
            val android = BitmapFactory.decodeResource(resources, id)
            return BitmapUtils.createBitmapFromAndroidBitmap(android)
        }
    }
}