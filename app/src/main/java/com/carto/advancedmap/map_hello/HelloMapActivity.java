package com.carto.advancedmap.map_hello;

import android.app.Activity;
import android.os.Bundle;

import com.carto.advancedmap.R;
import com.carto.core.BinaryData;
import com.carto.core.MapPos;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.projections.EPSG3857;
import com.carto.projections.Projection;
import com.carto.ui.MapView;
import com.carto.utils.AssetPackage;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;

/**
 * Created by aareundo on 15/08/16.
 */

public class HelloMapActivity extends Activity {

    static final String LICENSE = "XTUN3Q0ZDUmFWcm9pWEUycVN3LzlGeVhEZWZuVnp3RkJBaFE3dHpTSTlDaEFVeW9aWUNQdmc" +
            "wNDdwNitEWEE9PQoKcHJvZHVjdHM9c2RrLWFuZHJvaWQtMy4qCnBhY2thZ2VOYW1lPWNvbS5jYXJ0by5hZ" +
            "HZhbmNlZG1hcAp3YXRlcm1hcms9bnV0aXRlcQpvbmxpbmVMaWNlbnNlPTEKdXNlcktleT1jZmYyMzI1N2ZiN" +
            "TBiYWFmMDY4NDI1Y2NkNmMwYzk4Mgo=";

    protected MapView mapView;
    protected Projection baseProjection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register license
        MapView.registerLicense(LICENSE, getApplicationContext());

        // Set our view from our hello_map layout resource
        setContentView(R.layout.hello_map);
        mapView = (MapView) this.findViewById(R.id.hello_map_view);

        baseProjection = new EPSG3857();

        // 2. Add base map
        BinaryData data = AssetUtils.loadAsset("nutibright-v3.zip");
        AssetPackage styleAssets = new ZippedAssetPackage(data);
        CartoOnlineVectorTileLayer baseLayer = new CartoOnlineVectorTileLayer("nutiteq.osm", styleAssets);
        mapView.getLayers().add(baseLayer);

        // 3. Set default location and zoom
        MapPos berlin = baseProjection.fromWgs84(new MapPos(13.38933, 52.51704));
        mapView.setFocusPos(berlin, 0);
        mapView.setZoom(10, 0);
    }
}
