package com.carto.advanced.kotlin.sections.editing

import android.os.Bundle
import android.view.View
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity
import com.carto.advanced.kotlin.sections.base.utils.toCartoColor
import com.carto.advanced.kotlin.utils.Colors
import com.carto.geometry.Geometry
import com.carto.geometry.LineGeometry
import com.carto.geometry.PointGeometry
import com.carto.geometry.PolygonGeometry
import com.carto.layers.VectorEditEventListener
import com.carto.layers.VectorElementDragPointStyle
import com.carto.layers.VectorElementDragPointStyle.VECTOR_ELEMENT_DRAG_POINT_STYLE_NORMAL
import com.carto.layers.VectorElementDragPointStyle.VECTOR_ELEMENT_DRAG_POINT_STYLE_VIRTUAL
import com.carto.layers.VectorElementDragResult
import com.carto.layers.VectorElementDragResult.VECTOR_ELEMENT_DRAG_RESULT_MODIFY
import com.carto.layers.VectorElementEventListener
import com.carto.styles.PointStyle
import com.carto.styles.PointStyleBuilder
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener
import com.carto.ui.VectorElementClickInfo
import com.carto.ui.VectorElementDragInfo
import com.carto.vectorelements.Line
import com.carto.vectorelements.Point
import com.carto.vectorelements.Polygon
import com.carto.vectorelements.VectorElement

class EditingActivity : BaseActivity() {

    var contentView: EditingView? = null

    var editListener = EditEventListener()
    var selectListener = VectorElementSelectListener()
    var deselectListener = VectorElementDeselectListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = EditingView(this)
        setContentView(contentView)
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.editLayer?.vectorEditEventListener = editListener
        contentView?.editLayer?.vectorElementEventListener = selectListener
        contentView?.map?.mapEventListener = deselectListener

        contentView?.trashCan?.setOnClickListener {
            contentView?.editSource?.remove(contentView?.editLayer?.selectedVectorElement)
            deselect()
        }

        contentView?.addElements()
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.editLayer?.vectorEditEventListener = null
        contentView?.editLayer?.vectorElementEventListener = null
        contentView?.map?.mapEventListener = null

        contentView?.trashCan?.setOnClickListener(null)
    }

    fun select(element: VectorElement?) {
        contentView?.editLayer?.selectedVectorElement = element
        runOnUiThread {
            contentView?.trashCan?.visibility = View.VISIBLE
        }
    }

    fun deselect() {
        contentView?.editLayer?.selectedVectorElement = null
        runOnUiThread {
            contentView?.trashCan?.visibility = View.GONE
        }
    }

    inner class EditEventListener : VectorEditEventListener() {

        var styleNormal: PointStyle? = null
        var styleVirtual: PointStyle? = null

        init {
            val builder = PointStyleBuilder()
            builder.color = Colors.transparentLocationRed.toCartoColor()
            builder.size = 15.0f

            styleNormal = builder.buildStyle()

            builder.size = 15.0f

            styleVirtual = builder.buildStyle()
        }

        override fun onElementModify(element: VectorElement?, geometry: Geometry?) {

            if (element is Point) {
                element.geometry = geometry as PointGeometry
            } else if (element is Line) {
                element.geometry = geometry as LineGeometry
            } else if (element is Polygon) {
                element.geometry = geometry as PolygonGeometry
            }
        }

        override fun onElementDelete(element: VectorElement?) {
            contentView?.editSource?.remove(element)
        }

        override fun onDragStart(dragInfo: VectorElementDragInfo?): VectorElementDragResult {
            return VECTOR_ELEMENT_DRAG_RESULT_MODIFY
        }

        override fun onDragMove(dragInfo: VectorElementDragInfo?): VectorElementDragResult {
            return VECTOR_ELEMENT_DRAG_RESULT_MODIFY
        }

        override fun onDragEnd(dragInfo: VectorElementDragInfo?): VectorElementDragResult {
            return VECTOR_ELEMENT_DRAG_RESULT_MODIFY
        }

        override fun onSelectDragPointStyle(element: VectorElement?, dragPointStyle: VectorElementDragPointStyle?): PointStyle {

            if (dragPointStyle == VECTOR_ELEMENT_DRAG_POINT_STYLE_NORMAL) {
                return styleNormal!!
            } else if (dragPointStyle == VECTOR_ELEMENT_DRAG_POINT_STYLE_VIRTUAL) {
                return  styleVirtual!!
            }

            return styleNormal!!
        }
    }

    inner class VectorElementSelectListener : VectorElementEventListener() {

        override fun onVectorElementClicked(clickInfo: VectorElementClickInfo?): Boolean {
            select(clickInfo?.vectorElement)
            return true
        }
    }

    inner class VectorElementDeselectListener : MapEventListener() {
        override fun onMapClicked(mapClickInfo: MapClickInfo?) {
            deselect()
        }
    }
}
