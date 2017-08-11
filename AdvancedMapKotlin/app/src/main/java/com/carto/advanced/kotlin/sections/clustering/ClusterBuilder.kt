package com.carto.advanced.kotlin.sections.clustering

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import com.carto.core.MapPos
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
    var elements: MutableMap<Long, MarkerStyle> = mutableMapOf()

    override fun buildClusterElement(mapPos: MapPos?, elements: VectorElementVector?): VectorElement {

        val count = elements?.size()!!
        var style = findByKey(count)

        if (count <= 1.0) {
            style = (elements.get(0) as Marker).style
        }

        if (style == null) {
            val canvasBitmap = image?.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(canvasBitmap)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 12.0f
            paint.color = Color.BLACK

            val x: Float = (image?.width!! / 2).toFloat()
            val y: Float = (image?.height!! / 2).toFloat() - 5.0f

            val text = count.toString()
            canvas.drawText(text, x, y, paint)

            val builder = MarkerStyleBuilder()
            builder.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(canvasBitmap)
            builder.size = 30.0f
            builder.placementPriority = -count.toInt()

            style = builder.buildStyle()

            this.elements.put(count, style)
        }

        return Marker(mapPos, style)
    }

    fun findByKey(count: Long): MarkerStyle? {

        if (elements.containsKey(count)) {
            return elements[count]!!
        }

        return null
    }
}