package com.carto.cartomap.android.builder;

import com.carto.cartomap.android.maplistener.MyUTFGridEventListener;
import com.carto.core.MapPos;
import com.carto.core.Variant;
import com.carto.core.VariantType;
import com.carto.layers.Layer;
import com.carto.layers.TileLayer;
import com.carto.layers.VectorLayer;
import com.carto.services.CartoVisBuilder;
import com.carto.ui.MapView;

/**
 * Created by aareundo on 31/08/16.
 */

public class MyCartoVisBuilder extends CartoVisBuilder {

    private VectorLayer vectorLayer; // vector layer for popups
    private MapView mapView;

    public MyCartoVisBuilder(MapView mapView, VectorLayer vectorLayer) {
        this.mapView = mapView;
        this.vectorLayer = vectorLayer;
    }

    @Override
    public void setCenter(MapPos mapPos) {
        mapView.setFocusPos(mapView.getOptions().getBaseProjection().fromWgs84(mapPos), 1.0f);
    }

    @Override
    public void setZoom(float zoom) {
        mapView.setZoom(zoom, 1.0f);
    }

    @Override
    public void addLayer(Layer layer, Variant attributes) {

        // Add the layer to the map view
        mapView.getLayers().add(layer);

        // Check if the layer has info window. In that case will add a custom UTF grid event listener to the layer.
        Variant infoWindow = attributes.getObjectElement("infowindow");

        if (infoWindow.getType() == VariantType.VARIANT_TYPE_OBJECT) {
            MyUTFGridEventListener myEventListener = new MyUTFGridEventListener(vectorLayer);
            TileLayer tileLayer = (TileLayer) layer;
            tileLayer.setUTFGridEventListener(myEventListener);
        }
    }
}
