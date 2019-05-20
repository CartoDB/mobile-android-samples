package com.carto.advanced.kotlin.sections.clustering

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import com.carto.core.MapPos
import com.carto.layers.ClusterBuilderMode
import com.carto.layers.ClusterElementBuilder
import com.carto.styles.MarkerStyle
import com.carto.styles.MarkerStyleBuilder
import com.carto.utils.BitmapUtils
import com.carto.vectorelements.Marker
import com.carto.vectorelements.VectorElement
import com.carto.vectorelements.VectorElementVector

/**
 * Created by aareundo on 11/07/2017.
 */
class ClusterBuilder : ClusterElementBuilder() {

    var image: Bitmap? = null
    var elements: MutableMap<Int, MarkerStyle> = mutableMapOf()

    override fun getBuilderMode(): ClusterBuilderMode {
        return ClusterBuilderMode.CLUSTER_BUILDER_MODE_ELEMENT_COUNT
    }

    override fun buildClusterElement(mapPos: MapPos?, elementCount: Int): VectorElement {
        var style = findByKey(elementCount)

        if (style == null) {
            val canvasBitmap = image?.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(canvasBitmap)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 12.0f
            paint.color = Color.BLACK

            val x: Float = (image?.width!! / 2).toFloat()
            val y: Float = (image?.height!! / 2).toFloat() - 5.0f

            val text = elementCount.toString()
            canvas.drawText(text, x, y, paint)

            val builder = MarkerStyleBuilder()
            builder.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(canvasBitmap)
            builder.size = 30.0f
            builder.placementPriority = elementCount.toInt()

            style = builder.buildStyle()

            this.elements.put(elementCount, style)
        }

        return Marker(mapPos, style)
    }

    override fun buildClusterElement(mapPos: MapPos?, elements: VectorElementVector): VectorElement {
        if (elements.size().toInt() == 1) {
            val style = (elements.get(0) as Marker).style
            return Marker(mapPos, style)
        }

        return buildClusterElement(mapPos, elements.size().toInt())
    }

    fun findByKey(count: Int): MarkerStyle? {

        if (elements.containsKey(count)) {
            return elements[count]!!
        }

        return null
    }
}