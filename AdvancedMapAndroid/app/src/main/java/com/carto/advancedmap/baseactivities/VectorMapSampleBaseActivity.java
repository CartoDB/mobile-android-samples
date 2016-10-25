package com.carto.advancedmap.baseactivities;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.carto.advancedmap.MapApplication;
import com.carto.core.MapRange;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.datasources.MemoryCacheTileDataSource;
import com.carto.datasources.CartoOnlineTileDataSource;
import com.carto.datasources.PersistentCacheTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.geometry.Feature;
import com.carto.geometry.Geometry;
import com.carto.geometry.LineGeometry;
import com.carto.geometry.MultiGeometry;
import com.carto.geometry.PointGeometry;
import com.carto.geometry.PolygonGeometry;
import com.carto.graphics.Color;
import com.carto.layers.Layer;
import com.carto.layers.VectorLayer;
import com.carto.layers.VectorTileEventListener;
import com.carto.layers.VectorTileLayer;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.GeometryCollectionStyleBuilder;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.ui.VectorTileClickInfo;
import com.carto.utils.AssetUtils;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.GeometryCollection;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.Polygon;
import com.carto.vectortiles.MBVectorTileDecoder;
import com.carto.styles.CompiledStyleSet;
import com.carto.utils.ZippedAssetPackage;
import com.carto.core.BinaryData;

/**
 * Base activity for vector map samples. Adds menu with multiple style choices.
 */
public class VectorMapSampleBaseActivity extends MapSampleBaseActivity {

	public static final String MAIN_STYLE = "nutibright-v3";

	protected TileDataSource vectorTileDataSource;
    protected MBVectorTileDecoder vectorTileDecoder;
    protected boolean persistentTileCache = false;
    
    // Style parameters
    protected String vectorStyleName = MAIN_STYLE; // default style name, each style has corresponding .zip asset
    protected String vectorStyleLang = "en"; // default map language

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Update options
        mapView.getOptions().setZoomRange(new MapRange(0, 20));
        
        // Set default base map - online vector with persistent caching
        updateBaseLayer();

		initializeVectorTileListener();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	Menu langMenu = menu.addSubMenu("Language");

    	addLanguageMenuOption(langMenu, "English", "en");
    	addLanguageMenuOption(langMenu, "German",  "de");
        addLanguageMenuOption(langMenu, "Spanish",  "es");
        addLanguageMenuOption(langMenu, "Italian",  "it");
    	addLanguageMenuOption(langMenu, "French",  "fr");
    	addLanguageMenuOption(langMenu, "Russian", "ru");
    	addLanguageMenuOption(langMenu, "Chinese", "zh");
   	
    	Menu styleMenu = menu.addSubMenu("Style");

    	addStyleMenuOption(styleMenu, "Basic", "basic");
    	addStyleMenuOption(styleMenu, "NutiBright 2D", MAIN_STYLE + ":default");
		addStyleMenuOption(styleMenu, "Nutiteq Dark", MAIN_STYLE + ":nutiteq_dark");
		addStyleMenuOption(styleMenu, "Nutiteq Grey", MAIN_STYLE + ":nutiteq_grey");
		addStyleMenuOption(styleMenu, "NutiBright 3D", "nutibright3d");
    	addStyleMenuOption(styleMenu, "Loose Leaf", "looseleaf");

