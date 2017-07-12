package com.carto.advanced.kotlin.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Environment
import com.carto.graphics.Bitmap
import com.carto.utils.BitmapUtils
import java.io.File

/**
 * Created by aareundo on 12/07/2017.
 */
class Utils {
    companion object {
        fun resourceToBitmap(resources: Resources, id: Int): Bitmap {
            val android = BitmapFactory.decodeResource(resources, id)
            return BitmapUtils.createBitmapFromAndroidBitmap(android)
        }

        fun createDirectory(context: Context, folder: String): String {

            val directory = File(context.getExternalFilesDir(null), folder)

            if (!directory.exists()) {
                directory.mkdir()
            }

            return directory.absolutePath
        }
    }
}