package com.carto.advanced.kotlin.sections.base.views

import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.sections.base.utils.setFrame
import android.content.Context
/**
 * Created by aareundo on 30/06/2017.
 */

open class MapBaseView(context: Context) : BaseView(context) {

    companion object {
        // Content descriptions for auto tests
        val INFO_BUTTON_DESCRIPTION = "info_button"
        val MAP_DESCRIPTION = "map_view"
    }

    var topBanner: com.carto.advanced.kotlin.components.Banner = com.carto.advanced.kotlin.components.Banner(context)

    var map: com.carto.ui.MapView = com.carto.ui.MapView(context)

    val popup = com.carto.advanced.kotlin.components.popup.SlideInPopup(context)

    var projection: com.carto.projections.Projection? = null

    val infoButton = com.carto.advanced.kotlin.components.PopupButton(context, R.drawable.icon_info)
    val infoContent = com.carto.advanced.kotlin.components.popupcontent.InformationPopupContent(context)

    val buttons: MutableList<com.carto.advanced.kotlin.components.PopupButton> = mutableListOf()

    init {
        map.options.isZoomGestures = true
        map.options.clearColor = com.carto.graphics.Color(0, 0, 0, 255)

        projection = map.options.baseProjection

        addView(popup)

        addView(map)

        topBanner.alpha = 0.0f
        addView(topBanner)

        addButton(infoButton)

        setMainViewFrame()

        infoButton.contentDescription = INFO_BUTTON_DESCRIPTION
        map.contentDescription = MAP_DESCRIPTION
    }

    val bottomLabelHeight: Int = (40 * getMetrics().density).toInt()
    val smallPadding: Int = (5 * getMetrics().density).toInt()

    val buttonSize: Int = (60 * getMetrics().density).toInt()

    override fun layoutSubviews() {

        var x: Int = 0
        var y: Int = 0
        var w: Int = frame.width
        var h: Int = frame.height

        map.setFrame(x, y, w, h)

        popup.setFrame(x, y, w, h)

        val count = buttons.count()
        val buttonWidth = buttonSize
        val innerPadding: Int = (25 * getMetrics().density).toInt()

        val totalArea: Int = buttonWidth * count + (innerPadding * (count - 1))

        w = buttonWidth
        h = w
        y = frame.height - (h + smallPadding + bottomLabelHeight)
        x = frame.width / 2 - totalArea / 2

        for (button in buttons) {
            button.setFrame(x, y, w, h)
            x += w + innerPadding
        }

        x = 0
        y = 0
        w = frame.width
        h = (50 * getDensity()).toInt()

        topBanner.setFrame(x, y, w, h)
    }

    var title = ""
    var description = ""

    open fun addListeners() {
        infoButton.setOnClickListener {
            popup.setPopupContent(infoContent)
            popup.popup.header.setText("INFORMATION")
            infoContent.setTitle(title)
            infoContent.setDescription(description)
            popup.show()
        }
    }

    open fun removeListeners() {
        infoButton.setOnClickListener(null)
    }

    fun addButton(button: com.carto.advanced.kotlin.components.PopupButton) {
        buttons.add(button)
        addView(button)
    }

    fun removeButton(button: com.carto.advanced.kotlin.components.PopupButton) {
        if (buttons.contains(button)) {
            buttons.remove(button)
            removeView(button)
        }
    }

    fun addBaseLayer(style: com.carto.layers.CartoBaseMapStyle): com.carto.layers.CartoOnlineVectorTileLayer {

        val layer = com.carto.layers.CartoOnlineVectorTileLayer(style)
        layer.isPreloading = true
        map.layers.add(layer)
        return layer
    }

    fun showBanner(text: String) {
        topBanner.setText(text)
        layoutSubviews()
    }
}