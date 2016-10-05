package com.carto.cartomap.android.map;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.carto.cartomap.android.Description;
import com.carto.cartomap.android.builder.MyCartoVisBuilder;
import com.carto.cartomap.android.mapbase.MapSampleBaseActivity;

import com.carto.core.BinaryData;
import com.carto.datasources.LocalVectorDataSource;

import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;

import com.carto.services.CartoVisLoader;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;

import java.io.IOException;

/**
 * A sample demonstrating how to use high level Carto VisJSON API to display interactive maps.
 * A list of different visjson URLs can be selected from the menu.
 * CartoVisLoader class is used to load and configure all corresponding layers.
 * Items on overlay layers are clickable, this is implemented using custom UTFGridEventListener.
 */
@Description(value = "Use CARTO VisJSON API to display interactive maps")
public class CartoVisJSONActivity extends MapSampleBaseActivity {

    static final String circleUrl = "http://documentation.cartodb.com/api/v2/viz/836e37ca-085a-11e4-8834-0edbca4b5057/viz.json";
    static final String testUrl = "http://documentation.cartodb.com/api/v2/viz/3ec995a8-b6ae-11e4-849e-0e4fddd5de28/viz.json";
    static final String countriesUrl = "http://documentation.cartodb.com/api/v2/viz/2b13c956-e7c1-11e2-806b-5404a6a683d5/viz.json";
    static final String dotsUrl = "https://documentation.cartodb.com/api/v2/viz/236085de-ea08-11e2-958c-5404a6a683d5/viz.json";
    static final String fontsUrl = "https://cartomobile-team.carto.com/u/nutiteq/api/v2/viz/13332848-27da-11e6-8801-0e5db1731f59/viz.json";

    private String visJSONURL = dotsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the initial visJSON
        updateVis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addVisJSONMenuOption(menu, "circle", circleUrl);
        addVisJSONMenuOption(menu, "test", testUrl);
        addVisJSONMenuOption(menu, "countries", countriesUrl);
        addVisJSONMenuOption(menu, "dots", dotsUrl);

        return true;
    }

    private void addVisJSONMenuOption(final Menu menu, String text, final String value) {
        MenuItem menuItem = menu.add(text).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick (MenuItem item){

                for (int i = 0; i < menu.size(); i++) {
                    MenuItem otherItem = menu.getItem(i);
                    if (otherItem == item) {
                        otherItem.setIcon(android.R.drawable.checkbox_on_background);
                    } else {
                        otherItem.setIcon(null);
                    }
                }

                visJSONURL = value;
                updateVis();

                return true;
            }
        });

        if (visJSONURL.equals(value)) {
            menuItem.setIcon(android.R.drawable.checkbox_on_background);
        }
    }

    protected void updateVis() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mapView.getLayers().clear();

                // Create overlay layer for popups
                Projection proj = mapView.getOptions().getBaseProjection();
                LocalVectorDataSource dataSource = new LocalVectorDataSource(proj);
                VectorLayer vectorLayer = new VectorLayer(dataSource);

                // Create VIS loader
                CartoVisLoader loader = new CartoVisLoader();
                loader.setDefaultVectorLayerMode(true);

                BinaryData fontData = AssetUtils.loadAsset("carto-fonts.zip");
                loader.setVectorTileAssetPackage(new ZippedAssetPackage(fontData));

                MyCartoVisBuilder visBuilder = new MyCartoVisBuilder(mapView, vectorLayer);

                try {
                    loader.loadVis(visBuilder, visJSONURL);
                }
                catch (IOException e) {
                    Log.e("EXCEPTION", "Exception: " + e);
                }

                // Add the created popup overlay layer on top of all visJSON layers
                mapView.getLayers().add(vectorLayer);
            }
        });

        thread.start(); // TODO: should serialize execution
    }
}
