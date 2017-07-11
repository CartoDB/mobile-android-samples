package com.carto.advanced.kotlin.sections.clustering

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.sections.base.BaseActivity
import com.carto.geometry.GeoJSONGeometryReader
import com.carto.geometry.PointGeometry
import com.carto.styles.MarkerStyleBuilder
import com.carto.utils.BitmapUtils
import com.carto.vectorelements.Marker
import com.carto.vectorelements.VectorElementVector
import org.jetbrains.anko.doAsync

class ClusteringActivity : BaseActivity() {

    var contentView: ClusteringView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = ClusteringView(this)
        setContentView(contentView)

        val cBuilder = ClusterBuilder()
        cBuilder.image = BitmapFactory.decodeResource(resources, R.drawable.marker_black)

        contentView?.initializeClusterLayer(cBuilder)

        // Kotlin Anko library: https://github.com/Kotlin/anko
        doAsync {

            alert("Reading .geojson from assets")
            val json = getJsonFromAssets()

            // This is the style of a non-cluster element
            // This element will be displayed when clustering animation completes and it's no longer a cluster
            val mBuilder = MarkerStyleBuilder()
            mBuilder.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(cBuilder.image)
            mBuilder.size = 30.0f

            val style = mBuilder.buildStyle()

            // Read GeoJSON, parse it using SDK GeoJSON parser
            val reader = GeoJSONGeometryReader()
            reader.targetProjection = contentView?.projection

            alert("Parsing .geojson to feature collection")
            val features = reader.readFeatureCollection(json)

            alert("Clustering...")
            val elements = VectorElementVector()
            val total = features.featureCount

            for(i in 0..total - 1) {
                // This data set features point geometry, however, it can also be LineGeometry or PolygonGeometry
                val geometry = features.getFeature(i).geometry as PointGeometry
                elements.add(Marker(geometry, style))
            }

            runOnUiThread {
                contentView?.addClusters(elements)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()
    }

    fun getJsonFromAssets(): String {

        val filename = "cities15000.geojson"

        val stream = assets.open(filename)
        val size = stream.available()
        val buffer = ByteArray(size)

        stream.read(buffer)
        stream.close()

        return String(buffer, charset("UTF-8"))
    }
}
