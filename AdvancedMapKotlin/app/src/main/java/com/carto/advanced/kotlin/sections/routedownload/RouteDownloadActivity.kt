package com.carto.advanced.kotlin.sections.routedownload

import android.os.Bundle
import com.carto.advanced.kotlin.routing.Route
import com.carto.advanced.kotlin.routing.Routing
import com.carto.advanced.kotlin.sections.base.BaseActivity
import com.carto.advanced.kotlin.utils.BoundingBox
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.packagemanager.*
import com.carto.routing.CartoOnlineRoutingService
import com.carto.routing.PackageManagerValhallaRoutingService
import com.carto.ui.ClickType
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener
import org.jetbrains.anko.doAsync

class RouteDownloadActivity : BaseActivity() {

    var contentView: RouteDownloadView? = null

    var routing: Routing? = null

    var boundingBox: BoundingBox? = null

    var mapManager: CartoPackageManager? = null
    var routingManager: CartoPackageManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = RouteDownloadView(this)
        setContentView(contentView)

        routing = Routing(this, contentView!!.map)

        var folder = Utils.createDirectory(this, "mappackages")
        mapManager = CartoPackageManager(Routing.MAP_SOURCE, folder)

        folder = Utils.createDirectory(this, "routeingpackages")
        routingManager = CartoPackageManager(Routing.ROUTING_TAG + Routing.ROUTING_SOURCE, folder)

        setOnlineMode()

        val mapPackages = mapManager!!.localPackages
        val routingPackages = routingManager!!.localPackages

        val mapPackageCount = mapPackages.size().toInt()
        val routingPackageCount = routingPackages.size().toInt()


        val existing = mutableListOf<PackageInfo>()

        // Add existing packages to the map as polygons,
        // but ensure both map package and routing package exist before adding them
        for (i in 0..mapPackageCount - 1) {

            val mapPackage = mapPackages[i]

            for (j in 0..routingPackageCount - 1) {
                val routingPackage = routingPackages[j]

                if (mapPackage.packageId.contains(routingPackage.packageId)) {
                    existing.add(mapPackage)
                }
            }
        }

        contentView?.addPolygonsTo(existing)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.overlayLayer!!.vectorElementEventListener = VectorElementIgnoreListener()


        mapManager?.packageManagerListener = object : PackageManagerListener() {
            override fun onPackageListUpdated() {
                // No implementation
            }

            override fun onPackageListFailed() {
                // No implementation
            }

            override fun onPackageStatusChanged(id: String?, version: Int, status: PackageStatus?) {
                instance.runOnUiThread {
                    val progress = status?.progress!!
                    contentView?.progressLabel?.update("Downloading Map: $progress%", progress)
                }
            }

            override fun onPackageUpdated(id: String?, version: Int) {

            }

            override fun onPackageCancelled(id: String?, version: Int) {

            }

            override fun onPackageFailed(id: String?, version: Int, errorType: PackageErrorType?) {
                contentView?.progressLabel?.complete("Map download failed")
            }
        }

        routingManager?.packageManagerListener = object : PackageManagerListener() {
            override fun onPackageListUpdated() {
                // No implementation
            }

            override fun onPackageListFailed() {
                // No implementation
            }

            override fun onPackageStatusChanged(id: String?, version: Int, status: PackageStatus?) {
                instance.runOnUiThread {
                    val progress = status?.progress!!
                    contentView?.progressLabel?.update("Downloading Route: $progress%", progress)
                }
            }

            override fun onPackageUpdated(id: String?, version: Int) {
                contentView?.addPolygonTo(boundingBox?.bounds!!)
            }

            override fun onPackageCancelled(id: String?, version: Int) {

            }

            override fun onPackageFailed(id: String?, version: Int, errorType: PackageErrorType?) {
                instance.runOnUiThread {
                    contentView?.progressLabel?.complete("Route download failed")
                }

            }
        }

        mapManager?.start()
        routingManager?.start()

        contentView?.map?.mapEventListener = object : MapEventListener() {

            var startPosition: MapPos? = null
            var stopPosition: MapPos? = null

            override fun onMapClicked(mapClickInfo: MapClickInfo?) {
                if (mapClickInfo?.clickType != ClickType.CLICK_TYPE_LONG) {
                    // Only listen to long clicks
                    return
                }

                val position = mapClickInfo.clickPos

                if (startPosition == null) {
                    startPosition = position

                    routing?.setStartMarker(startPosition!!)

                    instance.runOnUiThread {
                        contentView?.downloadButton?.disable()
                        contentView?.progressLabel?.hide()
                    }

                } else {
                    stopPosition = position

                    routing?.setStopMarker(stopPosition!!)
                    showRoute(startPosition!!, stopPosition!!)

                    startPosition = null
                    stopPosition = null
                }
            }
        }

        contentView?.downloadButton?.setOnClickListener {  }
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.overlayLayer!!.vectorElementEventListener = null

        mapManager?.packageManagerListener = null

        routingManager?.packageManagerListener = null

        contentView?.map?.mapEventListener = null

        contentView?.downloadButton?.setOnClickListener(null)
    }

    fun setOnlineMode() {
        routing?.service = CartoOnlineRoutingService(Routing.MAP_SOURCE + Routing.TRANSPORT_MODE)
    }

    fun setOfflineMode() {
        routing?.service = PackageManagerValhallaRoutingService(routingManager)
    }

    fun showRoute(start: MapPos, stop: MapPos) {
        doAsync {
            val result = routing!!.getResult(start, stop)

            if (result == null) {
                runOnUiThread {
                    contentView?.progressLabel?.complete("Routing failed. Please try again")
                    return@runOnUiThread
                }
            }

            val color = com.carto.graphics.Color(14, 122, 254, 150)

            runOnUiThread {

                contentView?.progressLabel?.complete(routing?.getMessage(result!!)!!)

                routing!!.show(result!!, color, {
                    route: Route ->
                    boundingBox = BoundingBox.fromMapBounds(contentView?.projection!!, route.bounds)
                })
                contentView?.downloadButton?.enable()

                if (!contentView?.progressLabel?.isVisible()!!) {
                    contentView?.progressLabel?.show()
                }
            }

        }
    }
}
