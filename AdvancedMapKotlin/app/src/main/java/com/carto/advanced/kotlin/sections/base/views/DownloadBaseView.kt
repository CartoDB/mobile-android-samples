package com.carto.advanced.kotlin.sections.base.views

import com.carto.advanced.kotlin.R
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 03/07/2017.
 */
open class DownloadBaseView(context: android.content.Context) : MapBaseView(context) {

    val progressLabel = com.carto.advanced.kotlin.components.ProgressLabel(context)

    var onlineLayer: com.carto.layers.CartoOnlineVectorTileLayer? = null
    var offlineLayer: com.carto.layers.CartoOfflineVectorTileLayer? = null

    var manager: com.carto.packagemanager.CartoPackageManager? = null

    val switchButton = com.carto.advanced.kotlin.components.SwitchButton(context, R.drawable.icon_wifi_on, R.drawable.icon_wifi_off)
    init {

        addView(progressLabel)
        progressLabel.hide()

        addButton(switchButton)

        setOnlineMode()
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

    fun setOnlineMode() {

        if (onlineLayer == null) {
            onlineLayer = addBaseLayer(com.carto.layers.CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)
        }

        if (offlineLayer != null) {
            map.layers?.remove(offlineLayer)
        }

        map.layers?.insert(0, onlineLayer)
    }

    fun setOfflineMode(manager: com.carto.packagemanager.CartoPackageManager) {
        map.layers?.remove(onlineLayer)

        if (offlineLayer == null) {
            offlineLayer = com.carto.layers.CartoOfflineVectorTileLayer(manager, CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)
            offlineLayer!!.isPreloading = true
        }

        map.layers?.insert(0, offlineLayer)
    }

    fun setOfflineMode() {
        setOfflineMode(manager!!)
    }
}