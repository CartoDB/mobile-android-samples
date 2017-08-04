package com.carto.advanced.kotlin.sections.clustering

import android.content.Context
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.MapBaseView
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.ClusteredVectorLayer
import com.carto.vectorelements.VectorElementVector

/**
 * Created by aareundo on 03/07/2017.
 */
class ClusteringView(context: Context) : MapBaseView(context) {

    init {

        title = Texts.clusteringInfoHeader
        description = Texts.clusteringInfoContainer

        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_POSITRON)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }

    var source: LocalVectorDataSource? = null

    fun initializeClusterLayer(builder: ClusterBuilder) {
        source = LocalVectorDataSource(projection)
        val layer = ClusteredVectorLayer(source, builder)
        // Default is 100. A good value depends on data
        layer.minimumClusterDistance = 50.0f
        map.layers.add(layer)
    }

    fun addClusters(elements: VectorElementVector) {
        source?.addAll(elements)
    }
}