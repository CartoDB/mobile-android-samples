package com.carto.advanced.kotlin.sections.base

/**
 * Created by aareundo on 13/07/2017.
 */
import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by ccheng on 3/18/14.
 */
class JustifiedTextView(context: Context) : TextView(context) {

    /**
     * Copied and translated from:
     * https://github.com/ufo22940268/android-justifiedtextview
     */

    private var mLineY: Int = 0
    private var mViewWidth: Int = 0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        val paint = paint
        paint.color = currentTextColor
        paint.drawableState = drawableState
        mViewWidth = measuredWidth
        val text = text as String
        mLineY = 0
        mLineY += (textSize * 1.5).toInt()
        val layout = layout
        for (i in 0..layout.lineCount - 1) {
            val lineStart = layout.getLineStart(i)
            val lineEnd = layout.getLineEnd(i)
            val line = text.substring(lineStart, lineEnd)

            val width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint())
            if (needScale(line) && i < layout.lineCount - 1) {
                drawScaledText(canvas, lineStart, line, width)
            } else {
                canvas.drawText(line, 0f, mLineY.toFloat(), paint)
            }

            mLineY += lineHeight
        }
    }

    private fun drawScaledText(canvas: Canvas, lineStart: Int, line: String, lineWidth: Float) {
        var line = line
        var x = 0f
        if (isFirstLineOfParagraph(lineStart, line)) {
            val blanks = "  "
            canvas.drawText(blanks, x, mLineY.toFloat(), paint)
            val bw = StaticLayout.getDesiredWidth(blanks, paint)
            x += bw

            line = line.substring(3)
        }

        val d = (mViewWidth - lineWidth) / line.length - 1
        for (i in 0..line.length - 1) {
            val c = line[i].toString()
            val cw = StaticLayout.getDesiredWidth(c, paint)
            canvas.drawText(c, x, mLineY.toFloat(), paint)
            x += cw + d
        }
    }

    private fun isFirstLineOfParagraph(lineStart: Int, line: String): Boolean {
        return line.length > 3 && line[0] == ' ' && line[1] == ' '
    }

    private fun needScale(line: String): Boolean {
        if (line.length == 0) {
            return false
        } else {
            return line[line.length - 1] != '\n'
        }
    }

}