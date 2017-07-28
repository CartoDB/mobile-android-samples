package com.carto.advanced.kotlin.sections.geocoding

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.shapes.Shape
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ListView
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.BaseGeocodingView
import com.carto.advanced.kotlin.sections.base.setFrame
import com.carto.advanced.kotlin.utils.Colors
import com.carto.geocoding.GeocodingResult
import android.widget.TextView
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.sections.base.hide
import com.carto.advanced.kotlin.sections.base.show


/**
 * Created by aareundo on 11/07/2017.
 */
class GeocodingView(context: Context) : BaseGeocodingView(context) {

    val inputField = EditText(context)
    val resultTable = ListView(context)
    val adapter = GeocodingResultAdapter(context)
    val padding = (5 * resources.displayMetrics.density).toInt()

    init {

        title = Texts.geocodingInfoHeader
        description = Texts.geocodingInfoContainer

        inputField.setTextColor(Color.WHITE)
        inputField.setBackgroundColor(Colors.darkTransparentGray)
        inputField.setPadding(padding, inputField.paddingTop, inputField.paddingRight, inputField.paddingBottom)
        inputField.imeOptions = EditorInfo.IME_ACTION_DONE
        inputField.hint = "Type address..."
        inputField.setHintTextColor(Color.LTGRAY)
        inputField.setSingleLine()

        addView(inputField)

        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            f.isAccessible = true
            f.set(inputField, R.drawable.cursor)
        } catch (ignored: Exception) { }

        resultTable.adapter = adapter
        resultTable.setBackgroundColor(Colors.lightTransparentGray)
        addView(resultTable)

        hideTable()

        layoutSubviews()

        disableAutoFocusOfTextField()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()

        val x: Int = padding
        var y: Int = padding
        val w: Int = frame.width - 2 * padding
        var h: Int = (45 * resources.displayMetrics.density).toInt()

        inputField.setFrame(x, y, w, h)

        y += h + (1 * resources.displayMetrics.density).toInt()
        h = (200 * resources.displayMetrics.density).toInt()

        resultTable.setFrame(x, y, w, h)
        adapter.width = frame.width
    }

    fun update(list: MutableList<GeocodingResult>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    fun showTable() {
        resultTable.visibility = View.VISIBLE
    }

    fun hideTable() {
        resultTable.visibility = View.GONE
    }

    fun clearInput() {
        inputField.text = SpannableStringBuilder("")
    }

    fun showBannerInsteadOfSearchBar() {
        inputField.alpha = 0.0f
        showBanner("DOWNLOAD A PACKAGE TO START GEOCODING")
    }

    fun showSearchBar() {
        inputField.show({})
        topBanner.hide({})
    }
}