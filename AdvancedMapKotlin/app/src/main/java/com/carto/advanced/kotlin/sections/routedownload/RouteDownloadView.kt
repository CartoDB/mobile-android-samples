package com.carto.advanced.kotlin.sections.routedownload

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.DownloadBaseView
import com.carto.advanced.kotlin.utils.BoundingBox
import com.carto.core.MapBounds
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.VectorLayer
import com.carto.packagemanager.PackageInfo
import com.carto.styles.PolygonStyleBuilder
import com.carto.vectorelements.Polygon

/**
 * Created by aareundo on 03/07/2017.
 */
class RouteDownloadView(context: Context) : DownloadBaseView(context) {

    val overlaySource = LocalVectorDataSource(projection)
    val overlayLayer = VectorLayer(overlaySource)

    val downloadButton = PopupButton(context, R.drawable.icon_download)

    init {

        title = Texts.routeDownloadInfoHeader
        description = Texts.routeDownloadInfoContainer

        addButton(downloadButton)

        layoutSubviews()

        map.layers.add(overlayLayer)

        val washingtonDC = projection?.fromWgs84(MapPos(-77.0369, 38.9072))
        map.setFocusPos(washingtonDC, 0.0f)
        map.setZoom(8.0f, 0.0f)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    fun addPolygonTo(bounds: MapBounds) {

        val builder = PolygonStyleBuilder()
        builder.color = com.carto.graphics.Color(100, 100, 230, 60)

        val positions = MapPosVector()

        positions.add(MapPos(bounds.min.x, bounds.min.y))
        positions.add(MapPos(bounds.max.x, bounds.min.y))
        positions.add(MapPos(bounds.max.x, bounds.max.y))
        positions.add(MapPos(bounds.min.x, bounds.max.y))

        val polygon = Polygon(positions, builder.buildStyle())
        overlaySource.add(polygon)
    }

    fun addPolygonsTo(list: MutableList<PackageInfo>) {

        for (item in list) {
            val id = item.packageId

            if (id.contains(BoundingBox.identifier)) {
                val bounds = BoundingBox.fromString(projection!!, id).bounds!!
                addPolygonTo(bounds)
            }
        }
    }
}