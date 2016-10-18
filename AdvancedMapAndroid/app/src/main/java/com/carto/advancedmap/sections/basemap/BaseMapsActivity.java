package com.carto.advancedmap.sections.basemap;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.carto.advancedmap.mapbase.MapSampleBaseActivity;
import com.carto.advancedmap.util.Description;
import com.carto.core.BinaryData;
import com.carto.datasources.CartoOnlineTileDataSource;
import com.carto.layers.VectorTileLayer;
import com.carto.styles.CompiledStyleSet;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;
import com.carto.vectortiles.MBVectorTileDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aareundo on 14/10/16.
 */

@Description(value = "Choice between different base maps, styles, languages")
public class BaseMapsActivity extends MapSampleBaseActivity {

    Menu OSMMenu, languageMenu, tileTypeMenu, styleMenu;
    String currentOSM, currentLanguage, currentTileType, currentStyle;

    Map<String, String> OSMMap = new HashMap<String, String>() {{
        put("Nutiteq", "nutiteq.osm");
        put("Mapzen", "mapzen.osm");
    }};

    Map<String, String> languageMap = new HashMap<String, String>() {{
        put("English", "en");
        put("German",  "de");
        put("Spanish",  "es");
        put("Italian",  "it");
        put("French",  "fr");
        put("Russian", "ru");
        put("Chinese", "zh");
    }};

    Map<String, String> tileTypeMap = new HashMap<String, String>() {{
        put("Vector", "vector");
        put("Raster", "raster");
    }};

    Map<String, String> nutiteqStyleMap = new HashMap<String, String>() { {
        put("Default", "nutibright-v3:default");
        put("Dark", "nutibright-v3:nutiteq_dark");
        put("Gray", "nutibright-v3:nutiteq_grey");
    }};

    Map<String, String> mapzenStyleMap = new HashMap<String, String>() {{
        put("Positron", "positron");
        put("Dark Matter", "dark_matter");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentOSM = OSMMap.entrySet().iterator().next().getValue();
        currentLanguage = languageMap.entrySet().iterator().next().getValue();
        currentTileType = tileTypeMap.entrySet().iterator().next().getValue();
        currentStyle = getBaseStyle();

        updateBaseLayer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        OSMMenu = menu.addSubMenu("OSM");
        languageMenu = menu.addSubMenu("Language");
        tileTypeMenu = menu.addSubMenu("Tile type");
        styleMenu = menu.addSubMenu("Style");

        setMenuItem(OSMMap, OSMMenu, MenuListener.OSM_ID);
        setMenuItem(languageMap, languageMenu, MenuListener.LANGUAGE_ID);
        setMenuItem(tileTypeMap, tileTypeMenu, MenuListener.TILE_ID);
        setMenuItem(findStyleMap(), styleMenu, MenuListener.STYLE_ID);

        return super.onCreateOptionsMenu(menu);
    }

    private void setMenuItem(Map<String, String> map, Menu menu, int id) {

        boolean initialItemSet = false;

        for (Map.Entry<String, String> item : map.entrySet()) {
            MenuItem menuItem = menu.add(item.getKey());

            MenuListener listener = new MenuListener(menu);
            listener.id = id;
            menuItem.setOnMenuItemClickListener(listener);

            if (!initialItemSet) {
                listener.setIconToItem(menuItem);
                initialItemSet = true;
            }
        }
    }

    private void updateBaseLayer() {

        boolean styleBuildings3D = false;

        CompiledStyleSet vectorTileStyleSet;

        if (currentStyle.contains(":")) {

            String[] split = currentStyle.split(":");
            String fileName = split[0];
            String styleName = split[1];

            String styleAssetName = fileName + ".zip";
            BinaryData styleBytes = AssetUtils.loadAsset(styleAssetName);

            // Create style set
            vectorTileStyleSet = new CompiledStyleSet(new ZippedAssetPackage(styleBytes), styleName);

        } else {

            if (currentStyle.equals("nutibright3d")) {
                currentStyle = getBaseStyle();
                styleBuildings3D = true;
            }

            String styleAssetName = currentStyle + ".zip";
            BinaryData styleBytes = AssetUtils.loadAsset(styleAssetName);

            // Create style set
            vectorTileStyleSet = new CompiledStyleSet(new ZippedAssetPackage(styleBytes));
        }

        MBVectorTileDecoder vectorTileDecoder = new MBVectorTileDecoder(vectorTileStyleSet);

        // Set language, language-specific texts from vector tiles will be used
        vectorTileDecoder.setStyleParameter("lang", currentLanguage);

        // OSM Bright style set supports choosing between 2d/3d buildings. Set corresponding parameter.

        vectorTileDecoder.setStyleParameter("buildings3d", styleBuildings3D ? "1": "0");
        vectorTileDecoder.setStyleParameter("markers3d",styleBuildings3D ? "1" : "0");
        vectorTileDecoder.setStyleParameter("texts3d",styleBuildings3D ? "1" : "0");

        // Create tile data source for vector tiles
        CartoOnlineTileDataSource vectorTileDataSource = new CartoOnlineTileDataSource(currentOSM);

        // Remove old base layer, create new base layer
        if (baseLayer != null) {
            mapView.getLayers().remove(baseLayer);
        }

        baseLayer = new VectorTileLayer(vectorTileDataSource, vectorTileDecoder);
        mapView.getLayers().insert(0, baseLayer);
    }

    private String getBaseStyle() {
        return findStyleMap().entrySet().iterator().next().getValue();
    }

    /*************
     * LISTENERS *
     *************/

    private class ListenerBase {

        protected Menu menu;

        private ListenerBase(Menu menu) {
            this.menu = menu;
        }

        void setIconToItem(MenuItem item) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setIcon(null);
            }

            item.setIcon(android.R.drawable.checkbox_on_background);
        }
    }

    private Map<String, String> findStyleMap() {

        if (currentOSM == "nutiteq.osm") {
            return nutiteqStyleMap;
        } else {
            return mapzenStyleMap;
        }
    }

    private String findValueInMap(Map<String, String> map, String title) {

        for (Map.Entry<String, String> item : map.entrySet()) {
            if (item.getKey() == title) {
                return  item.getValue();
            }
        }

        return null;
    }
    private class MenuListener extends ListenerBase implements MenuItem.OnMenuItemClickListener {

        static final int OSM_ID = 0;
        static final int LANGUAGE_ID = 1;
        static final int TILE_ID = 2;
        static final int STYLE_ID = 3;

        public int id;

        private MenuListener(Menu menu) {
            super(menu);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            setIconToItem(item);

            String title = item.getTitle().toString();

            if (id == OSM_ID) {
                currentOSM = findValueInMap(OSMMap, title);
            } else if (id == LANGUAGE_ID) {
                currentLanguage = findValueInMap(languageMap, title);
            } else if (id == TILE_ID) {
                currentTileType = findValueInMap(tileTypeMap, title);
            } else {
                currentStyle = findValueInMap(findStyleMap(), title);
            }

            updateBaseLayer();
            return true;
        }
    }
}
