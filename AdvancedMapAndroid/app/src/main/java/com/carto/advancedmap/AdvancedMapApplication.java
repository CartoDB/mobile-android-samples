package com.carto.advancedmap;

import android.app.Application;

import com.carto.ui.MapView;

/**
 * Entry point for an android application.
 * One-time actions, such as registering ids, licenses, starting services etc.
 * should be initialized here
 */
public class AdvancedMapApplication extends Application {

    static final String LICENSE = "XTUN3Q0ZDUmFWcm9pWEUycVN3LzlGeVhEZWZuVnp3RkJBaFE3dHpTSTlDaEFVeW9aWUNQdmcwND" +
            "dwNitEWEE9PQoKcHJvZHVjdHM9c2RrLWFuZHJvaWQtMy4qCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZHZhbmNlZG1hcAp3YXR" +
            "lcm1hcms9bnV0aXRlcQpvbmxpbmVMaWNlbnNlPTEKdXNlcktleT1jZmYyMzI1N2ZiNTBiYWFmMDY4NDI1Y2NkNmMwYzk4Mgo=";

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
