package com.carto.advanced.kotlin.sections.gpslocation

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity
import com.carto.ui.MapEventListener

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

        contentView?.rotationResetButton?.setOnClickListener {
            contentView?.map?.setMapRotation(0.0f, 0.3f)
        }

        contentView?.map?.mapEventListener = object : MapEventListener() {

            var previousAngle: Float = -1.0f
            var previousZoom: Float = -1.0f

            override fun onMapMoved() {

                val angle = contentView?.map?.mapRotation!!
                val zoom = contentView?.map?.zoom!!

                if (previousAngle != angle) {
                    contentView?.rotationResetButton?.rotate(angle)
                    previousAngle = angle
                }

                if (previousZoom != zoom) {
                    contentView?.scaleBar?.update()
                    previousZoom = zoom
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        manager?.removeUpdates(listener)

        contentView?.rotationResetButton?.setOnClickListener(null)
        contentView?.map?.mapEventListener = null
    }

    override fun onPermissionsGranted(granted: Boolean) {

        if (granted) {
            initializeLocationManager()
        } else {
            val text = "Fine, so we won't track your location"
            contentView?.showBanner(text)
            contentView?.switch?.visibility = View.GONE
        }
    }

    fun initializeLocationManager() {

        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        listener = CartoLocationListener()

        val providers = manager?.getProviders(true)!!

        if (providers.size == 0) {
            val text = "Cannot get location, no location providers enabled. Check device settings"
            contentView?.showBanner(text)
        }

        for (provider in providers) {
            manager?.requestLocationUpdates(provider, 1000, 50.0f, listener)
        }
    }

    inner class CartoLocationListener : android.location.LocationListener {

        override fun onLocationChanged(location: Location?) {

            // Not "online", but reusing the online switch to achieve location tracking functionality
            if (contentView?.switch?.isOnline!!) {
                contentView?.showUserAt(location!!)
            }

        }

        override fun onProviderDisabled(provider: String?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

    }
}
