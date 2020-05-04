package com.carto.advancedmap.sections.basemap;

import android.content.Context;

import com.carto.advanced.kotlin.components.PopupButton;
import com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent.LanguagePopupContent;
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContent;
import com.carto.advancedmap.R;
import com.carto.advancedmap.baseclasses.views.MapBaseView;
import com.carto.advancedmap.kotlinui.mapoptioncontent.MapOptionPopupContent;
import com.carto.components.RenderProjectionMode;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.CartoBaseMapPOIRenderMode;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineRasterTileLayer;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.CartoVectorTileLayer;
import com.carto.layers.Layer;
import com.carto.layers.TileLayer;
import com.carto.layers.VectorLayer;
import com.carto.layers.VectorTileLayer;
import com.carto.projections.Projection;
import com.carto.vectortiles.MBVectorTileDecoder;

/**
 * Created by aareundo on 08/11/16.
 */

public class BaseMapsView extends MapBaseView
{
    public static final CartoBaseMapStyle DEFAULT_STYLE = CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER;

    public PopupButton styleButton;
    public PopupButton languageButton;
    public PopupButton mapOptionsButton;

    public StylePopupContent styleContent;
    public LanguagePopupContent languageContent;
    public MapOptionPopupContent mapOptionsContent;

    private String currentLanguage = "en";
    private boolean buildings3D = false;
    private boolean texts3D = true;
    private boolean pois = false;

    public BaseMapsView(Context context) {
        super(context);

        styleButton = new PopupButton(context, R.drawable.icon_basemap);
        addButton(styleButton);

        languageButton = new PopupButton(context, R.drawable.icon_language);
        addButton(languageButton);

        mapOptionsButton = new PopupButton(context, R.drawable.icon_switches);
        addButton(mapOptionsButton);

        styleContent = new StylePopupContent(context);
        languageContent = new LanguagePopupContent(context);
        mapOptionsContent = new MapOptionPopupContent(context);

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

    public void setMapOptionsContent() {
        popup.getPopup().getHeader().setText("CONFIGURE RENDERING");
        setContent(mapOptionsContent);
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
        } else if (source.equals(StylePopupContent.getCartoRasterSource())) {

            if (selection.equals(StylePopupContent.getHereSatelliteDaySource())) {
                currentLayer = new CartoOnlineRasterTileLayer("here.satellite.day@2x");
            } else if(selection.equals(StylePopupContent.getHereNormalDaySource())) {
                currentLayer = new CartoOnlineRasterTileLayer("here.normal.day@2x");
            }
        }

        // Raster tiles do not support language choice
        if (currentSelection.equals(StylePopupContent.getCartoRasterSource())) {
            languageButton.disable();
        } else {
            languageButton.enable();
        }

        mapView.getLayers().clear();
        mapView.getLayers().add(currentLayer);

        updateLanguage(currentLanguage);
        updateMapOption("buildings3d", buildings3D);
        updateMapOption("texts3d", texts3D);
        updateMapOption("pois", pois);

        currentListener = initializeVectorTileListener();
    }

    void updateLanguage(String code) {
        currentLanguage = code;

        if (!(currentLayer instanceof VectorTileLayer)) {
            return;
        }

        VectorTileLayer current = (VectorTileLayer)currentLayer;
        MBVectorTileDecoder decoder = (MBVectorTileDecoder)current.getTileDecoder();
        decoder.setStyleParameter("lang", currentLanguage);
    }

    void updateMapOption(String option, boolean value) {
        if (option.equals("globe")) {
            mapView.getOptions().setRenderProjectionMode(value ? RenderProjectionMode.RENDER_PROJECTION_MODE_SPHERICAL : RenderProjectionMode.RENDER_PROJECTION_MODE_PLANAR);
            return;
        }

        if (option.equals("buildings3d")) {
            buildings3D = value;
        }
        if (option.equals("texts3d")) {
            texts3D = value;
        }
        if (option.equals("pois")) {
            pois = value;
        }

        if (!(currentLayer instanceof VectorTileLayer)) {
            return;
        }

        VectorTileLayer current = (VectorTileLayer)currentLayer;
        MBVectorTileDecoder decoder = (MBVectorTileDecoder)current.getTileDecoder();
        if (option.equals("buildings3d")) {
            decoder.setStyleParameter("buildings", buildings3D ? "2" : "1");
        }
        if (option.equals("texts3d")) {
            decoder.setStyleParameter("texts3d", texts3D ? "1" : "0");
        }
        if (option.equals("pois") && currentLayer instanceof CartoVectorTileLayer) {
            CartoVectorTileLayer cartoLayer = (CartoVectorTileLayer)currentLayer;
            cartoLayer.setPOIRenderMode(pois ? CartoBaseMapPOIRenderMode.CARTO_BASEMAP_POI_RENDER_MODE_FULL : CartoBaseMapPOIRenderMode.CARTO_BASEMAP_POI_RENDER_MODE_NONE);
        }
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
