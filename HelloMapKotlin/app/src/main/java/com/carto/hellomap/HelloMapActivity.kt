package com.carto.hellomap

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carto.components.RenderProjectionMode
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.core.Variant
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.layers.VectorLayer
import com.carto.projections.EPSG4326
import com.carto.projections.Projection
import com.carto.styles.LineJoinType
import com.carto.styles.LineStyleBuilder
import com.carto.styles.MarkerStyleBuilder
import com.carto.styles.PointStyleBuilder
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener
import com.carto.ui.MapView
import com.carto.vectorelements.Line
import com.carto.vectorelements.Marker
import com.carto.vectorelements.Point
import java.util.*

class HelloMapActivity : AppCompatActivity() {

    val LICENSE = "XTUMwQ0ZDU2hUalFxWm1EbGo4TWpXMjYxaER3dk5BeTJBaFVBb0VTWVIyUzAySEl4QjZVWTdBKzkydEc4L3ZjPQoKYXBwVG9rZW49OTE1OTg4MDktYTlmMC00ZTMwLWI0OGUtZGQ5MWMwZmI2MTUyCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5oZWxsb21hcApvbmxpbmVMaWNlbnNlPTEKcHJvZHVjdHM9c2RrLWFuZHJvaWQtNC4qCndhdGVybWFyaz1jdXN0b20K"

    var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapView.registerLicense(LICENSE, this)

        // Initialize mapView
        mapView = MapView(this)
        setContentView(mapView)
        setTitle("HelloMap")

        // Set map view options
        val proj = EPSG4326()
        mapView?.options?.baseProjection = proj
        mapView?.options?.renderProjectionMode = RenderProjectionMode.RENDER_PROJECTION_MODE_SPHERICAL
        mapView?.options?.isZoomGestures = true

        // Tallinn coordinates as longitude/latitude
        val tallinn = MapPos(24.646469, 59.426939)

        // Focus and zoom to Tallinn
        mapView?.setFocusPos(tallinn, 0.0f)
        mapView?.setZoom(2.0f, 0.0f)
        mapView?.setZoom(4.0f, 2.0f)

        // Create basemap layer and add it to mapView
        val baseLayer = CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER)
        mapView?.layers?.add(baseLayer)

        // Create data source and layer for markers
        val dataSource = LocalVectorDataSource(proj)
        val vectorLayer = VectorLayer(dataSource)
        mapView?.layers?.add(vectorLayer)

        // Create a style for markers
        val styleBuilder = MarkerStyleBuilder()
        styleBuilder.size = 30F
        styleBuilder.color = com.carto.graphics.Color(255, 0, 0, 255)
        val style = styleBuilder.buildStyle()

        // Add a marker to vector layer by adding it to the data source
        val marker = Marker(tallinn, style)
        dataSource.add(marker)

        // Connect custom map event listener to receive clicks on the map
        mapView?.mapEventListener = MyMapEventListener(dataSource)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Disconnect listeners to avoid memory leaks
        mapView?.mapEventListener = null
    }

    /*********************
     MAP CLICK LISTENER
     **********************/
    private class MyMapEventListener : MapEventListener {
        var dataSource: LocalVectorDataSource? = null
        var random: Random = Random()
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)

        public constructor(dataSource: LocalVectorDataSource) {
            this.dataSource = dataSource
        }

        override fun onMapClicked(mapClickInfo: MapClickInfo?) {
            super.onMapClicked(mapClickInfo)

            // Create a random style for markers
            val styleBuilder = MarkerStyleBuilder()
            styleBuilder.size = random.nextFloat() * 30.0f + 15.0f
            styleBuilder.color = com.carto.graphics.Color(colors[random.nextInt(colors.size)])
            val style = styleBuilder.buildStyle()

            // Add a marker to vector layer by adding it to the data source
            val marker = Marker(mapClickInfo?.clickPos, style)
            dataSource?.add(marker)
        }
    }
}
