package com.carto.advanced.kotlin.sections.base.views

import com.carto.advanced.kotlin.R
import com.carto.layers.CartoBaseMapStyle
import android.content.Context
/**
 * Created by aareundo on 03/07/2017.
 */
open class DownloadBaseView(context: Context, withBaseLayer: Boolean = true) : MapBaseView(context) {

    val progressLabel = com.carto.advanced.kotlin.components.ProgressLabel(context)

    var onlineLayer: com.carto.layers.CartoOnlineVectorTileLayer? = null
    var offlineLayer: com.carto.layers.CartoOfflineVectorTileLayer? = null

    var manager: com.carto.packagemanager.CartoPackageManager? = null

    val switchButton = com.carto.advanced.kotlin.components.SwitchButton(context, R.drawable.icon_wifi_on, R.drawable.icon_wifi_off)
    init {

        addView(progressLabel)
        progressLabel.hide()

        addButton(switchButton)

        if (withBaseLayer) {
            setOnlineMap()
        }
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val w = frame.width
        val h = bottomLabelHeight
        val x = 0
        val y = frame.height - h

        progressLabel.setFrame(x, y, w, h)
    }

    fun removeSwitch() {
        removeButton(switchButton)
    }

    fun setOnlineMap() {

        if (onlineLayer == null) {
            onlineLayer = addBaseLayer(com.carto.layers.CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)
        }

        if (offlineLayer != null) {
            map.layers?.remove(offlineLayer)
        }

        map.layers?.insert(0, onlineLayer)
    }

    fun setOfflineMap(manager: com.carto.packagemanager.CartoPackageManager) {
        map.layers?.remove(onlineLayer)

        if (offlineLayer == null) {
            offlineLayer = com.carto.layers.CartoOfflineVectorTileLayer(manager, CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)
            offlineLayer!!.isPreloading = true
        }

        map.layers?.insert(0, offlineLayer)
    }

    fun setOfflineMap() {
        setOfflineMap(manager!!)
    }
}