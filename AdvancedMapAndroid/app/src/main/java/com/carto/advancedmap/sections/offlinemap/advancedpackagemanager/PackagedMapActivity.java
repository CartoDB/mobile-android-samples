package com.carto.advancedmap.sections.offlinemap.advancedpackagemanager;

import android.os.Bundle;

import com.carto.advancedmap.baseactivities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.core.BinaryData;
import com.carto.datasources.PackageManagerTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.VectorTileLayer;
import com.carto.styles.CompiledStyleSet;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;
import com.carto.vectortiles.MBVectorTileDecoder;

/**
 * A uses AdvancedPackageManagerActivity datasource. This has maps which are downloaded offline using PackageManager
 */
@ActivityData(name = "Packaged Map", description = "This has maps which are downloaded offline using PackageManager")
public class PackagedMapActivity extends MapBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);
        // Create style set
        PackageManagerTileDataSource source = AdvancedPackageManagerActivity.dataSource;

        BinaryData styleBytes = AssetUtils.loadAsset("nutiteq-dark.zip");
        CompiledStyleSet style = new CompiledStyleSet(new ZippedAssetPackage(styleBytes));

        // Create Decoder
        MBVectorTileDecoder decoder = new MBVectorTileDecoder(style);

        VectorTileLayer layer = new VectorTileLayer(source, decoder);

        mapView.getLayers().add(layer);


        ActivityData data = ((ActivityData) this.getClass().getAnnotations()[0]);
        String name = data.name();
        String description = data.description();

        setTitle(name);
        getActionBar().setSubtitle(description);
    }

}
