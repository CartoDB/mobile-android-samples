package com.carto.advanced.kotlin.sections.routesearch

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.components.PopupButton
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.MapBaseView
import com.carto.advanced.kotlin.sections.vectorelement.VectorObjectClickListener
import com.carto.core.MapPos
import com.carto.core.Variant
import com.carto.datasources.LocalVectorDataSource
import com.carto.datasources.TileDataSource
import com.carto.geometry.PointGeometry
import com.carto.geometry.VectorTileFeature
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.VectorLayer
import com.carto.layers.VectorTileLayer
import com.carto.search.VectorTileSearchService
import com.carto.styles.PointStyleBuilder
import com.carto.vectorelements.Point

/**
 * Created by mark on 11/08/2017.
 */
class RouteSearchView(context: Context) : MapBaseView(context) {

    var baseSource: TileDataSource? = null
    var baseLayer: VectorTileLayer? = null
    var searchService: VectorTileSearchService? = null

    val overlaySource = LocalVectorDataSource(projection)
    val overlayLayer = VectorLayer(overlaySource)

    init {

        title = Texts.routeSearchInfoHeader
        description = Texts.routeSearchInfoContainer

        layoutSubviews()

        baseLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_POSITRON)
        baseSource = baseLayer!!.dataSource;
        searchService = VectorTileSearchService(baseSource, baseLayer?.tileDecoder)

        map.layers.add(overlayLayer)

        val washingtonDC = projection?.fromWgs84(MapPos(-77.0369, 38.9072))
        map.setFocusPos(washingtonDC, 0.0f)
        map.setZoom(14.0f, 0.0f)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    fun clearPOIs() {
        overlaySource.clear()
    }

    fun addPOITo(feature: VectorTileFeature) {
        val builder = PointStyleBuilder()
        builder.size = 20.0f
        builder.color = com.carto.graphics.Color(230, 100, 100, 200)

        val point = Point((feature.geometry as PointGeometry).pos, builder.buildStyle())
        val titleKey = VectorObjectClickListener.CLICK_TITLE
        val descriptionKey = VectorObjectClickListener.CLICK_DESCRIPTION
        point.setMetaDataElement(titleKey, Variant("Properties"))
        point.setMetaDataElement(descriptionKey, Variant(feature.properties.toString()))
        overlaySource.add(point)
    }
}