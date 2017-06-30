package com.carto.advanced.kotlin;

import android.app.Application;
import android.os.Bundle

import com.carto.ui.MapView;
import com.carto.utils.Log;

/**
 * Entry point for an android application.
 * One-time actions, such as registering ids, licenses, starting services etc.
 * should be initialized here
 */
class MapApplication : Application() {

    val LICENSE = "XTUM0Q0ZRQ3lnck9iNmN3ZUV3SStZS2lvcVdTMUtQVk9WZ0lWQUpwUG1vbEoxNFU3SVBERnJ4SkFmbnZwZ0FsOQoKYXBwVG9rZW49YjhhODBlM2QtODZhZS00ZDcyLWI5NDktZTZiMTYyYTJlMjk1CnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZHZhbmNlZG1hcApvbmxpbmVMaWNlbnNlPTEKcHJvZHVjdHM9c2RrLWFuZHJvaWQtNC4qCndhdGVybWFyaz1jdXN0b20K";

    override fun onCreate() {
        super.onCreate()

        MapView.registerLicense(LICENSE, this)
    }
}
