package com.carto.advancedmap.sections.vectorobjects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;

import com.carto.advancedmap.R;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.main.ActivityData;
import com.carto.core.MapPos;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.FeatureCollection;
import com.carto.geometry.GeoJSONGeometryReader;
import com.carto.geometry.PointGeometry;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.ClusterElementBuilder;
import com.carto.layers.ClusteredVectorLayer;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.Marker;
import com.carto.vectorelements.VectorElement;
import com.carto.vectorelements.VectorElementVector;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aareundo on 07/11/16.
 */


public class ClusteredMarkersActivity extends MapBaseActivity {

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        // Add default base layer
        addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_POSITRON);

        // Initialize a local vector data source
        final LocalVectorDataSource source = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        ClusteredVectorLayer layer = new ClusteredVectorLayer(source, new MyClusterElementBuilder(this));
        layer.setMinimumClusterDistance(50);

        // Add the clustered vector layer to the map
        mapView.getLayers().add(layer);

        // As the file to load is rather large, we don't want to block our main thread
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                // Create a basic style, as the ClusterElementBuilder will set the real style
                MarkerStyle style = new MarkerStyleBuilder().buildStyle();

                // Read GeoJSON, parse it using SDK GeoJSON parser
                GeoJSONGeometryReader reader = new GeoJSONGeometryReader();

                // Set target projection to base (mercator)
                reader.setTargetProjection(baseProjection);
                alert("Starting load from .geojson");

                // Read features from local asset
                FeatureCollection features = reader.readFeatureCollection(loadJSONFromAsset());
                alert("Finished load from .geojson");

                VectorElementVector elements = new VectorElementVector();

                for (int i = 0; i < features.getFeatureCount(); i++) {
                    // This data set features point geometry,
                    // however, it can also be LineGeometry or PolygonGeometry
                    boolean destroyed = ClusteredMarkersActivity.this.isDestroyed();
                    if (destroyed) {
                        System.out.println("Activity was destroyed. Finish thread");
                        return;
                    }
                    PointGeometry geometry = (PointGeometry) features.getFeature(i).getGeometry();
                    elements.add(new Marker(geometry, style));
                }

                source.addAll(elements);
                alert("Finished adding Markers to source. Clustering started");
            }
        });

        thread.start();
    }

    private String loadJSONFromAsset() {

        String fileName = "cities15000.geojson";

        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            alert("Failed to load file: " + fileName);
            return null;
        }
    }

    /**
     *  CLUSTER BUILDER
     **/
    private class MyClusterElementBuilder extends ClusterElementBuilder {

        @SuppressLint("UseSparseArrays")
        private Map<Integer, MarkerStyle> markerStyles = new HashMap<>();
        private Bitmap markerBitmap;

        MyClusterElementBuilder(Context context) {
            markerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_black);
        }

        @Override
        public VectorElement buildClusterElement(MapPos pos, VectorElementVector elements) {

            // Try to reuse existing marker styles
            MarkerStyle style = markerStyles.get((int) elements.size());

            if (elements.size() == 1) {
                style = ((Marker) elements.get(0)).getStyle();
            }

            if (style == null) {

                Bitmap canvasBitmap = markerBitmap.copy(Bitmap.Config.ARGB_8888, true);
                android.graphics.Canvas canvas = new android.graphics.Canvas(canvasBitmap);

                android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(12);
                paint.setColor(android.graphics.Color.argb(255, 0, 0, 0));

                float x = markerBitmap.getWidth() / 2;
                float y = markerBitmap.getHeight() / 2 - 5;

                canvas.drawText(Integer.toString((int) elements.size()), x, y, paint);

                MarkerStyleBuilder styleBuilder = new MarkerStyleBuilder();
                styleBuilder.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(canvasBitmap));
                styleBuilder.setSize(30);
                styleBuilder.setPlacementPriority((int)-elements.size());

                style = styleBuilder.buildStyle();

                markerStyles.put((int) elements.size(), style);
            }

            // Create marker for the cluster
            Marker marker = new Marker(pos, style);
            return marker;
        }
    }

}
