package com.carto.advanced.kotlin.sections.vectorelement

import com.carto.advanced.kotlin.sections.base.toCartoColor
import com.carto.advanced.kotlin.utils.Colors
import com.carto.datasources.LocalVectorDataSource
import com.carto.graphics.Color
import com.carto.layers.VectorElementEventListener
import com.carto.styles.BalloonPopupMargins
import com.carto.styles.BalloonPopupStyleBuilder
import com.carto.ui.VectorElementClickInfo
import com.carto.vectorelements.BalloonPopup

/**
 * Created by aareundo on 12/07/2017.
 */
class VectorObjectClickListener(val source: LocalVectorDataSource) : VectorElementEventListener() {

    companion object {
        val CLICK_TITLE = "click_title"
        val CLICK_DESCRIPTION = "click_description"
    }

    var previous: BalloonPopup? = null

    override fun onVectorElementClicked(clickInfo: VectorElementClickInfo?): Boolean {

        if (previous != null) {
            source.remove(previous)
        }

        val element = clickInfo?.vectorElement!!

        val builder = BalloonPopupStyleBuilder()
        builder.leftMargins = BalloonPopupMargins(0, 0, 0, 0)
        builder.rightMargins = BalloonPopupMargins(6, 3, 6, 3)
        builder.titleColor = Colors.navy.toCartoColor()
        builder.titleFontSize = 12
        builder.descriptionColor = android.graphics.Color.GRAY.toCartoColor()
        builder.descriptionFontSize = 10
        builder.cornerRadius = 5

        val style = builder.buildStyle()
        val title = element.getMetaDataElement(CLICK_TITLE).string
        val description = element.getMetaDataElement(CLICK_DESCRIPTION).string

        var popup: BalloonPopup?

        if (element is BalloonPopup) {
            popup = BalloonPopup(element, style, title, description)
        } else {
            popup = BalloonPopup(clickInfo.clickPos, style, title, description)
        }

        source.add(popup)
        previous = popup

        return true
    }

    fun reset() {
        if (previous != null) {
            source.remove(previous)
            previous = null
        }
    }
}