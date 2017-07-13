package com.carto.advanced.kotlin.sections.base

import android.content.Context
import com.carto.advanced.kotlin.components.ProgressLabel
import com.carto.advanced.kotlin.components.StateSwitch
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOfflineVectorTileLayer
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.packagemanager.CartoPackageManager

/**
 * Created by aareundo on 03/07/2017.
 */
open class DownloadBaseView(context: Context) : MapBaseView(context) {

    val MAP_SOURCE = "nutiteq.osm"

    val onlineSwitch = StateSwitch(context)
    val progressLabel = ProgressLabel(context)

    var onlineLayer: CartoOnlineVectorTileLayer? = null
    var offlineLayer: CartoOfflineVectorTileLayer? = null

    var manager: CartoPackageManager? = null

    init {

        addView(onlineSwitch)
        addView(progressLabel)

        progressLabel.hide()

        setOnlineMode()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding: Int = (5 * context.resources.displayMetrics.density).toInt()

        var w: Int = onlineSwitch.getTotalWidth()
        var h: Int = onlineSwitch.getTotalHeight()
        var y: Int = padding
        var x: Int = frame.width - (w + padding)

        onlineSwitch.setFrame(x, y, w, h)

        w = frame.width
        h = bottomLabelHeight
        x = 0
        y = getBottomLabelY()

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