package com.carto.advanced.kotlin.sections.vectorelement

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.MapBaseView
import com.carto.advanced.kotlin.sections.base.toCartoColor
import com.carto.advanced.kotlin.utils.Colors
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.core.MapPosVectorVector
import com.carto.core.Variant
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.layers.VectorLayer
import com.carto.styles.*
import com.carto.utils.AssetUtils
import com.carto.vectorelements.*

/**
 * Created by aareundo on 03/07/2017.
 */
class VectorElementView(context: Context) : MapBaseView(context) {

    var baseLayer: CartoOnlineVectorTileLayer? = null
    var source: LocalVectorDataSource? = null
    var objectLayer: VectorLayer? = null

    init {

        title = Texts.vectorElementsInfoHeader
        description = Texts.vectorElementsInfoContainer

        baseLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT)

        source = LocalVectorDataSource(projection)
        objectLayer = VectorLayer(source)
        map.layers.add(objectLayer)

        val longitude = -77.0369
        val latitude = 38.9072
        val washingtonDC = projection?.fromWgs84(MapPos(longitude, latitude))

        // Set specific focus, zoom, tilt and rotation for an optimal initial view
        map.setFocusPos(washingtonDC, 0.0f)
        map.setZoom(14.5f, 0.0f)
        map.setTilt(20.0f, 0.0f)
        map.rotate(-30.0f, 0.0f)

        /*
         * Milktruck NML Model
         */
        val data = AssetUtils.loadAsset("milktruck.nml")
        val model = NMLModel(washingtonDC, data)
        model.scale = 20.0f

        val titleKey = VectorObjectClickListener.CLICK_TITLE
        val descriptionKey = VectorObjectClickListener.CLICK_DESCRIPTION

        model.setMetaDataElement(titleKey, Variant("Milktruck"))
        model.setMetaDataElement(descriptionKey, Variant("This is an nml file loaded from bundled assets"))
        source?.add(model)

        /*
         * 3D Polygon
         */
        val builder3D = Polygon3DStyleBuilder()
        builder3D.color = Colors.transparentNavy.toCartoColor()

        val positions = MapPosVector()
        positions.add(projection?.fromWgs84(MapPos(longitude + 0.001, latitude - 0.001)))
        positions.add(projection?.fromWgs84(MapPos(longitude + 0.0015, latitude - 0.001)))
        positions.add(projection?.fromWgs84(MapPos(longitude + 0.002, latitude - 0.001)))
        positions.add(projection?.fromWgs84(MapPos(longitude + 0.002, latitude - 0.002)))
        positions.add(projection?.fromWgs84(MapPos(longitude + 0.0015, latitude - 0.001)))
        positions.add(projection?.fromWgs84(MapPos(longitude + 0.001, latitude - 0.002)))

        val holes = MapPosVectorVector()

        val polygon = Polygon3D(positions, holes, builder3D.buildStyle(), 150.0f)
        polygon.setMetaDataElement(titleKey, Variant("3D Polygon"))
        polygon.setMetaDataElement(descriptionKey, Variant("This a gray 3D polygon"))
        source?.add(polygon)

        /*
         * Balloon Popup
         */
        val balloonBuilder = BalloonPopupStyleBuilder()

        balloonBuilder.leftMargins = BalloonPopupMargins(6, 6, 6, 6)
        balloonBuilder.leftImage = Utils.resourceToBitmap(context.resources, R.drawable.info)

        balloonBuilder.rightMargins = BalloonPopupMargins(2, 6, 12, 6)
        balloonBuilder.rightImage = Utils.resourceToBitmap(context.resources, R.drawable.arrow)

        balloonBuilder.placementPriority = 1
        balloonBuilder.titleFontSize = 15
        balloonBuilder.descriptionFontSize = 12

        var position = projection?.fromWgs84(MapPos(longitude + 0.003, latitude + 0.003))
        val balloonPopup = BalloonPopup(position, balloonBuilder.buildStyle(), "Balloon popup", "Look at me, whee!")
        balloonPopup.setMetaDataElement(titleKey, Variant("Did you just click me?"))
        balloonPopup.setMetaDataElement(descriptionKey, Variant("You'd better not try that again"))
        source?.add(balloonPopup)

        /*
         * Line
         */
        val lineBuilder = LineStyleBuilder()
        positions.add(projection?.fromWgs84(MapPos(longitude - 0.0015, latitude - 0.001)))
        positions.add(projection?.fromWgs84(MapPos(longitude - 0.001, latitude - 0.002)))
        lineBuilder.color = Colors.green.toCartoColor()
        val line = Line(positions, lineBuilder?.buildStyle())
        line.setMetaDataElement(titleKey, Variant("Hi! They call me Line!"))
        val description = Variant("I'm just a little fatso line between the point and the car. I dislike the car")
        line.setMetaDataElement(descriptionKey, description)
        source?.add(line)

        /*
         * Marker
         */
        val markerBuilder = MarkerStyleBuilder()
        markerBuilder.color = Colors.appleBlue.toCartoColor()
        markerBuilder.size = 15.0f
        markerBuilder.bitmap = Utils.resourceToBitmap(resources, R.drawable.icon_pin_red)
        position = projection?.fromWgs84(MapPos(longitude - 0.005, latitude - 0.005))
        val marker = Marker(position, markerBuilder.buildStyle())
        marker.setMetaDataElement(titleKey, Variant("Hi!"))
        marker.setMetaDataElement(descriptionKey, Variant("I'm a dark blue marker, my name is Mark"))
        source?.add(marker)

        /*
         * Point
         */
        val pointBuilder = PointStyleBuilder()
        pointBuilder.color = Colors.locationRed.toCartoColor()
        position = projection?.fromWgs84(MapPos(longitude - 0.003, latitude - 0.003))
        val point = Point(position, pointBuilder.buildStyle())
        point.setMetaDataElement(titleKey, Variant("Hi!"))
        point.setMetaDataElement(descriptionKey, Variant("I'm just a red dot lying on the ground"))
        source?.add(point)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}