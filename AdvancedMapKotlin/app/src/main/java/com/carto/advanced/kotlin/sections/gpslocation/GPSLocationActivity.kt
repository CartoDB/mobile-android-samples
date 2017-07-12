package com.carto.advanced.kotlin.sections.gpslocation

import android.Manifest
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import com.carto.advanced.kotlin.sections.base.BaseActivity

class GPSLocationActivity : BaseActivity() {

    var contentView: GPSLocationView? = null

    var manager: LocationManager? = null
    var listener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = GPSLocationView(this)
        setContentView(contentView)

        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        requestPermission(permission)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()
    }

    override fun onPermissionsGranted(granted: Boolean) {

        if (granted) {

        } else {
            val text = "Fine, so we won't track your location"
            contentView?.topBanner?.setText(text)
            contentView?.layoutSubviews()
            contentView?.switch?.visibility = View.GONE
        }
    }
}
