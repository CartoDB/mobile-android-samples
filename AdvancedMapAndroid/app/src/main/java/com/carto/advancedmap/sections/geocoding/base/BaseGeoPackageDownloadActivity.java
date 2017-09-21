package com.carto.advancedmap.sections.geocoding.base;

import com.carto.advancedmap.sections.offlinemap.advancedpackagemanager.PackageManagerBaseActivity;
import com.carto.advancedmap.utils.Sources;

/**
 * Created by aareundo on 21/08/2017.
 */

public class BaseGeoPackageDownloadActivity extends PackageManagerBaseActivity {

    @Override
    public String getFolderName() {
        return "geocodingpackages";
    }

    @Override
    public String getSource() {
        return Sources.GEOCODING_TAG + Sources.OFFLINE_GEOCODING;
    }
}
