package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.baseclasses.views.PackageManagerBaseView;

import com.carto.advancedmap.utils.Sources;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.routing.PackageManagerValhallaRoutingService;

/**
 * Created by aareundo on 16/12/16.
 */

public class OfflineRoutingActivity extends BaseRoutingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        PackageManagerValhallaRoutingService service =
                new PackageManagerValhallaRoutingService(contentView.manager);
        setService(service);
    }
    
    @Override
    public void onResume() {
        super.onResume();

        if (!contentView.hasLocalPackages()) {
            contentView.banner.alert("Click the globe icon to download a package");
        }
    }
}
