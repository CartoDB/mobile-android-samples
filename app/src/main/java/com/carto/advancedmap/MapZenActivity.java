package com.carto.advancedmap;

import android.os.Bundle;

import com.carto.core.MapRange;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.VectorTileLayer;
import com.carto.utils.AssetUtils;
import com.carto.vectortiles.MBVectorTileDecoder;
import com.carto.vectortiles.CompiledStyleSet;
import com.carto.vectortiles.ZippedAssetPackage;
import com.carto.core.BinaryData;

/**
 * Use Mapzen vector tiles from https://mapzen.com/projects/vector-tiles/
 *
 * Special style is needed, as the vector data structure is different. We use style inside same .zip package
 *
 */
public class MapZenActivity extends MapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);

        String mapZenKey = "vector-tiles-F2ETj3g";

        // Configure to Mapzen tiles, we load all layers to have rich map
        TileDataSource baseRasterTileDataSource = new HTTPTileDataSource(1, 16, "http://vector.mapzen.com/osm/all/{z}/{x}/{y}.mvt?api_key="+mapZenKey);
        String styleAssetName = "nutibright-v3.zip";
        BinaryData styleBytes = AssetUtils.loadAsset(styleAssetName);
        // Create style set
        CompiledStyleSet vectorTileStyleSet = new CompiledStyleSet(new ZippedAssetPackage(styleBytes), "mapzen");

        // we must use special style file for mapzen, as this has different data structure
        MBVectorTileDecoder vectorTileDecoder = new MBVectorTileDecoder(vectorTileStyleSet);

        // use "name:en" to get English names where available, "name" is for generic local names
        vectorTileDecoder.setStyleParameter("name", "name:en");

        // try to set client-side buffer, as mapzen does not have it in tiles
        vectorTileDecoder.setBuffering(1.0f/64.0f);

        // Create vector layer
        baseLayer = new VectorTileLayer(baseRasterTileDataSource, vectorTileDecoder);
        mapView.getLayers().add(baseLayer);

        mapView.getOptions().setZoomRange(new MapRange(0,22));
    }
}
