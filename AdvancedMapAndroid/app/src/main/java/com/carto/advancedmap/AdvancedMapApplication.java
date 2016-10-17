package com.carto.advancedmap;

import android.app.Application;

import com.carto.ui.MapView;

/**
 * Entry point for an android application.
 * One-time actions, such as registering ids, licenses, starting services etc.
 * should be initialized here
 */
public class AdvancedMapApplication extends Application {

    static final String LICENSE = "XTUN3Q0ZHZDZLQm9KU3krYmVVTTVEdDhCbWtWUmt1L1NBaFI4RW9oMW03aWpvSXhoZURmSXZqUG1XM1pwQ2c9PQoKYXBwVG9rZW49YjhhODBlM2QtODZhZS00ZDcyLWI5NDktZTZiMTYyYTJlMjk1CnZhbGlkVW50aWw9MjAxNi0xMC0xNQp3YXRlcm1hcms9Y3VzdG9tCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZHZhbmNlZG1hcApvbmxpbmVMaWNlbnNlPTEKcHJvZHVjdHM9c2RrLWFuZHJvaWQtNC4qCg==";

    @Override
    public void onCreate() {
        super.onCreate();

        // The initial step: register your license (this must be done before using MapView).
        // You can get your free/commercial license from: http://developer.carto.com
        // The license used here is intended only for CARTO Mobile SDK demos and WILL NOT work with other apps!
        MapView.registerLicense(LICENSE, getApplicationContext());

        com.carto.utils.Log.setShowInfo(true);

    }
}
