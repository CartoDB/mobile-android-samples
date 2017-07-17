package com.carto.advanced.kotlin.sections.routedownload

import android.os.Bundle
import com.carto.advanced.kotlin.routing.Route
import com.carto.advanced.kotlin.routing.Routing
import com.carto.advanced.kotlin.sections.base.BaseActivity
import com.carto.advanced.kotlin.utils.BoundingBox
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.packagemanager.CartoPackageManager
import com.carto.packagemanager.PackageInfo
import com.carto.routing.CartoOnlineRoutingService
import com.carto.routing.PackageManagerValhallaRoutingService
import org.jetbrains.anko.doAsync
import java.nio.MappedByteBuffer

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
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.overlayLayer!!.vectorElementEventListener = null
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
                contentView?.progressLabel?.complete("Routing failed. Please try again")
                return@doAsync
            }

            contentView?.progressLabel?.complete(routing?.getMessage(result)!!)

            val color = com.carto.graphics.Color(14, 122, 254, 150)

            runOnUiThread {
                routing!!.show(result, color, {
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
