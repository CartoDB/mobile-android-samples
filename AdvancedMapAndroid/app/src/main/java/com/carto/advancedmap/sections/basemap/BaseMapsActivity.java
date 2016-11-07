package com.carto.advancedmap.sections.basemap;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.carto.advancedmap.baseactivities.MapSampleBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.core.BinaryData;
import com.carto.datasources.CartoOnlineTileDataSource;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.RasterTileLayer;
import com.carto.layers.TileLayer;
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

@ActivityData(name = "Base Maps", description = "Choice between different base maps, styles, languages")
public class BaseMapsActivity extends MapSampleBaseActivity {

    public static final String POSITRON_URL = "http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png";
    public static final String DARKMATTER_URL = "http://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}.png";

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
        put("Gray", "nutibright-v3:nutiteq_gray");
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

        mapView.getLayers().clear();

        TileLayer layer;

        if (currentTileType.equals("raster")) {
            String url;

            if (currentStyle.equals("positron")) {
                url = POSITRON_URL;
            } else {
                url = DARKMATTER_URL;
            }

            TileDataSource source = new HTTPTileDataSource(1, 19, url);
            layer = new RasterTileLayer(source);
        } else {

            if (currentOSM == "nutiteq.osm") {

                if (!isCurrentStyleNutiteq()) {
                    currentStyle = nutiteqStyleMap.entrySet().iterator().next().getValue();
                }

                // Use one of CARTO SDK's built-in styles
                if (currentStyle.split(":")[1].equals("default")) {
                    layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
                } else if (currentStyle.split(":")[1].equals("nutiteq_gray")) {
                    layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY);
                } else {
                    layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARK);
                }

                // When using built-in styles, the decoder and datasource are automatically created,
                // so we just need to access them in order to change the language
                MBVectorTileDecoder decoder = (MBVectorTileDecoder)((VectorTileLayer)layer).getTileDecoder();
                decoder.setStyleParameter("lang", currentLanguage);

            } else {

                if (isCurrentStyleNutiteq()) {
                    currentStyle = mapzenStyleMap.entrySet().iterator().next().getValue();
                }

                // Be sure you have mapzen styles (dark_matter.zip, positron.zip) in your asset folder
                BinaryData bytes = AssetUtils.loadAsset(currentStyle + ".zip");
                CompiledStyleSet styleSet = new CompiledStyleSet(new ZippedAssetPackage(bytes));

                MBVectorTileDecoder decoder = new MBVectorTileDecoder(styleSet);
                decoder.setStyleParameter("lang", currentLanguage);

                CartoOnlineTileDataSource source = new CartoOnlineTileDataSource(currentOSM);

                layer = new VectorTileLayer(source, decoder);
            }
        }

        mapView.getLayers().add(layer);
    }

    private boolean isCurrentStyleNutiteq() {

        for (Map.Entry item : nutiteqStyleMap.entrySet()) {
            if (item.getValue() == currentStyle) {
                return  true;
            }
        }
        return false;
    }

    private String getBaseStyle() {
        return findStyleMap().entrySet().iterator().next().getValue();
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

    /*************
     * LISTENER *
     *************/

    private class MenuListener implements MenuItem.OnMenuItemClickListener {

        static final int OSM_ID = 0;
        static final int LANGUAGE_ID = 1;
        static final int TILE_ID = 2;
        static final int STYLE_ID = 3;

        public int id;

        protected Menu menu;

        private MenuListener(Menu menu) { this.menu = menu; }

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

            if (id == OSM_ID) {
                updateStyleMenu();
            }
            updateBaseLayer();

            return true;
        }

        void setIconToItem(MenuItem item) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setIcon(null);
            }

            item.setIcon(android.R.drawable.checkbox_on_background);
        }

        private void updateStyleMenu() {
            styleMenu.clear();
            setMenuItem(findStyleMap(), styleMenu, MenuListener.STYLE_ID);
        }
    }
}
