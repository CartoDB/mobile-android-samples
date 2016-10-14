package com.carto.advancedmap.sections.overlaydatasources;

import android.os.Bundle;

import com.carto.advancedmap.util.Description;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.core.MapPos;
import com.carto.core.ScreenPos;
import com.carto.datasources.BitmapOverlayRasterTileDataSource;
import com.carto.graphics.Bitmap;
import com.carto.layers.RasterTileLayer;
import com.carto.layers.TileSubstitutionPolicy;
import com.carto.projections.Projection;
import com.carto.utils.BitmapUtils;
import com.carto.core.MapPosVector;
import com.carto.core.ScreenPosVector;

/*
 * A sample demonstrating how to add ground level raster overlay. This samples
 * adds additional raster layer on top of base layer, using NTBitmapOverlayRasterTileDataSource.
 * Note: for really big overlays (containing 10000 pixels or more), Carto Mobile SDK provides
 * GDAL-based raster tile data source. This data source is not part of the standard SDK, it
 * is an extra feature provided using GIS-extensions package.
 */
@Description(value = "Show not tiled Bitmap on ground")
public class GroundOverlayActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);

        Projection proj = mapView.getOptions().getBaseProjection();
        
        // Load ground overlay bitmap
        //Bitmap androidMarkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jefferson);
        //com.carto.graphics.Bitmap overlayBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidMarkerBitmap);
        Bitmap overlayBitmap = BitmapUtils.loadBitmapFromAssets("jefferson-building-ground-floor.jpg");
        
        // Create two vectors containing geographical positions and corresponding raster image pixel coordinates.
        // 2, 3 or 4 points may be specified. Usually 2 points are enough (for conformal mapping).
        MapPos pos = proj.fromWgs84(new MapPos(-77.004590, 38.888702));
        double sizeNS = 110, sizeWE = 100;

        MapPosVector mapPoses = new MapPosVector();
        mapPoses.add(new MapPos(pos.getX()-sizeWE, pos.getY()+sizeNS));
        mapPoses.add(new MapPos(pos.getX()+sizeWE, pos.getY()+sizeNS));
        mapPoses.add(new MapPos(pos.getX()+sizeWE, pos.getY()-sizeNS));
        mapPoses.add(new MapPos(pos.getX()-sizeWE, pos.getY()-sizeNS));

        ScreenPosVector bitmapPoses = new ScreenPosVector();
        bitmapPoses.add(new ScreenPos(0, 0));
        bitmapPoses.add(new ScreenPos(0, overlayBitmap.getHeight()));
        bitmapPoses.add(new ScreenPos(overlayBitmap.getWidth(), overlayBitmap.getHeight()));
        bitmapPoses.add(new ScreenPos(overlayBitmap.getWidth(), 0));
        
        // Create bitmap overlay raster tile data source
        BitmapOverlayRasterTileDataSource rasterDataSource = new BitmapOverlayRasterTileDataSource(
                0, 20, overlayBitmap, proj, mapPoses, bitmapPoses
        );

        RasterTileLayer rasterLayer = new RasterTileLayer(rasterDataSource);
        mapView.getLayers().add(rasterLayer);
        
        // Apply zoom level bias to the raster layer.
        // By default, bitmaps are upsampled on high-DPI screens.
        // We will correct this by applying appropriate bias
        float zoomLevelBias = (float) (Math.log(mapView.getOptions().getDPI() / 160.0f) / Math.log(2));

        rasterLayer.setZoomLevelBias(zoomLevelBias * 0.75f);
        rasterLayer.setTileSubstitutionPolicy(TileSubstitutionPolicy.TILE_SUBSTITUTION_POLICY_VISIBLE);
        
        mapView.setFocusPos(pos, 0);
        mapView.setZoom(15.5f, 0);
    }
}
