package com.carto.advancedmap.sections.routing;

import android.os.Bundle;

import com.carto.advancedmap.sections.offlinemap.advancedpackagemanager.PackageManagerBaseView;

import com.carto.advancedmap.utils.Sources;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.routing.PackageManagerValhallaRoutingService;

/**
 * Created by aareundo on 16/12/16.
 */

public class OfflineRoutingActivity extends BaseRoutingActivity {

    @Override
    public String getFolderName() {
        return "com.carto.routingpackages";
    }

    @Override
    public String getSource() {
        return Sources.ROUTING_TAG + Sources.OFFLINE_ROUTING;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        contentView = new PackageManagerBaseView(this);
        setContentView(contentView);

        super.onCreate(savedInstanceState);

        contentView.addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);

        PackageManagerValhallaRoutingService service =
                new PackageManagerValhallaRoutingService(contentView.manager);
        setService(service);
    }

}
