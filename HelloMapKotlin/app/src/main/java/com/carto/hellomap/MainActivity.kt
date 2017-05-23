package com.carto.hellomap

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.core.Variant
import com.carto.datasources.LocalVectorDataSource
import com.carto.graphics.Color
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.layers.VectorLayer
import com.carto.projections.Projection
import com.carto.styles.LineJoinType
import com.carto.styles.LineStyleBuilder
import com.carto.styles.MarkerStyleBuilder
import com.carto.styles.PointStyleBuilder
import com.carto.ui.MapView
import com.carto.vectorelements.Line
import com.carto.vectorelements.Marker
import com.carto.vectorelements.Point

class MainActivity : AppCompatActivity() {

    val LICENSE = "XTUMwQ0ZDU2hUalFxWm1EbGo4TWpXMjYxaER3dk5BeTJBaFVBb0VTWVIyUzAySEl4QjZVWTdBKzkydEc4L3ZjPQoKYXBwVG9rZW49OTE1OTg4MDktYTlmMC00ZTMwLWI0OGUtZGQ5MWMwZmI2MTUyCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5oZWxsb21hcApvbmxpbmVMaWNlbnNlPTEKcHJvZHVjdHM9c2RrLWFuZHJvaWQtNC4qCndhdGVybWFyaz1jdXN0b20K"

    var mapView: MapView? = null
    var projection: Projection? = null
    var source: LocalVectorDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapView.registerLicense(LICENSE, this)

        mapView = MapView(this)
        setContentView(mapView)

        mapView?.layers?.add(CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT))

        // Get base projection from mapView
        projection = mapView?.options?.baseProjection
        // Create a local vector data source
        source = LocalVectorDataSource(projection)
        // Initialize layer
        val layer = VectorLayer(source)
        mapView?.layers?.add(layer)

        // 1. Position in latitude/longitude has to be converted using projection
        // 2. X is longitude, Y is latitude
        val tallinn = projection?.fromWgs84(MapPos(24.646469, 59.426939))

        addLine(tallinn)
        addPoint(tallinn)
        addMarker(tallinn)
    }

    fun addMarker(position: MapPos?) {

        val builder = MarkerStyleBuilder()
        builder.size = 30F
        val red = Color(255, 0, 0, 255)
        builder.color = red

        val style = builder.buildStyle()

        val marker = Marker(position, style)

        // The defined metadata will be used later for Popups
        marker.setMetaDataElement("ClickText", Variant("Marker"))
        source?.add(marker)

        mapView?.setFocusPos(position, 0F)
        mapView?.setZoom(12F, 1F)

    }

    fun addPoint(position: MapPos?) {

        // 1. Create style and position for the Point
        val builder = PointStyleBuilder()
        builder.color = Color(0, 255, 0, 255)
        builder.size = 16F

        // 2. Create Point, add to datasource with metadata
        val point1 = Point(position, builder.buildStyle())
        point1.setMetaDataElement("ClickText", Variant("Point nr 1"))

        source?.add(point1)

        // 4. Animate map to the point
        mapView?.setFocusPos(position, 1F)
        mapView?.setZoom(12F, 1F)
    }

    fun addLine(position: MapPos?) {

        // 1. Create line style, and line poses
        val lineStyleBuilder = LineStyleBuilder()
        lineStyleBuilder.color = Color(255, 0, 0, 255)
        // Define how lines are joined
        lineStyleBuilder.lineJoinType = LineJoinType.LINE_JOIN_TYPE_ROUND
        lineStyleBuilder.width = 8F

        // 2. Special MapPosVector must be used for coordinates
        val linePoses = MapPosVector()
        val initial = projection?.fromWgs84(MapPos(24.645565, 59.422074))

        // 3. Add positions
        linePoses.add(initial)
        linePoses.add(projection?.fromWgs84(MapPos(24.643076, 59.420502)));
        linePoses.add(projection?.fromWgs84(MapPos(24.645351, 59.419149)));
        linePoses.add(projection?.fromWgs84(MapPos(24.648956, 59.420393)));
        linePoses.add(projection?.fromWgs84(MapPos(24.650887, 59.422707)));

        // 4. Add a line
        val line1 = Line(linePoses, lineStyleBuilder.buildStyle());
        line1.setMetaDataElement("ClickText", Variant("Line nr 1"))
        source?.add(line1)

        // 5. Animate map to the line
        mapView?.setFocusPos(position, 1F)
        mapView?.setZoom(12F, 1F)
    }
}
