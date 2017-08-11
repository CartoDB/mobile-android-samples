package com.carto.advanced.kotlin.sections.vectorelement

import com.carto.advanced.kotlin.sections.base.utils.toCartoColor
import com.carto.advanced.kotlin.utils.Colors
import com.carto.core.VariantType
import com.carto.datasources.LocalVectorDataSource
import com.carto.layers.VectorElementEventListener
import com.carto.styles.AnimationStyleBuilder
import com.carto.styles.AnimationType
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
            previous = null
        }

        val element = clickInfo?.vectorElement!!
        if (element.getMetaDataElement(CLICK_TITLE).type != VariantType.VARIANT_TYPE_STRING) {
            return true;
        }

        val animationBuilder = AnimationStyleBuilder()
        animationBuilder.relativeSpeed = 2.0f
        animationBuilder.sizeAnimationType = AnimationType.ANIMATION_TYPE_SPRING

        val builder = BalloonPopupStyleBuilder()
        builder.leftMargins = BalloonPopupMargins(0, 0, 0, 0)
        builder.rightMargins = BalloonPopupMargins(6, 3, 6, 3)
        builder.titleColor = Colors.navy.toCartoColor()
        builder.titleFontSize = 12
        builder.descriptionColor = android.graphics.Color.GRAY.toCartoColor()
        builder.descriptionFontSize = 10
        builder.cornerRadius = 5
        builder.animationStyle = animationBuilder.buildStyle()

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