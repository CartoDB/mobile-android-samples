package com.carto.cartomap.sections.mapsapi;

import android.os.Bundle;

import com.carto.cartomap.sections.BaseMapActivity;
import com.carto.cartomap.util.Description;
import com.carto.core.BinaryData;
import com.carto.core.MapPos;
import com.carto.core.StringVariantMap;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.Feature;
import com.carto.geometry.Geometry;
import com.carto.geometry.LineGeometry;
import com.carto.geometry.MultiGeometry;
import com.carto.geometry.PointGeometry;
import com.carto.geometry.PolygonGeometry;
import com.carto.graphics.Color;
import com.carto.layers.Layer;
import com.carto.layers.LayerVector;
import com.carto.layers.VectorLayer;
import com.carto.layers.VectorTileEventListener;
import com.carto.layers.VectorTileLayer;
import com.carto.projections.Projection;
import com.carto.services.CartoMapsService;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.CartoCSSStyleSet;
import com.carto.styles.GeometryCollectionStyleBuilder;
import com.carto.styles.LineStyleBuilder;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.ui.VectorTileClickInfo;
import com.carto.utils.AssetUtils;
import com.carto.utils.ZippedAssetPackage;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.GeometryCollection;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.Polygon;
import com.carto.vectortiles.MBVectorTileDecoder;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

@Description(value = "CARTO data as Vector Tiles, using CartoCSS styling")
public class NamedMapActivity extends BaseMapActivity {

    static final String baseUrl = "http://192.168.1.31/user/demo-admin/api/v1/map/";
    static final String api = "demo-admin@05be0b35@d299d92a84985240af8767694f134620:1476098385738/1/{z}/{x}/{y}.mvt";

    VectorLayer vectorLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CartoMapsService service = new CartoMapsService();
        service.setAPITemplate("demo-admin");
        // Use raster layers, not vector
        service.setDefaultVectorLayerMode(true);

        final Projection projection = mapView.getOptions().getBaseProjection();
        final MapPos hiiumaa = projection.fromWgs84(new MapPos(22.7478235498916, 58.8330577553785));

        // Be sure to make network queries on another thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String name = "tpl_708edf80_8bf0_11e6_806c_0e2b00211e61";
                    LayerVector layers = service.buildNamedMap(name, new StringVariantMap());

                    for (int i = 0; i < layers.size(); i++) {
                        Layer layer = layers.get(i);
                    }

                    HTTPTileDataSource sourceOver = new HTTPTileDataSource(0, 13, baseUrl + api);

                    // Load fonts
                    BinaryData fontData = AssetUtils.loadAsset("carto-fonts.zip");
                    ZippedAssetPackage fontPackage = new ZippedAssetPackage(fontData);

                    // Load CSS
                    String css = "";
                    CartoCSSStyleSet styleSheet = new CartoCSSStyleSet(css, fontPackage);

                    MBVectorTileDecoder decoder = new MBVectorTileDecoder(styleSheet);

                    VectorTileLayer overLayer = new VectorTileLayer(sourceOver, decoder);
                    mapView.getLayers().add(overLayer);

                    mapView.setFocusPos(hiiumaa, 0);
                    mapView.setZoom(5, 1);

                    LocalVectorDataSource dataSource = new LocalVectorDataSource(projection);

                    vectorLayer = new VectorLayer(dataSource);
                    mapView.getLayers().add(vectorLayer);

                    VectorTileListener listener = new VectorTileListener(vectorLayer);
                    overLayer.setVectorTileEventListener(listener);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
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

            // Set a higher placement priority so it would always be visible
            builder.setPlacementPriority(10);

            String message = feature.getProperties().toString();

            BalloonPopup popup = new BalloonPopup(clickInfo.getClickPos(), builder.buildStyle(), "Click", message);

            source.add(popup);

            return true;
        }
    }
}
