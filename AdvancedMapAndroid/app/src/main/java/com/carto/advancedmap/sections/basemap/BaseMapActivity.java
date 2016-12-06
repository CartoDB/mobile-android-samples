package com.carto.advancedmap.sections.basemap;

import android.os.Bundle;
import android.view.MenuItem;

import com.carto.advancedmap.baseactivities.BaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.sections.basemap.model.Section;
import com.carto.advancedmap.sections.basemap.model.SectionType;
import com.carto.advancedmap.sections.basemap.model.Sections;
import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.core.BinaryData;
import com.carto.core.MapPos;
import com.carto.datasources.CartoOnlineTileDataSource;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.Layer;
import com.carto.layers.RasterTileLayer;
import com.carto.layers.TileLayer;
import com.carto.layers.VectorLayer;
import com.carto.layers.VectorTileLayer;
import com.carto.projections.Projection;
import com.carto.styles.CompiledStyleSet;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;
import com.carto.vectortiles.MBVectorTileDecoder;

/**
 * Created by aareundo on 08/11/16.
 */

@ActivityData(name = "Base Maps", description = "Choice between different base maps, styles, languages")
public class BaseMapActivity extends BaseActivity {

    BaseMapsView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView = new BaseMapsView(this);
        setContentView(contentView);

        setTitle("Base maps");

        // Zoom to Central Europe so some texts would be visible
        MapPos europe = contentView.mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(15.2551, 54.5260));
        contentView.mapView.setFocusPos(europe, 0);
        contentView.mapView.setZoom(5, 0);

        alert("Click the menu to choose between different styles and languages");

        contentView.menu.setItems(Sections.getList());

        // Set initial style
        contentView.menu.setInitialItem(Sections.getNutiteq());
        contentView.menu.setInitialItem(Sections.getLanguage());

        updateBaseLayer(Sections.getNutiteq(), Sections.getBaseStyleValue());
        updateLanguage(Sections.getBaseLanguageCode());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        setTitle(title);
        getActionBar().setSubtitle(description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String currentOSM;
    String currentSelection;
    TileLayer currentLayer;

    public void updateBaseLayer(Section section, String selection)
    {
        if (section.getType() != SectionType.LANGUAGE) {
            currentOSM = section.getOSM().getValue();
            currentSelection = selection;
        }

        if (section.getType() == SectionType.VECTOR) {

            if (currentOSM == "nutiteq.osm") {
                // Nutiteq styles are bundled with the SDK, we can initialize them via constuctor
                if (currentSelection == "default") {
                    currentLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DEFAULT);
                } else if (currentSelection == "gray") {
                    currentLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_GRAY);
                } else {
                    currentLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARK);
                }
            } else if (currentOSM == "mapzen.osm") {
                // Mapzen styles are all bundled in one .zip file.
                // Selection contains both the style name and file name (cf. Sections.cs in Shared)
                String fileName = currentSelection.split(":")[0];
                String styleName = currentSelection.split(":")[1];

                // Create a style set from the file and style
                BinaryData styleAsset = AssetUtils.loadAsset(fileName + ".zip");
                ZippedAssetPackage assetPackage = new ZippedAssetPackage(styleAsset);
                CompiledStyleSet styleSet = new CompiledStyleSet(assetPackage, styleName);

                // Create datasource and style decoder
                CartoOnlineTileDataSource source = new CartoOnlineTileDataSource(currentOSM);
                MBVectorTileDecoder decoder = new MBVectorTileDecoder(styleSet);

                currentLayer = new VectorTileLayer(source, decoder);
            }

            resetLanguage();
            contentView.menu.setLanguageMenuEnabled(true);

        } else if (section.getType() == SectionType.RASTER) {
            // We know that the value of raster will be Positron or Darkmatter,
            // as Nutiteq and Mapzen use vector tiles

            // Additionally, raster tiles do not support language choice
            String url = currentSelection;

            TileDataSource source = new HTTPTileDataSource(1, 19, url);
            currentLayer = new RasterTileLayer(source);

            // Language choice not enabled in raster tiles
            contentView.menu.setLanguageMenuEnabled(false);

        } else if (section.getType() == SectionType.LANGUAGE) {
            if (currentLayer instanceof RasterTileLayer) {
                // Raster tile language chance is not supported
                return;
            }

            updateLanguage(selection);
        }

        contentView.mapView.getLayers().clear();
        contentView.mapView.getLayers().add(currentLayer);

        contentView.menu.hide();

        initializeVectorTileListener();
    }

    void resetLanguage() {

        contentView.menu.setInitialItem(Sections.getLanguage());
        updateLanguage(Sections.getBaseLanguageCode());
    }

    void updateLanguage(String code) {
        if (currentLayer == null) {
            return;
        }

        MBVectorTileDecoder decoder = (MBVectorTileDecoder)((VectorTileLayer)currentLayer).getTileDecoder();
        decoder.setStyleParameter("lang", code);
    }

    void initializeVectorTileListener()
    {
        Projection projection = contentView.mapView.getOptions().getBaseProjection();
        LocalVectorDataSource source = new LocalVectorDataSource(projection);

        VectorLayer vectorLayer = new VectorLayer(source);
        contentView.mapView.getLayers().add(vectorLayer);

        Layer layer = contentView.mapView.getLayers().get(0);

        if (layer instanceof VectorTileLayer)
        {
            ((VectorTileLayer)layer).setVectorTileEventListener(new VectorTileListener(vectorLayer));
        }
    }
}
