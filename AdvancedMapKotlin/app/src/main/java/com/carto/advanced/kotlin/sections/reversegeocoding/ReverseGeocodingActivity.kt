package com.carto.advanced.kotlin.sections.reversegeocoding

import android.os.Bundle
import com.carto.advanced.kotlin.sections.base.activities.PackageDownloadBaseActivity
import com.carto.advanced.kotlin.sections.base.views.BaseGeocodingView
import com.carto.advanced.kotlin.utils.Utils
import com.carto.geocoding.*
import com.carto.packagemanager.CartoPackageManager
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener

/**
 * Created by aareundo on 11/07/2017.
 */
class ReverseGeocodingActivity : PackageDownloadBaseActivity() {

    var service: ReverseGeocodingService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = ReverseGeocodingView(this)
        setContentView(contentView)

        val folder = Utils.createDirectory(this, "geocodingpackages")
        contentView?.manager = CartoPackageManager(BaseGeocodingView.SOURCE, folder)

        service = PackageManagerReverseGeocodingService(contentView?.manager)
    }

    override fun onResume() {
        super.onResume()

        contentView?.map?.mapEventListener = object : MapEventListener() {

            override fun onMapClicked(mapClickInfo: MapClickInfo?) {

                val location = mapClickInfo?.clickPos
                val request = ReverseGeocodingRequest(contentView?.projection, location)

                val meters = 125.0f
                request.searchRadius = meters

                val results = service?.calculateAddresses(request)
                // Scan the results list. If we found relatively close point-based match,
                // use this instead of the first result.
                // In case of POIs within buildings, this allows us to hightlight POI instead of the building

                var result: GeocodingResult? = null
                val count = results?.size()?.toInt()!!

                if (count > 0) {
                    result = results.get(0)
                }

                for (i in 0..count - 1) {

                    val other = results.get(i)

                    // 0.8f means 125 * (1.0 - 0.9) = 12.5 meters (rank is relative distance)
                    if (other.rank > 0.9f) {
                        val name = other.address.name
                        // Points of interest usually have names, others just have addresses
                        if (name != null && name != "") {
                            result = other
                            break
                        }
                    }
                }

                if (result == null) {
                    alert("Couldn't find any addresses. Are you sure you have " +
                            "downloaded the region you're trying to reverse geocode?")
                    return
                }

                val title = ""
                val description = result.toString()
                val goToPosition = false

                (contentView as ReverseGeocodingView).showResult(result, title, description, goToPosition)
            }
        }

        if (contentView?.hasLocalPackages()!!) {
            contentView?.showLocalPackages()
        }
    }

    override fun onPause() {
        super.onPause()

        contentView?.map?.mapEventListener = null
    }

    override fun setOnlineMode() {
        service = PeliasOnlineReverseGeocodingService(BaseGeocodingView.MAPZEN_API_KEY)
    }

    override fun setOfflineMode() {
        service = PackageManagerReverseGeocodingService(contentView?.manager)
    }
}