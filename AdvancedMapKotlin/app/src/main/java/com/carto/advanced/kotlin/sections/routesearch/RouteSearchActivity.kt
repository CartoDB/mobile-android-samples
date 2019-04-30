package com.carto.advanced.kotlin.sections.routesearch

import android.os.Bundle
import com.carto.advanced.kotlin.routing.Route
import com.carto.advanced.kotlin.routing.Routing
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity
import com.carto.advanced.kotlin.sections.base.activities.PackageDownloadBaseActivity
import com.carto.advanced.kotlin.sections.base.views.BaseGeocodingView
import com.carto.advanced.kotlin.sections.vectorelement.VectorObjectClickListener
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.geometry.Geometry
import com.carto.geometry.LineGeometry
import com.carto.geometry.PointGeometry
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.packagemanager.CartoPackageManager
import com.carto.routing.CartoOnlineRoutingService
import com.carto.routing.PackageManagerRoutingService
import com.carto.search.SearchRequest
import com.carto.ui.ClickType
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener
import org.jetbrains.anko.doAsync

class RouteSearchActivity : BaseActivity() {

    var contentView: RouteSearchView? = null

    var routing: Routing? = null
    var objectListener: VectorObjectClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = RouteSearchView(this)
        setContentView(contentView)

        routing = Routing(this, contentView!!.map)
        routing?.showTurns = false

        objectListener = VectorObjectClickListener((contentView as RouteSearchView).overlaySource)

        routing?.service = CartoOnlineRoutingService("nutiteq.osm.car")
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        (contentView as RouteSearchView).overlayLayer.vectorElementEventListener = objectListener

        contentView?.map?.mapEventListener = object : MapEventListener() {

            var startPosition: MapPos? = null
            var stopPosition: MapPos? = null

            override fun onMapClicked(mapClickInfo: MapClickInfo?) {
                if (mapClickInfo?.clickType != ClickType.CLICK_TYPE_LONG) {
                    // Only listen to long clicks
                    objectListener?.reset()
                    return
                }

                val position = mapClickInfo.clickPos

                if (startPosition == null) {
                    startPosition = position

                    routing?.setStartMarker(startPosition!!)
                    (contentView as RouteSearchView).clearPOIs()
                } else {
                    stopPosition = position

                    routing?.setStopMarker(stopPosition!!)
                    showRoute(startPosition!!, stopPosition!!)

                    startPosition = null
                    stopPosition = null
                }
            }
        }

        contentView?.topBanner?.alert("Long click on a location to add a starting point")
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        (contentView as RouteSearchView).overlayLayer.vectorElementEventListener = null

        contentView?.map?.mapEventListener = null
    }

    fun showRoute(start: MapPos, stop: MapPos) {
        doAsync {
            val routingResult = routing!!.getResult(start, stop)

            if (routingResult == null) {
                runOnUiThread {
                    contentView?.showBanner("Routing failed. Please try again")
                }
                return@doAsync
            }

            val color = com.carto.graphics.Color(14, 122, 254, 150)

            runOnUiThread {

                contentView?.showBanner(routing?.getMessage(routingResult)!!)

                routing!!.show(routingResult, color, {
                    _: Route -> run {}
                })

                val collection = routing?.routeDataSource?.featureCollection
                val count = collection?.featureCount!!

                for (i in 0..count - 1) {
                    val item = collection.getFeature(i)!!

                    if (item.geometry is LineGeometry) {
                        doAsync {
                            showAttractions(item.geometry)
                        }
                    }
                }
            }

        }
    }

    fun showAttractions(geometry: Geometry) {

        val request = SearchRequest()
        request.projection = (contentView as RouteSearchView).baseSource?.projection
        request.geometry = geometry
        request.searchRadius = 500.0f
        request.filterExpression = "class='attraction'"

        val searchResults = (contentView as RouteSearchView).searchService!!.findFeatures(request)

        for (i in 0..searchResults.featureCount-1) {
            val item = searchResults.getFeature(i)

            if (item.geometry is PointGeometry) {
                (contentView as RouteSearchView).addPOITo(item)
            }
        }
    }
}
