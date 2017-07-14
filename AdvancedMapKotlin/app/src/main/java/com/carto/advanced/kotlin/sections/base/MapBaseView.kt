package com.carto.advanced.kotlin.sections.base

import com.carto.advanced.kotlin.MapApplication
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.components.popup.SlideInPopup
import com.carto.advanced.kotlin.components.popupcontent.InformationPopupContent
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.projections.Projection

/**
 * Created by aareundo on 30/06/2017.
 */

open class MapBaseView(context: android.content.Context) : BaseView(context) {

    var topBanner: Banner = Banner(context)

    var map: com.carto.ui.MapView = com.carto.ui.MapView(context)

    val popup = SlideInPopup(context)

    var projection: Projection? = null

    val infoButton = PopupButton(context, R.drawable.icon_info)
    val infoContent = InformationPopupContent(context)

    val buttons: MutableList<PopupButton> = mutableListOf()

    val topBar = BaseView(context)

    init {

        projection = map.options.baseProjection

        addView(popup)

        addView(map)

        addView(topBanner)

        addButton(infoButton)

        setMainViewFrame()
    }

    val bottomLabelHeight: Int = (40 * getMetrics().density).toInt()
    val smallPadding: Int = (5 * getMetrics().density).toInt()

    fun getBottomLabelY(): Int {
        // I have no idea why this is necessary
        val bonus: Int = (8 * context.resources.displayMetrics.density).toInt()
        val unusableArea = MapApplication.navigationBarHeight!! + MapApplication.statusBarHeight!! + bonus
        return frame.height - (bottomLabelHeight + unusableArea)
    }

    override fun layoutSubviews() {

        var x: Int = 0
        var y: Int = 0
        var w: Int = frame.width
        var h: Int = frame.height

        map.setFrame(x, y, w, h)

        popup.setFrame(x, y, w, h)

        val count = buttons.count()
        val buttonWidth: Int = (60 * getMetrics().density).toInt()
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
        h = frame.height / 12

        topBanner.setFrame(x, y, w, h)
    }

    var title = ""
    var description = ""

    open fun addListeners() {
        infoButton.setOnClickListener {
            popup.setPopupContent(infoContent)
            popup.popup.header.setText("INFORMATION")
            infoContent.setTitle(title!!)
            infoContent.setDescription(description!!)
            popup.show()
        }
    }

    open fun removeListeners() {
        infoButton.setOnClickListener(null)
    }

    fun addButton(button: PopupButton) {
        buttons.add(button)
        addView(button)
    }

    fun addBaseLayer(style: CartoBaseMapStyle): CartoOnlineVectorTileLayer {

        val layer = CartoOnlineVectorTileLayer(style)
        map.layers.add(layer)
        return layer
    }

    fun showBanner(text: String) {
        topBanner.setText(text)
        layoutSubviews()
    }
}