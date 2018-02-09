package com.carto.advanced.kotlin.main

import android.app.Activity
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

/**
 * Screenshot taker for Espresso tests within AWS Device Farm
 *
 * Saves the image with the specified name (or a randomly generated one) and
 * saves it into a directory Device Farm will pull from when generating reports.
 *
 * From:
 * https://github.com/awslabs/aws-device-farm-sample-app-for-android/blob/master/app/src/androidTest/java/com/amazonaws/devicefarm/android/referenceapp/Util/ScreenShot.java
 */
object Screenshot {

    val FOLDER = "/test-screenshots/"

    private val TAG = "SCREENSHOT"
    private val SCREEN_SHOT_IMAGE_QUALITY = 100

    val directory: String
        get() = Environment.getExternalStorageDirectory().absolutePath

    fun take(activity: Activity, fileName: String, bitmap: Bitmap?) {
        var bitmap = bitmap
        // Create the file path.
        val pathBuilder = StringBuilder()
                .append(directory)
                .append(FOLDER)
                .append(fileName)
                .append(".png")

        val imageFile = File(pathBuilder.toString())

        // Verify that the directory exists and create it if not.
        val directoryPath = imageFile.parentFile
        if (!directoryPath.isDirectory) {
            Log.i(TAG, "Creating directory: " + directoryPath.toString())
            if (!directoryPath.mkdirs()) {
                Log.e(TAG, "Failed to create the directory")
                return
            }
        }

        Log.i(TAG, "Saving to path: " + imageFile.toString())

        if (bitmap == null) {
            val phoneView = activity.window.decorView.rootView
            phoneView.isDrawingCacheEnabled = true
            bitmap = Bitmap.createBitmap(phoneView.drawingCache)
            phoneView.isDrawingCacheEnabled = false
        }

        var out: OutputStream? = null

        try {
            out = FileOutputStream(imageFile)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, SCREEN_SHOT_IMAGE_QUALITY, out)
            out.flush()
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } finally {
            try {
                if (out != null) {
                    out.close()
                }
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
            }

        }
    }

    fun take(activity: Activity, bitmap: Bitmap) {
        take(activity, UUID.randomUUID().toString(), bitmap)
    }
}
