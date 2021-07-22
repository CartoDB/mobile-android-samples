package com.carto.advanced.kotlin.sections.geocoding

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.carto.advanced.kotlin.sections.base.activities.PackageDownloadBaseActivity
import com.carto.advanced.kotlin.sections.base.views.BaseGeocodingView
import com.carto.advanced.kotlin.utils.Utils
import com.carto.geocoding.*
import com.carto.packagemanager.CartoPackageManager
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener

/**
 * Created by aareundo on 11/07/2017.
 */
class GeocodingActivity : PackageDownloadBaseActivity() {

    var service: GeocodingService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = GeocodingView(this)
        setContentView(contentView)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val folder = Utils.createDirectory(this, "geocodingpackages")
        contentView?.manager = CartoPackageManager(BaseGeocodingView.SOURCE, folder)

        setOnlineMode()
    }

    val changeListener = object: TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun afterTextChanged(s: Editable?) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            var text = s!!

            if ((text.toString() == "")) {
                return
            }

            (contentView as? GeocodingView)!!.showTable()
            text = (contentView as? GeocodingView)!!.inputField.text.toString()
            val autoComplete = true
            geocode(text, autoComplete)
        }
    }

    override fun packageDownloadComplete() {
        (contentView as? GeocodingView)!!.showSearchBar()
    }

    override fun onResume() {
        super.onResume()

        contentView?.map?.mapEventListener = object : MapEventListener() {
            override fun onMapClicked(mapClickInfo: MapClickInfo?) {

                runOnUiThread {
                    contentView?.closeKeyboard()
                    (contentView as? GeocodingView)!!.hideTable()
                }
            }
        }

        (contentView as? GeocodingView)!!.inputField.addTextChangedListener(changeListener)
        (contentView as? GeocodingView)!!.inputField.setOnEditorActionListener() { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                onEditingEnded(true)
            }
            false
        }

        (contentView as? GeocodingView)!!.resultTable.setOnItemClickListener { _, _, position, _ ->
            run {
                showResult(results!!.get(position)!!)
                onEditingEnded(false)
            }
        }

    }

    override fun onPause() {
        super.onPause()

        contentView?.map?.mapEventListener = null

        (contentView as? GeocodingView)!!.inputField.removeTextChangedListener(changeListener)

        (contentView as? GeocodingView)!!.resultTable.onItemClickListener = null

        (contentView as? GeocodingView)!!.inputField.onFocusChangeListener = null
    }

    fun onEditingEnded(geocode: Boolean) {
        contentView?.closeKeyboard()
        (contentView as? GeocodingView)!!.hideTable()
        if (geocode) {
            val text = (contentView as? GeocodingView)!!.inputField.text.toString()
            val autoComplete = false
            geocode(text, autoComplete)
        }
        (contentView as? GeocodingView)!!.clearInput()
    }

    var searchQueueSize: Int = 0
    var addresses = mutableListOf<GeocodingResult>()
    var results: GeocodingResultVector? = null

    fun geocode(text: String, autocomplete: Boolean) {

        searchQueueSize += 1

        val thread = Thread(Runnable {
            if (searchQueueSize - 1 > 0) {
                // Cancel the request if we have additional pending requests queued
                print("Geocoding: request pending, skipping current")
                return@Runnable
            }

            searchQueueSize -= 1

            val request = GeocodingRequest(contentView?.projection, text)

            if (service is PackageManagerGeocodingService) {
                (service as PackageManagerGeocodingService).isAutocomplete = autocomplete
            } else {
                (service as MapBoxOnlineGeocodingService).isAutocomplete = autocomplete
            }

            results = service!!.calculateAddresses(request)
            val count = results!!.size()

            runOnUiThread {
                // In autocomplete mode just fill the autocomplete address list and reload tableview
                // In full geocode mode, show the result
                if (autocomplete) {
                    addresses.clear()

                    for (i in 0..count - 1) {
                        val result = results?.get(i.toInt())
                        addresses.add(result!!)
                    }

                    (contentView as? GeocodingView)!!.update(addresses)

                } else if (count > 0) {
                    showResult(results?.get(0)!!)
                }

            }
        })
        thread.start()
    }

    fun showResult(result: GeocodingResult) {
        val title = ""
        val description = result.getPrettyAddress()
        val goToPosition = true

        (contentView as? GeocodingView)!!.showResult(result, title, description, goToPosition)
    }

    override fun setOnlineMode() {
        service = MapBoxOnlineGeocodingService(BaseGeocodingView.MAPBOX_TOKEN)
        updateUIBasedOnModeAndAvailablePackages(true)
    }

    override fun setOfflineMode() {
        service = PackageManagerGeocodingService(contentView?.manager)
        updateUIBasedOnModeAndAvailablePackages(false)
    }

    private fun updateUIBasedOnModeAndAvailablePackages(isOnline: Boolean) {

        if (isOnline) {
            (contentView as? GeocodingView)!!.showSearchBar()
        } else {
            if (contentView?.hasLocalPackages()!!) {
                contentView?.showLocalPackages()
                (contentView as? GeocodingView)!!.showSearchBar()
            } else {
                (contentView as? GeocodingView)!!.showBannerInsteadOfSearchBar()
            }
        }
    }
}
