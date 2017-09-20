package com.carto.advancedmap.sections.overlaydatasources;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.core.MapPos;
import com.carto.core.MapTile;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.datasources.components.TileData;
import com.carto.layers.RasterTileLayer;
import com.carto.utils.BitmapUtils;

@ActivityData(name = "Custom Raster Data Source", description = "Customized raster tile data source")
public class CustomRasterDataSourceActivity extends MapBaseActivity {

    public static final String TILED_RASTER_URL = "http://{s}.basemaps.cartocdn.com/light_all/{zoom}/{x}/{y}.png";
    public static final String HILLSHADE_RASTER_URL = "http://tiles.wmflabs.org/hillshading/{zoom}/{x}/{y}.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize base and hillshade data sources
        TileDataSource baseTileDataSource = new HTTPTileDataSource(0, 24, TILED_RASTER_URL);
        TileDataSource hillshadeTileDataSource = new HTTPTileDataSource(0, 24, HILLSHADE_RASTER_URL);
        
        // Create merged raster data source
        TileDataSource mergedTileDataSource = new MyMergedRasterTileDataSource(baseTileDataSource, hillshadeTileDataSource);

        // Create raster layer
        baseLayer = new RasterTileLayer(mergedTileDataSource);
        mapView.getLayers().add(baseLayer);
        
        // finally animate map to a nice place
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(-122.4323, 37.7582)), 1);
        mapView.setZoom(13, 1);
    }

    /**
     * A custom raster tile data source that loads tiles from two sources and then blends
     * them into a single tile.
     */
    private static class MyMergedRasterTileDataSource extends TileDataSource {

        private TileDataSource dataSource1;
        private TileDataSource dataSource2;
        private Paint paint;

        MyMergedRasterTileDataSource(TileDataSource dataSource1, TileDataSource dataSource2) {
            super(dataSource1.getMinZoom(), dataSource1.getMaxZoom());
            this.dataSource1 = dataSource1;
            this.dataSource2 = dataSource2;
        }

        public TileData loadTile(MapTile tile) {
            TileData tileData1 = dataSource1.loadTile(tile);
            TileData tileData2 = dataSource2.loadTile(tile);

            if (tileData1 == null) {
                return tileData2;
            }

            if (tileData2 == null) {
                return tileData1;
            }

            // Combine the bitmaps
            com.carto.graphics.Bitmap tileBitmap1 = com.carto.graphics.Bitmap.createFromCompressed(tileData1.getData());
            com.carto.graphics.Bitmap tileBitmap2 = com.carto.graphics.Bitmap.createFromCompressed(tileData2.getData());
            Bitmap bitmap1 = BitmapUtils.createAndroidBitmapFromBitmap(tileBitmap1);
            Bitmap bitmap2 = BitmapUtils.createAndroidBitmapFromBitmap(tileBitmap2);

            if (paint == null) {
                paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            }

            Canvas canvas = new Canvas(bitmap1);
            canvas.drawBitmap(bitmap2, null, new Rect(0, 0, bitmap1.getWidth(), bitmap1.getHeight()), paint);

            return new TileData(BitmapUtils.createBitmapFromAndroidBitmap(bitmap1).compressToInternal());
        }

    }
}
