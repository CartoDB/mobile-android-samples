package com.carto.advanced.kotlin.sections.base

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.ProgressLabel
import com.carto.advanced.kotlin.components.StateSwitch
import com.carto.advanced.kotlin.components.SwitchButton
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOfflineVectorTileLayer
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.packagemanager.CartoPackageManager

/**
 * Created by aareundo on 03/07/2017.
 */
open class DownloadBaseView(context: Context) : MapBaseView(context) {

    val MAP_SOURCE = "nutiteq.osm"

    val progressLabel = ProgressLabel(context)

    var onlineLayer: CartoOnlineVectorTileLayer? = null
    var offlineLayer: CartoOfflineVectorTileLayer? = null

    var manager: CartoPackageManager? = null

    val switchButton = SwitchButton(context, R.drawable.icon_wifi_on, R.drawable.icon_wifi_off)
    init {

        addView(progressLabel)
        progressLabel.hide()

        addButton(switchButton)

        setOnlineMode()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding: Int = (5 * context.resources.displayMetrics.density).toInt()

        val w = frame.width
        val h = bottomLabelHeight
        val x = 0
        val y = frame.height - h

        progressLabel.setFrame(x, y, w, h)
    }

    fun setOnlineMode() {

        if (onlineLayer == null) {
            onlineLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT)
        }

        if (offlineLayer != null) {
            map.layers?.remove(offlineLayer)
        }

        map.layers?.insert(0, onlineLayer)
    }

    fun setOfflineMode(manager: CartoPackageManager) {
        map.layers?.remove(onlineLayer)
        offlineLayer = CartoOfflineVectorTileLayer(manager, CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT)
        map.layers?.insert(0, offlineLayer)
    }

    fun setOfflineMode() {
        setOfflineMode(manager!!)
    }
}