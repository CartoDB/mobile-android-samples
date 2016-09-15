package com.carto.cartomap.android;

import android.app.Application;

import com.carto.ui.MapView;

/**
 * Entry point for an android application.
 * One-time actions, such as registering ids, licenses, starting services etc.
 * should be initialized here
 */
public class CartoMapApplication extends Application {

    static final String LICENSE = "XTUN3Q0ZIN1RZdVJNanhFbWxpTlg5TDNxZFZmZGZSanpBaFFEVFVza0UycWxUZlNrL3Fi" +
            "TU4zVmFwOGk2cmc9PQoKcHJvZHVjdHM9c2RrLWFuZHJvaWQtNC4qCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5jYXJ0b21" +
            "hcC5hbmRyb2lkCndhdGVybWFyaz1kZXZlbG9wbWVudAp2YWxpZFVudGlsPTIwMTYtMDktMTUKb25saW5lTGljZW5zZT0xCg==";

    @Override
    public void onCreate() {
        super.onCreate();

        // The initial step: register your license (this must be done before using MapView).
        // You can get your free/commercial license from: http://developer.carto.com
        // The license used here is intended only for CARTO Mobile SDK demos and WILL NOT work with other apps!
        MapView.registerLicense(LICENSE, getApplicationContext());

        com.carto.utils.Log.setShowDebug(true);
        com.carto.utils.Log.setShowInfo(true);
    }
}
