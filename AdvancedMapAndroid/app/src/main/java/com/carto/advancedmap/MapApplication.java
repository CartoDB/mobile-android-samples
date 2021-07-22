package com.carto.advancedmap;

import android.app.Application;

import com.carto.ui.MapView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

/**
 * Entry point for an android application.
 * One-time actions, such as registering ids, licenses, starting services etc.
 * should be initialized here
 */
public class MapApplication extends Application {

    public static final String LOG_TAG = "carto-android-sample";

    static final String LICENSE = "XTUM0Q0ZRQ3lnck9iNmN3ZUV3SStZS2lvcVdTMUtQVk9WZ0lWQUpwUG1vbEoxNFU3SVBERnJ4SkFmbnZwZ0FsOQoKYXBwVG9rZW49YjhhODBlM2QtODZhZS00ZDcyLWI5NDktZTZiMTYyYTJlMjk1CnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZHZhbmNlZG1hcApvbmxpbmVMaWNlbnNlPTEKcHJvZHVjdHM9c2RrLWFuZHJvaWQtNC4qCndhdGVybWFyaz1jdXN0b20K";

    private void fixSSLConnectionOnOlderAndroidDevices() {
        // Older Android versions (4.x) have issues accepting TLSv1.2 secure connections that SDK requires.
        // This snippet installs a workaround for such devices.
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        fixSSLConnectionOnOlderAndroidDevices();

        // The initial step: register your license (this must be done before using mapView).
        // You can get your free/commercial license from: http://developer.carto.com
        // The license used here is intended only for CARTO Mobile SDK demos and WILL NOT work with other apps!
        MapView.registerLicense(LICENSE, getApplicationContext());

        com.carto.utils.Log.setShowInfo(true);
        com.carto.utils.Log.setShowDebug(true);
        com.carto.utils.Log.setShowError(true);
    }
}
