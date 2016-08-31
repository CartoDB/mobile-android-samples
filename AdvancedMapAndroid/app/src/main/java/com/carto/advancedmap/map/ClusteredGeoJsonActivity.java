package com.carto.advancedmap.map;

import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.map_base.VectorMapSampleBaseActivity;
import com.carto.core.MapPos;
import com.carto.core.MapRange;
import com.carto.core.StringVector;
import com.carto.core.Variant;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.Feature;
import com.carto.geometry.FeatureCollection;
import com.carto.geometry.GeoJSONGeometryReader;
import com.carto.layers.ClusterElementBuilder;
import com.carto.layers.ClusteredVectorLayer;
import com.carto.layers.VectorLayer;
import com.carto.styles.BalloonPopupStyle;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.VectorElement;
import com.carto.vectorelements.VectorElementVector;

import java.io.IOException;
import java.io.InputStream;

/**
 * A sample demonstrating how to read data from GeoJSON and add clustered Markers to map.
 * Both points from GeoJSON, and cluster markers are shown as Ballons which have dynamic texts
 *
 * NB! Suggestions if you have a lot of points (tens or hundreds of thousands) and clusters:
 * 1. Use Point geometry instead of Balloon or Marker
 * 2. Instead of Balloon with text generate dynamically Point bitmap with cluster numbers
 * 3. Make sure you reuse cluster style bitmaps. Creating new bitmap in rendering has technical cost
 */
@Description(value = "Read data from GeoJSON and show as clusters")
public class ClusteredGeoJsonActivity extends VectorMapSampleBaseActivity {

    static class MyClusterElementBuilder extends ClusterElementBuilder {
        BalloonPopupStyle balloonPopupStyle;

        public MyClusterElementBuilder(){
        	balloonPopupStyle = new BalloonPopupStyleBuilder().buildStyle();
        }
        
        @Override
        public VectorElement buildClusterElement(MapPos pos, VectorElementVector elements) {

            // Cluster popup has just a number of cluster elements, and default style
            // You can create here also Marker, Point etc. Point is suggested for big number of objects
            // Note: pos has center of the cluster coordinates

            BalloonPopup popup = new BalloonPopup(
                    pos,
                    balloonPopupStyle,
                    Long.toString(elements.size()), "");
            return popup;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);


        // 1. Initialize a local vector data source
        LocalVectorDataSource vectorDataSource1 = new LocalVectorDataSource(baseProjection);
        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new ClusteredVectorLayer(vectorDataSource1, new MyClusterElementBuilder());
        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);
        // Set visible zoom range for the vector layer
        vectorLayer1.setVisibleZoomRange(new MapRange(0, 18));

        // Style for popups
        BalloonPopupStyle balloonPopupStyle = new BalloonPopupStyleBuilder().buildStyle();

        // read GeoJSON, parse it using SDK GeoJSON parser
        GeoJSONGeometryReader geoJsonParser = new GeoJSONGeometryReader();
        FeatureCollection features = geoJsonParser.readFeatureCollection(loadJSONFromAsset());
        for (int i = 0; i < features.getFeatureCount(); i++) {
            Feature feature = features.getFeature(i);

            // create popup for each object
            BalloonPopup popup = new BalloonPopup(
                    feature.getGeometry(),
                    balloonPopupStyle,
                    feature.getProperties().getObjectElement("Capital").getString(),
                    feature.getProperties().getObjectElement("Country").getString());

            // add all properties as MetaData, so you can use it with click handling
            StringVector keys = feature.getProperties().getObjectKeys();
            for (int j = 0; j < keys.size(); j++) {
                String key = keys.get(j);
                Variant val = feature.getProperties().getObjectElement(key);
                popup.setMetaDataElement(key, val);
            }

            vectorDataSource1.add(popup);
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("capitals_3857.geojson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
