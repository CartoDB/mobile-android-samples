package com.carto.advanced.kotlin.sections.geocoding

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.carto.advanced.kotlin.components.popupcontent.packagepopupcontent.PackageCell
import com.carto.advanced.kotlin.sections.base.activities.BaseActivity
import com.carto.advanced.kotlin.sections.base.views.BaseGeocodingView
import com.carto.advanced.kotlin.utils.Utils
import com.carto.geocoding.GeocodingRequest
import com.carto.geocoding.GeocodingResult
import com.carto.geocoding.GeocodingResultVector
import com.carto.geocoding.PackageManagerGeocodingService
import com.carto.packagemanager.CartoPackageManager
import com.carto.packagemanager.PackageManagerListener
import com.carto.packagemanager.PackageStatus
import com.carto.ui.MapClickInfo
import com.carto.ui.MapEventListener
import org.jetbrains.anko.doAsync

/**
 * Created by aareundo on 11/07/2017.
 */
class GeocodingActivity : BaseActivity() {

    var contentView: GeocodingView? = null

    var service: PackageManagerGeocodingService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentView = GeocodingView(this)
        setContentView(contentView)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val folder = Utils.createDirectory(this, "geocodingpackages")
        contentView?.manager = CartoPackageManager(BaseGeocodingView.SOURCE, folder)

        service = PackageManagerGeocodingService(contentView?.manager)
    }

    val changeListener = object: TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun afterTextChanged(s: Editable?) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            var text = s!!

            if ((text.toString() == "")) {
                return
            }

            contentView?.showTable()
            text = contentView?.inputField?.text.toString()
            val autoComplete = true
            geocode(text, autoComplete)
        }
    }

    override fun onResume() {
        super.onResume()
        contentView?.addListeners()

        contentView?.map?.mapEventListener = object : MapEventListener() {
            override fun onMapClicked(mapClickInfo: MapClickInfo?) {
                contentView?.closeKeyboard()
                contentView?.hideTable()
            }
        }

        contentView?.manager?.packageManagerListener = object: PackageManagerListener() {
            override fun onPackageListUpdated() {
                val packages = contentView?.getPackages()!!
                runOnUiThread {
                    contentView?.updatePackages(packages)
                }
            }

            override fun onPackageStatusChanged(id: String?, version: Int, status: PackageStatus?) {
                runOnUiThread {
                    contentView?.onStatusChanged(id!!, status!!)
                }
            }

            override fun onPackageUpdated(id: String?, version: Int) {
                runOnUiThread {
                    contentView?.downloadComplete(id!!)
                    contentView?.showSearchBar()
                }
            }
        }

        contentView?.packageContent?.list?.setOnItemClickListener { _, view, _, _ ->
            run {
                val cell = view as PackageCell
                contentView?.onPackageClick(cell.item!!)
            }
        }

        contentView?.manager?.start()
        contentView?.manager?.startPackageListDownload()

        contentView?.inputField?.addTextChangedListener(changeListener)
        contentView?.inputField?.setOnEditorActionListener() { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                onEditingEnded(true)
            }
            false
        }

        contentView?.resultTable?.setOnItemClickListener { _, _, position, _ ->
            run {
                showResult(results!!.get(position)!!)
                onEditingEnded(false)
            }
        }

        if (contentView?.hasLocalPackages()!!) {
            contentView?.showLocalPackages()
            contentView?.showSearchBar()
        } else {
            contentView?.showBannerInsteadOfSearchBar()
        }
    }

    override fun onPause() {
        super.onPause()
        contentView?.removeListeners()

        contentView?.manager?.packageManagerListener = null

        contentView?.packageContent?.list?.onItemClickListener = null

        contentView?.manager?.stop(false)

        contentView?.map?.mapEventListener = null

        contentView?.inputField?.removeTextChangedListener(changeListener)

        contentView?.resultTable?.onItemClickListener = null

        contentView?.inputField?.onFocusChangeListener = null
    }

    fun onEditingEnded(geocode: Boolean) {
        contentView?.closeKeyboard()
        contentView?.hideTable()
        if (geocode) {
            val text = contentView?.inputField?.text.toString()
            val autoComplete = false
            geocode(text, autoComplete)
        }
        contentView?.clearInput()
    }

    var searchQueueSize: Int = 0
    var addresses = mutableListOf<GeocodingResult>()
    var results: GeocodingResultVector? = null

    fun geocode(text: String, autocomplete: Boolean) {

        searchQueueSize += 1

        doAsync {
            if (searchQueueSize - 1 > 0) {
                // Cancel the request if we have additional pending requests queued
                print("Geocoding: request pending, skipping current")
                return@doAsync
            }

            searchQueueSize -= 1

            val request = GeocodingRequest(contentView?.projection, text)

            service!!.isAutocomplete = autocomplete
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

                    contentView?.update(addresses)

                } else if (count > 0) {
                    showResult(results?.get(0)!!)
                }

            }
        }
    }

    fun showResult(result: GeocodingResult) {
        val title = ""
        val description = result.getPrettyAddress()
        val goToPosition = true

        contentView?.showResult(result, title, description, goToPosition)
    }
}
