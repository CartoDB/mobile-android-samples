package com.carto.advancedmap3.datasource;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.carto.core.MapTile;
import com.carto.datasources.TileDataSource;
import com.carto.core.TileData;
import com.carto.utils.BitmapUtils;

/**
 * A custom raster tile data source that loads tiles from two sources and then blends
 * them into a single tile.
 */
public class MyMergedRasterTileDataSource extends TileDataSource {
	private TileDataSource dataSource1;
    private TileDataSource dataSource2;
    private Paint paint;

	public MyMergedRasterTileDataSource(TileDataSource dataSource1, TileDataSource dataSource2) {
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
