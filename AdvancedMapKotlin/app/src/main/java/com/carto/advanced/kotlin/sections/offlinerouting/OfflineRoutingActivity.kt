package com.carto.advanced.kotlin.sections.offlinerouting

import android.os.Bundle
import com.carto.advanced.kotlin.routing.Routing
import com.carto.advanced.kotlin.sections.base.activities.PackageDownloadBaseActivity
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.packagemanager.CartoPackageManager
import com.carto.routing.CartoOnlineRoutingService
import com.carto.routing.PackageManagerValhallaRoutingService
import com.carto.ui.ClickType
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener
import org.jetbrains.anko.doAsync

/**
 * Created by aareundo on 18/08/2017.
 */
class OfflineRoutingActivity : PackageDownloadBaseActivity() {

    var routing: Routing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = OfflineRoutingView(this)
        setContentView(contentView)

        routing = Routing(this, contentView!!.map)

        val folder = Utils.createDirectory(this, "routingpackages")
        val source = Routing.ROUTING_TAG + Routing.OFFLINE_ROUTING_SOURCE
        contentView?.manager = CartoPackageManager(source, folder)

        setOnlineMode()
    }

    override fun onResume() {
        super.onResume()

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
    }

    override fun onPause() {
        super.onPause()

        contentView?.map?.mapEventListener = null
    }

    override fun setOnlineMode() {
        routing?.service = CartoOnlineRoutingService(Routing.ONLINE_ROUTING_SOURCE + Routing.TRANSPORT_MODE)
    }

    override fun setOfflineMode() {
        routing?.service = PackageManagerValhallaRoutingService(contentView!!.manager)
    }

    fun showRoute(start: MapPos, stop: MapPos) {
        doAsync {
            val result = routing!!.getResult(start, stop)

            if (result == null) {
                runOnUiThread {
                    contentView?.progressLabel?.complete("Routing failed. Please try again")
                }
                return@doAsync
            }

            runOnUiThread {
                contentView?.progressLabel?.complete(routing?.getMessage(result)!!)
            }

        }
    }
}