    	return true;
    }
    
    private void addLanguageMenuOption(final Menu menu, String text, final String value) {
    	MenuItem menuItem = menu.add(text).setOnMenuItemClickListener(new OnMenuItemClickListener(){
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
    	    	vectorStyleLang = value;
    	    	updateBaseLayer();
    	        return true;
    	    }
    	});
    	if (vectorStyleLang.equals(value)) {
    		menuItem.setIcon(android.R.drawable.checkbox_on_background);
    	}
    }
    
    private void addStyleMenuOption(final Menu menu, String text, final String value) {
    	MenuItem menuItem = menu.add(text).setOnMenuItemClickListener(new OnMenuItemClickListener(){
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
    	    	vectorStyleName = value;
    	    	updateBaseLayer();
    	        return true;
    	    }
    	});
    	if (vectorStyleName.equals(value)) {
    		menuItem.setIcon(android.R.drawable.checkbox_on_background);
    	}    	
    }
    
    protected void updateBaseLayer() {

    	boolean styleBuildings3D = false;

		CompiledStyleSet vectorTileStyleSet;

		if (vectorStyleName.contains(":")) {

			String[] split = vectorStyleName.split(":");
			String fileName = split[0];
			String styleName = split[1];

			String styleAssetName = fileName + ".zip";
			BinaryData styleBytes = AssetUtils.loadAsset(styleAssetName);

			// Create style set
			vectorTileStyleSet = new CompiledStyleSet(new ZippedAssetPackage(styleBytes), styleName);

		} else {

			if (vectorStyleName.equals("nutibright3d")) {
				vectorStyleName = MAIN_STYLE;
				styleBuildings3D = true;
			}

			String styleAssetName = vectorStyleName + ".zip";
			BinaryData styleBytes = AssetUtils.loadAsset(styleAssetName);

			// Create style set
			vectorTileStyleSet = new CompiledStyleSet(new ZippedAssetPackage(styleBytes));
		}

		vectorTileDecoder = new MBVectorTileDecoder(vectorTileStyleSet);

		// Set language, language-specific texts from vector tiles will be used
		vectorTileDecoder.setStyleParameter("lang", vectorStyleLang);

		// OSM Bright style set supports choosing between 2d/3d buildings. Set corresponding parameter.

		vectorTileDecoder.setStyleParameter("buildings3d", styleBuildings3D ? "1": "0");
		vectorTileDecoder.setStyleParameter("markers3d",styleBuildings3D ? "1" : "0");
		vectorTileDecoder.setStyleParameter("texts3d",styleBuildings3D ? "1" : "0");

		// Create tile data source for vector tiles
		if (vectorTileDataSource == null) {
			vectorTileDataSource = createTileDataSource();
		}

		// Remove old base layer, create new base layer
		if (baseLayer != null) {
			mapView.getLayers().remove(baseLayer);
		}

		baseLayer = new VectorTileLayer(vectorTileDataSource, vectorTileDecoder);
		mapView.getLayers().insert(0, baseLayer);
    }
    
    protected TileDataSource createTileDataSource() {
        TileDataSource vectorTileDataSource = new CartoOnlineTileDataSource("nutiteq.osm");

        // We don't use vectorTileDataSource directly (this would be also option),
        // but via caching to cache data locally persistently/non-persistently
        // Note that persistent cache requires WRITE_EXTERNAL_STORAGE permission
        TileDataSource cacheDataSource = vectorTileDataSource;
        if (persistentTileCache) {
        	String cacheFile = getExternalFilesDir(null)+"/mapcache.db";
        	Log.i(MapApplication.LOG_TAG,"cacheFile = "+cacheFile);
        	cacheDataSource = new PersistentCacheTileDataSource(vectorTileDataSource, cacheFile);
        } else {
        	cacheDataSource = new MemoryCacheTileDataSource(vectorTileDataSource);
        }
    	return cacheDataSource;
    }

	VectorLayer vectorLayer;

	void initializeVectorTileListener()
	{
		if (vectorLayer == null)
		{
			LocalVectorDataSource source = new LocalVectorDataSource(baseProjection);
			vectorLayer = new VectorLayer(source);
			mapView.getLayers().add(vectorLayer);
		}

		Layer layer = mapView.getLayers().get(0);

		if (layer instanceof VectorTileLayer)
		{
			((VectorTileLayer)layer).setVectorTileEventListener(new VectorTileListener(vectorLayer));
		}
	}

	private class VectorTileListener extends VectorTileEventListener {
		VectorLayer layer;

		public VectorTileListener(VectorLayer layer)
		{
			this.layer = layer;
		}

		@Override
		public boolean onVectorTileClicked(VectorTileClickInfo clickInfo) {

			LocalVectorDataSource source = (LocalVectorDataSource)layer.getDataSource();

			source.clear();

			Color color = new Color((short)0, (short)100, (short)200, (short)150);

			Feature feature = clickInfo.getFeature();
			Geometry geometry = feature.getGeometry();

			PointStyleBuilder pointBuilder = new PointStyleBuilder();
			pointBuilder.setColor(color);

			LineStyleBuilder lineBuilder = new LineStyleBuilder();
			lineBuilder.setColor(color);

			PolygonStyleBuilder polygonBuilder = new PolygonStyleBuilder();
			polygonBuilder.setColor(color);

			if (geometry instanceof PointGeometry)
			{
				source.add(new Point((PointGeometry)geometry, pointBuilder.buildStyle()));
			}
			else if (geometry instanceof LineGeometry)
			{
				source.add(new Line((LineGeometry)geometry, lineBuilder.buildStyle()));
			}
			else if (geometry instanceof PolygonGeometry)
			{
				source.add(new Polygon((PolygonGeometry)geometry, polygonBuilder.buildStyle()));
			}
			else if (geometry instanceof MultiGeometry)
			{
				GeometryCollectionStyleBuilder collectionBuilder = new GeometryCollectionStyleBuilder();
				collectionBuilder.setPointStyle(pointBuilder.buildStyle());
				collectionBuilder.setLineStyle(lineBuilder.buildStyle());
				collectionBuilder.setPolygonStyle(polygonBuilder.buildStyle());

				source.add(new GeometryCollection((MultiGeometry)geometry, collectionBuilder.buildStyle()));
			}

			BalloonPopupStyleBuilder builder = new BalloonPopupStyleBuilder();

			/// Set a higher placement priority so it would always be visible
			builder.setPlacementPriority(10);

			String message = feature.getProperties().toString();

			BalloonPopup popup = new BalloonPopup(clickInfo.getClickPos(), builder.buildStyle(), "Click", message);

			source.add(popup);

			return true;
		}
	}
}
