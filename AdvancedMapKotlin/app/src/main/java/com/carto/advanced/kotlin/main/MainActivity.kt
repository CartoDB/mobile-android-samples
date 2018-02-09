package com.carto.advanced.kotlin.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.carto.advanced.kotlin.model.Samples
import com.carto.advanced.kotlin.utils.Colors
import java.io.File

class MainActivity : AppCompatActivity() {

    var contentView: MainView? = null

    companion object {
        @JvmField val TITLE = "ACTIVITY_TITLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = MainView(this)
        setContentView(contentView)

        contentView?.addRows(Samples.list)

        val drawable = ColorDrawable(Colors.locationRed)
        supportActionBar?.setBackgroundDrawable(drawable)
    }

    override fun onResume() {
        super.onResume()

        for (row in contentView?.views!!) {
            row.setOnClickListener {
                val intent = Intent(this, row.sample?.activity)
                intent.putExtra(TITLE, row.sample?.title)
                startActivity(intent)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        for (row in contentView?.views!!) {
            row.setOnClickListener(null)
        }
    }

    // mkFolder() with permission logic for Espresso tests
    private val WRITE_STORAGE = 1

    fun mkFolder(folderName: String): Int {
        // make a folder under Environment.DIRECTORY_DCIM
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED != state) {
            Log.d("myAppName", "Error: external storage is unavailable")
            return 0
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            Log.d("myAppName", "Error: external storage is read only.")
            return 0
        }

        Log.d("myAppName", "External storage is not read only or unavailable")

        // Request permission when it is not granted.
        val grantResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
            Log.d("Advanced.Kotlin", "permission:WRITE_EXTERNAL_STORAGE: NOT granted!")
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions, WRITE_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), folderName)

        var result = 0
        if (folder.exists()) {
            Log.d("Advanced.Kotlin", "folder exists:" + folder.toString())
            result = 2 // folder exists
        } else {
            try {
                if (folder.mkdir()) {
                    Log.d("Advanced.Kotlin", "folder created:" + folder.toString())
                    result = 1 // folder created
                } else {
                    Log.d("Advanced.Kotlin", "creat folder fails:" + folder.toString())
                    result = 0 // create folder fails
                }
            } catch (ecp: Exception) {
                ecp.printStackTrace()
            }

        }
        return result
    }
}
