package com.carto.advanced.kotlin.sections.gpslocation

import android.content.Context
import android.location.Location
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.StateSwitch
import com.carto.advanced.kotlin.components.SwitchButton
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.VectorLayer
import com.carto.styles.MarkerStyleBuilder
import com.carto.utils.BitmapUtils
import com.carto.vectorelements.Marker

/**
 * Created by aareundo on 03/07/2017.
 */
class GPSLocationView(context: Context) : MapBaseView(context) {

    var switch = SwitchButton(context, R.drawable.icon_track_location_on, R.drawable.icon_track_location_off)

    var source: LocalVectorDataSource? = null

    init {

        title = Texts.gpsLocationInfoHeader
        description = Texts.gpsLocationInfoContainer

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY)

        addButton(switch)

        source = LocalVectorDataSource(projection)
        val layer = VectorLayer(source)
        map.layers.add(layer)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    var userMarker: Marker? = null

    fun showUserAt(location: Location) {

        val latitude = location.latitude
        val longitude = location.longitude

        val position = projection?.fromWgs84(MapPos(longitude, latitude))
        map.setFocusPos(position, 1.0f)
        map.setZoom(16.0f, 1.0f)

        if (userMarker == null) {
            val builder = MarkerStyleBuilder()
            val bitmap = Utils.resourceToBitmap(context.resources, R.drawable.icon_marker_blue)
            builder.bitmap = bitmap
            builder.size = 25.0f

            userMarker = Marker(position, builder.buildStyle())
            source?.add(userMarker)
        }

        userMarker?.setPos(position)
    }
}