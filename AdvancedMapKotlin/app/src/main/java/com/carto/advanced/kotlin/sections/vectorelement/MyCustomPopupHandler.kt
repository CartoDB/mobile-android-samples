package com.carto.advanced.kotlin.sections.vectorelement

import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.carto.ui.PopupClickInfo
import com.carto.ui.PopupDrawInfo
import com.carto.utils.BitmapUtils
import com.carto.vectorelements.CustomPopupHandler

/**
 * Created by aareundo on 22/09/2017.
 */
/**
 * Simple text-based popup.
 */
class MyCustomPopupHandler internal constructor(var text: String?) : CustomPopupHandler() {

    override fun onDrawPopup(drawInfo: PopupDrawInfo): com.carto.graphics.Bitmap {
        val style = drawInfo.popup.style

        // Calculate scaled dimensions
        var dpToPX = drawInfo.dpToPX
        var pxToDP = 1 / dpToPX

        if (style.isScaleWithDPI) {
            dpToPX = 1f
        } else {
            pxToDP = 1f
        }

        val screenWidth = drawInfo.screenBounds.width * pxToDP
        val screenHeight = drawInfo.screenBounds.height * pxToDP

        val fontSize = (FONT_SIZE * dpToPX).toInt()

        val triangleWidth = (TRIANGLE_SIZE * dpToPX).toInt()
        val triangleHeight = (TRIANGLE_SIZE * dpToPX).toInt()

        val strokeWidth = (STROKE_WIDTH * dpToPX).toInt()
        val screenPadding = (SCREEN_PADDING * dpToPX).toInt()

        // Get fonts
        val font = Typeface.create("HelveticaNeue-Light", Typeface.NORMAL)

        // Calculate the maximum popup size, adjust with dpi
        val maxPopupWidth = Math.min(screenWidth, screenHeight).toInt()

        val halfStrokeWidth = strokeWidth * 0.5f
        val maxTextWidth = maxPopupWidth - screenPadding * 2 - strokeWidth

        // Create paint object
        val paint = Paint()
        paint.isAntiAlias = true

        // Measure text size
        val textPaint = TextPaint()
        textPaint.color = TEXT_COLOR
        textPaint.textSize = fontSize.toFloat()
        textPaint.typeface = font
        val textSize = android.graphics.Point(0, 0)
        var textLayout: StaticLayout? = null

        if (text !== "") {
            textLayout = StaticLayout(text, textPaint, maxTextWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
            textSize.set(Math.min(textPaint.measureText(text), textLayout.width.toFloat()).toInt(), textLayout.height)
        }

        // Calculate bitmap size and create graphics context
        var popupWidth = textSize.x
        popupWidth += 2 * POPUP_PADDING + strokeWidth + triangleWidth
        var popupHeight = textSize.y
        popupHeight += 2 * POPUP_PADDING + strokeWidth

        // Create bitmap and canvas
        val bitmap = android.graphics.Bitmap.createBitmap(
                popupWidth, popupHeight, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Prepare triangle path
        val trianglePath = Path()
        trianglePath.moveTo(triangleWidth.toFloat(), 0f)
        trianglePath.lineTo(halfStrokeWidth, triangleHeight * 0.5f)
        trianglePath.lineTo(triangleWidth.toFloat(), triangleHeight.toFloat())
        trianglePath.close()

        // Calculate anchor point and triangle position
        val triangleOffsetX = 0
        val triangleOffsetY = (popupHeight - triangleHeight) / 2

        // Stroke background
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = STROKE_COLOR
        val backgroundRect = RectF(triangleWidth.toFloat(), halfStrokeWidth, (popupWidth - strokeWidth).toFloat(), (popupHeight - strokeWidth).toFloat())
        canvas.drawRect(backgroundRect, paint)

        // Stroke triangle
        canvas.save()
        canvas.translate(triangleOffsetX.toFloat(), triangleOffsetY.toFloat())
        canvas.drawPath(trianglePath, paint)
        canvas.restore()

        // Fill background
        paint.style = Paint.Style.FILL
        paint.color = BACKGROUND_COLOR
        canvas.drawRect(backgroundRect, paint)

        // Fill triangle
        canvas.save()
        canvas.translate(triangleOffsetX.toFloat(), triangleOffsetY.toFloat())
        canvas.drawPath(trianglePath, paint)
        canvas.restore()

        if (textLayout != null) {
            // Draw text
            canvas.save()
            canvas.translate(halfStrokeWidth + triangleWidth.toFloat() + POPUP_PADDING.toFloat(), halfStrokeWidth + POPUP_PADDING)
            textLayout.draw(canvas)
            canvas.restore()
        }

        return BitmapUtils.createBitmapFromAndroidBitmap(bitmap)
    }

    override fun onPopupClicked(clickInfo: PopupClickInfo): Boolean {
        return true
    }

    companion object {

        private val SCREEN_PADDING = 10
        private val POPUP_PADDING = 10
        private val FONT_SIZE = 15
        private val STROKE_WIDTH = 2
        private val TRIANGLE_SIZE = 10
        private val TEXT_COLOR = android.graphics.Color.BLACK
        private val STROKE_COLOR = android.graphics.Color.BLACK
        private val BACKGROUND_COLOR = android.graphics.Color.WHITE
    }
}