package com.carto.cartomap.android.sections.torqueapi;

import android.os.Bundle;
import android.util.Log;

import com.carto.cartomap.android.util.Description;
import com.carto.cartomap.android.basemap.VectorMapSampleBaseActivity;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.PersistentCacheTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.TorqueTileLayer;
import com.carto.styles.CartoCSSStyleSet;
import com.carto.vectortiles.TorqueTileDecoder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A sample demonstrating how to use Carto Torque tiles with CartoCSS styling
 */
@Description(value = "Torque showing shop movement during WWII")
public class TorqueShipsActivity extends VectorMapSampleBaseActivity {

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private static final long FRAME_TIME_MS = 100;

    private TorqueTileLayer torqueTileLayer;
    private boolean stopped;

    String cartoCSS = "#layer {\n"+
            "  comp-op: lighten;\n"+
            "  marker-type:ellipse;\n"+
            "  marker-width: 10;\n"+
            "  marker-fill: #FEE391;\n"+
            "  [value > 2] { marker-fill: #FEC44F; }\n"+
            "  [value > 3] { marker-fill: #FE9929; }\n"+
            "  [value > 4] { marker-fill: #EC7014; }\n"+
            "  [value > 5] { marker-fill: #CC4C02; }\n"+
            "  [value > 6] { marker-fill: #993404; }\n"+
            "  [value > 7] { marker-fill: #662506; }\n"+
            "\n"+
            "  [frame-offset = 1] {\n"+
            "    marker-width: 20;\n"+
            "    marker-fill-opacity: 0.1;\n"+
            "  }\n"+
            "  [frame-offset = 2] {\n"+
            "    marker-width: 30;\n"+
            "    marker-fill-opacity: 0.05;\n"+
            "  }\n"+
            "}\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);

        String encodedQuery =
                "WITH%20par%20AS%20(%20%20SELECT%20CDB_XYZ_Resolution({zoom})*1%20as%20res%2" +
                "C%20%20256%2F1%20as%20tile_size%2C%20CDB_XYZ_Extent({x}%2C%20{y}%2C%20{zoom})%20as" +
                "%20ext%20)%2Ccte%20AS%20(%20%20%20SELECT%20ST_SnapToGrid(i.the_geom_webmercator%2C" +
                "%20p.res)%20g%2C%20count(cartodb_id)%20c%2C%20floor((date_part(%27epoch%27%2C%20da" +
                "te)%20-%20-1796072400)%2F476536.5)%20d%20%20FROM%20(select%20*%20from%20ow)%20i%2C" +
                "%20par%20p%20%20%20WHERE%20i.the_geom_webmercator%20%26%26%20p.ext%20%20%20GROUP%2" +
                "0BY%20g%2C%20d)%20SELECT%20(st_x(g)-st_xmin(p.ext))%2Fp.res%20x__uint8%2C%20%20%20" +
                "%20%20%20%20%20(st_y(g)-st_ymin(p.ext))%2Fp.res%20y__uint8%2C%20array_agg(c)%20val" +
                "s__uint8%2C%20array_agg(d)%20dates__uint16%20FROM%20cte%2C%20par%20p%20where%20(st" +
                "_y(g)-st_ymin(p.ext))%2Fp.res%20%3C%20tile_size%20and%20(st_x(g)-st_xmin(p.ext))%2" +
                "Fp.res%20%3C%20tile_size%20GROUP%20BY%20x__uint8%2C%20y__uint8&last_updated=1970-0" +
                "1-01T00%3A00%3A00.000Z";

        // Define datasource with the query
        String url = "http://viz2.cartodb.com/api/v2/sql?q=" + encodedQuery + "&cache_policy=persist";
        HTTPTileDataSource torqueDataSource = new HTTPTileDataSource(0, 14, url);

        // Create persistent cache to make it faster
        String cacheFile = getExternalFilesDir(null)+"/torque_tile_cache.db";
        Log.i("LOG", "cacheFile = " + cacheFile);
        TileDataSource cacheDataSource = new PersistentCacheTileDataSource(torqueDataSource, cacheFile);

        // Create CartoCSS style from Torque points
        CartoCSSStyleSet torqueStyleSet = new CartoCSSStyleSet(cartoCSS);

        // Create tile decoder and Torque layer
        TorqueTileDecoder torqueDecoder = new TorqueTileDecoder(torqueStyleSet);

        torqueTileLayer = new TorqueTileLayer(cacheDataSource, torqueDecoder);

        mapView.getLayers().add(torqueTileLayer);

        // Start updating frames for animation
        worker.schedule(task, FRAME_TIME_MS, TimeUnit.MILLISECONDS);

        mapView.setZoom(1, 0);
    }

    @Override
    protected void onStop() {
        synchronized (worker) {
            stopped = true;
        }

        super.onStop();
    }

    private Runnable task = new Runnable() {
        public void run() {
            synchronized (worker) {

                int frameCount = ((TorqueTileDecoder)torqueTileLayer.getTileDecoder()).getFrameCount();
                int frameNr = (torqueTileLayer.getFrameNr()+1) % frameCount;

                torqueTileLayer.setFrameNr(frameNr);

                Log.d("LOG", "torque frame " + torqueTileLayer.getFrameNr()+ " of "+frameCount);

                if (!stopped) {
                    worker.schedule(task, FRAME_TIME_MS, TimeUnit.MILLISECONDS);
                }
            }
        }
    };

}
