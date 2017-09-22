package com.carto.advancedmap.sections.basemap;

import android.content.Context;

import com.carto.advanced.kotlin.components.PopupButton;
import com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent.LanguagePopupContent;
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContent;
import com.carto.advancedmap.R;
import com.carto.advancedmap.baseclasses.views.MapBaseView;
import com.carto.advancedmap.utils.Sources;
import com.carto.core.BinaryData;
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

public class BaseMapsView extends MapBaseView
{
    public static final CartoBaseMapStyle DEFAULT_STYLE = CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER;

    public PopupButton styleButton;
    public PopupButton languageButton;

    public StylePopupContent styleContent;
    public LanguagePopupContent languageContent;

    public BaseMapsView(Context context)
    {
        super(context);

        styleButton = new PopupButton(context, R.drawable.icon_basemap);
        addButton(styleButton);

        languageButton = new PopupButton(context, R.drawable.icon_language);
        addButton(languageButton);

        styleContent = new StylePopupContent(context);
        languageContent = new LanguagePopupContent(context);

        setMainViewFrame();
    }

    public void setLanguageContent() {
        popup.getPopup().getHeader().setText("SELECT A LANGUAGE");
        setContent(languageContent);
    }

    public void setBasemapContent() {
        popup.getPopup().getHeader().setText("SELECT A BASEMAP");
        setContent(styleContent);
    }

    String currentOSM;
    String currentSelection;
    TileLayer currentLayer;

    VectorTileListener currentListener;

    public void updateBaseLayer(String selection, String source) {

        currentSelection = selection;
        currentOSM = source;

        if (source.equals(StylePopupContent.getCartoVectorSource())) {

            // Carto styles are bundled with the SDK, we can initialize them via constructor
            if (currentSelection.equals(StylePopupContent.getVoyager())) {
                currentLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);
            } else if (currentSelection.equals(StylePopupContent.getPositron())) {
                currentLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_POSITRON);
            } else {
                currentLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_DARKMATTER);
            }

        } else if (source.equals(StylePopupContent.getMapzenSource())) {

            // Mapzen styles are all bundled in one .zip file.
            // Selection contains both the style name and file name (cf. Sections.cs in Shared)
            String fileName = "styles_mapzen.zip";
            String styleName = "";

            if (selection.equals(StylePopupContent.getBright())) {
                styleName = "style";
            } else if (selection.equals(StylePopupContent.getPositron())) {
                styleName = "positron";
            } else if (selection.equals(StylePopupContent.getDarkMatter())) {
                styleName = "positron_dark";
            }
            // Create a style set from the file and style
            BinaryData styleAsset = AssetUtils.loadAsset(fileName);
            ZippedAssetPackage assetPackage = new ZippedAssetPackage(styleAsset);
            CompiledStyleSet styleSet = new CompiledStyleSet(assetPackage, styleName);

            // Create datasource and style decoder
            CartoOnlineTileDataSource ds = new CartoOnlineTileDataSource(currentOSM);
            MBVectorTileDecoder decoder = new MBVectorTileDecoder(styleSet);

            currentLayer = new VectorTileLayer(ds, decoder);

        } else {

            // We know that the value of raster will be Positron or Darkmatter,

            // Additionally, raster tiles do not support language choice
            String url;

            if (selection.equals(StylePopupContent.getPositron())) {
                url = StylePopupContent.getPositronUrl();
            } else {
                url = StylePopupContent.getDarkMatterUrl();
            }

            TileDataSource ds = new HTTPTileDataSource(1, 19, url);
            currentLayer = new RasterTileLayer(ds);
        }

        if (currentOSM.equals(Sources.CARTO_VECTOR)) {
            // 3D texts on by default
            CartoOnlineVectorTileLayer current = (CartoOnlineVectorTileLayer) currentLayer;
            MBVectorTileDecoder decoder = (MBVectorTileDecoder) current.getTileDecoder();
            decoder.setStyleParameter("texts3d", "1");
        }

        if (currentSelection.equals(StylePopupContent.getCartoRasterSource())) {
            languageButton.disable();
        } else {
            languageButton.enable();
        }

        mapView.getLayers().clear();
        mapView.getLayers().add(currentLayer);

        currentListener = initializeVectorTileListener();
    }

    void updateLanguage(String code) {
        if (currentLayer == null) {
            return;
        }

        CartoOnlineVectorTileLayer current = (CartoOnlineVectorTileLayer)currentLayer;
        MBVectorTileDecoder decoder = (MBVectorTileDecoder)current.getTileDecoder();
        decoder.setStyleParameter("lang", code);
    }

    VectorTileListener initializeVectorTileListener() {

        Projection projection = mapView.getOptions().getBaseProjection();
        LocalVectorDataSource source = new LocalVectorDataSource(projection);

        VectorLayer vectorLayer = new VectorLayer(source);
        mapView.getLayers().add(vectorLayer);

        Layer layer = mapView.getLayers().get(0);

        VectorTileListener listener = new VectorTileListener(vectorLayer);

        if (layer instanceof VectorTileLayer) {
            ((VectorTileLayer)layer).setVectorTileEventListener(listener);
        }

        return listener;
    }
}
