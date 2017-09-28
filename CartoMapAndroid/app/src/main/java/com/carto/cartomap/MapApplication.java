package com.carto.cartomap;

import android.app.Application;
import com.carto.ui.MapView;

/**
 * Entry point for an android application.
 * One-time actions, such as registering ids, licenses, starting services etc.
 * should be initialized here
 */
public class MapApplication extends Application {

    static final String LICENSE = "XTUMwQ0ZFdUVRY1J1OVZPQzlEYnVldkdpbVlGSWhKcUpBaFVBbkNuTlUyaFZV" +
            "dG5ReVRtSHB4dGw3OEd1VDZNPQoKYXBwVG9rZW49OTFlMjQ3NjEtZDRmNi00NjQzLTkzMjctNTNkYjE1Njc" +
            "wN2NmCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5jYXJ0b21hcApvbmxpbmVMaWNlbnNlPTEKcHJvZHVjdHM9c2" +
            "RrLWFuZHJvaWQtNC4qCndhdGVybWFyaz1jdXN0b20K";

    @Override
    public void onCreate() {
        super.onCreate();

        // The initial step: register your license (this must be done before using MapView).
        // You can get your free/commercial license from: http://developer.carto.com
        // The license used here is intended only for CARTO Mobile SDK demos
        // and WILL NOT work with other apps!
        MapView.registerLicense(LICENSE, getApplicationContext());

        com.carto.utils.Log.setShowInfo(true);
    }
}
