package com.carto.advancedmap.sections.overlaydatasources;

import android.net.Uri;
import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.baseactivities.VectorMapSampleBaseActivity;
import com.carto.core.MapBounds;
import com.carto.core.MapPos;
import com.carto.core.MapTile;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.layers.RasterTileLayer;
import com.carto.projections.Projection;

/**
 * A sample demonstrating how to use WMS service raster on top of the vector base map
 */
@ActivityData(name = "WMS Map", description = "Use external WMS service for raster tile overlay")
public class WmsMapActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);


        // USGS Base map: http://basemap.nationalmap.gov/arcgis/rest/services/USGSTopo/MapServer
        String url = "http://basemap.nationalmap.gov/arcgis/services/USGSTopo/MapServer/WmsServer?";
        String layers = "0";

        HttpWmsTileDataSource wms = new HttpWmsTileDataSource(0, 14, baseProjection, false, url, "", layers, "image/png8");
        RasterTileLayer wmsLayer = new RasterTileLayer(wms);

        // Calculate zoom bias, basically this is needed to 'undo' automatic DPI scaling, we will display original raster with close to 1:1 pixel density
        double zoomLevelBias = Math.log(mapView.getOptions().getDPI() / 160) / Math.log(2);
        wmsLayer.setZoomLevelBias((float) zoomLevelBias);

        mapView.getLayers().add(wmsLayer);
        
        // finally animate map to map coverage
        mapView.setFocusPos(baseProjection.fromWgs84(new MapPos(-100, 40)), 1);
        mapView.setZoom(5, 1);
    }

    /**
     * A custom tile source makes WMS request. Supports only WGS84 (EPSG:4326) requests
     */
    private class HttpWmsTileDataSource extends HTTPTileDataSource {

        private String baseUrl;

        private String layer;
        private String format;
        private String style;
        private boolean wgsWms;
        private Projection projection;

        private int tileSize = 256;

        /**
         * Creates WMS DataSource, based on tiles. Map service must support EPSG:3857 or EPSG:4326.
         *     - EPSG:3857 has full compatibility and accuracy,
         *     - many services with EPSG:4326 work also, but on low zooms (world view) it is inaccurate,
         *     because map display projection is always EPSG:3857 (in SDK 3.0)
         *
         *     Only GetMap is implemented
         *
         * @param minZoom minimal zoom
         * @param maxZoom max zoom for map server
         * @param proj datasource projection, currently should be EPSG:3857
         * @param wgsWms false - uses EPSG:3857 for server, true - uses WGS84 (EPSG:4326) which is less accurate in low zooms
         * @param baseUrl You need to configure direct map URL, GetCapabilities is NOT used here
         * @param style - usually empty string, comma separated
         * @param layer comma-separated list of layers
         * @param format e.g. image/png, image/jpeg
         */
         HttpWmsTileDataSource(int minZoom, int maxZoom, Projection proj, boolean wgsWms,
                                     String baseUrl, String style, String layer, String format) {
            super(minZoom, maxZoom, baseUrl);
            this.baseUrl = baseUrl;
            this.style = style;
            this.layer = layer;
            this.format = format;
            this.wgsWms = wgsWms;
            this.projection = proj;
        }

        @Override
        protected String buildTileURL(String baseUrl, MapTile tile) {

            String srs = "EPSG:3857";
            String bbox = getTileBbox(tile);

            if(wgsWms){
                srs = "EPSG:4326";
            }

            Uri.Builder uri = createBaseUri("GetMap", srs);
            uri.appendQueryParameter("BBOX", bbox);
            return uri.build().toString();
        }

        private Uri.Builder createBaseUri(String request, String srs) {

            Uri.Builder uri = Uri.parse(baseUrl).buildUpon();
            uri.appendQueryParameter("LAYERS", layer);
            uri.appendQueryParameter("FORMAT", format);
            uri.appendQueryParameter("SERVICE", "WMS");
            uri.appendQueryParameter("VERSION", "1.1.0");
            uri.appendQueryParameter("REQUEST", request);
            uri.appendQueryParameter("STYLES", style);
            uri.appendQueryParameter("EXCEPTIONS", "application/vnd.ogc.se_inimage");
            uri.appendQueryParameter("SRS", srs);
            uri.appendQueryParameter("WIDTH", Integer.toString(tileSize));
            uri.appendQueryParameter("HEIGHT", Integer.toString(tileSize));
            return uri;
        }

        private String getTileBbox(MapTile tile) {

            MapBounds envelope = tileBounds(tile.getX(), tile.getY(), tile.getZoom(), projection);

            // Convert corners to WGS84 if maps server needs it.
            if(wgsWms){
                envelope = new MapBounds(projection.toWgs84(envelope.getMin()),projection.toWgs84(envelope.getMax()));
            }

            String bbox = ""
                    + envelope.getMin().getX() + "," + envelope.getMin().getY() + ","
                    + envelope.getMax().getX() + "," + envelope.getMax().getY();
            return bbox;
        }

        public MapBounds tileBounds(int tx, int ty, int zoom, Projection proj) {

            MapBounds bounds = proj.getBounds();

            double boundWidth = bounds.getMax().getX() - bounds.getMin().getX();
            double boundHeight = bounds.getMax().getY() - bounds.getMin().getY();

            int xCount = Math.max(1, (int) Math.round(boundWidth / boundHeight));
            int yCount = Math.max(1, (int) Math.round(boundHeight / boundWidth));

            double resx = boundWidth / xCount / (tileSize * (double) (1<<(zoom)));
            double resy = boundHeight / yCount / (tileSize * (double) (1<<(zoom)));

            double minx = ((double) tx + 0) * tileSize * resx + bounds.getMin().getX();
            double maxx = ((double) tx + 1) * tileSize * resx +  bounds.getMin().getX();
            double miny = -((double) ty + 1) * tileSize * resy +  bounds.getMax().getY();
            double maxy = -((double) ty + 0) * tileSize * resy + bounds.getMax().getY();

            MapBounds env = new MapBounds(new MapPos(minx,miny), new MapPos(maxx,maxy));

            return env;
        }
    }
}
