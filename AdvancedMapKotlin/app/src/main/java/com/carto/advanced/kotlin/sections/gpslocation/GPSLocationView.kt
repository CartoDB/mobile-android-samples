package com.carto.advanced.kotlin.sections.gpslocation

import android.content.Context
import com.carto.advanced.kotlin.components.StateSwitch
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.layers.CartoBaseMapStyle

/**
 * Created by aareundo on 03/07/2017.
 */
class GPSLocationView(context: Context) : MapBaseView(context) {

    var switch = StateSwitch(context)

    init {
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY)

        addView(switch)
        switch.setText(" TRACK LOCATION")

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val padding: Int = (5 * context.resources.displayMetrics.density).toInt()

        val w: Int = switch.getTotalWidth()
        val h: Int = switch.getTotalHeight()
        val y: Int = padding
        val x: Int = frame.width - (w + padding)

        switch.setFrame(x, y, w, h)
    }
}