package com.carto.advancedmap.map;

import android.os.Bundle;

import com.carto.advancedmap.Description;
import com.carto.advancedmap.mapbase.VectorMapSampleBaseActivity;
import com.carto.advancedmap.listener.MyMapEventListener;
import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.MapPosVectorVector;
import com.carto.core.MapRange;
import com.carto.core.Variant;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.graphics.Color;
import com.carto.layers.VectorLayer;
import com.carto.styles.Polygon3DStyleBuilder;
import com.carto.utils.AssetUtils;
import com.carto.vectorelements.NMLModel;
import com.carto.vectorelements.Polygon3D;

//import com.carto.datasources.NMLModelLODTreeDataSource;
//import com.carto.datasources.SqliteNMLModelLODTreeDataSource;
//import com.carto.layers.NMLModelLODTreeLayer;

/**
 * A sample demonstrating how to use 3D vector elements: 3D polygon, 3D model (NML) and 3D city (NMLDB)
 */
@Description(value = "3D vector elements: 3D polygon, 3D model (NML) and 3D city (NMLDB)")
public class Overlays3DActivity extends VectorMapSampleBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapSampleBaseActivity creates and configures mapView  
        super.onCreate(savedInstanceState);
        
        // 1. Initialize a local vector data source and layer
        LocalVectorDataSource vectorDataSource = new LocalVectorDataSource(baseProjection);
        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer = new VectorLayer(vectorDataSource);
        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer);
        // Set visible zoom range for the vector layer
        vectorLayer.setVisibleZoomRange(new MapRange(10, 24));

        
        //2. Add a single 3D model to the vector layer
        String[] models = new String[] { "milktruck.nml" };
        int counter = 0;
        for (String modelName : models) {
            double dx = (counter++) * 0.001;
            MapPos modelPos = baseProjection.fromWgs84(new MapPos(24.646469, 59.423939 + dx));
            NMLModel model = new NMLModel(modelPos, AssetUtils.loadAsset(modelName));
            model.setScale(20);
            model.setMetaDataElement("ClickText", new Variant("Single model"));
            vectorDataSource.add(model);
        }
      
        // 3. Add one 3D polygon (with hole)
        // Create 3d polygon style and poses
        Polygon3DStyleBuilder polygon3DStyleBuilder = new Polygon3DStyleBuilder();
        polygon3DStyleBuilder.setColor(new Color(0xFF3333FF));
        MapPosVector polygon3DPoses = new MapPosVector();
        polygon3DPoses.add(baseProjection.fromWgs84(new MapPos(24.635930, 59.416659)));
        polygon3DPoses.add(baseProjection.fromWgs84(new MapPos(24.642453, 59.411354)));
        polygon3DPoses.add(baseProjection.fromWgs84(new MapPos(24.646187, 59.409607)));
        polygon3DPoses.add(baseProjection.fromWgs84(new MapPos(24.652667, 59.413123)));
        polygon3DPoses.add(baseProjection.fromWgs84(new MapPos(24.650736, 59.416703)));
        polygon3DPoses.add(baseProjection.fromWgs84(new MapPos(24.646444, 59.416245)));
        // Create 3d polygon holes poses
        MapPosVector polygon3DHolePoses = new MapPosVector();
        polygon3DHolePoses.add(baseProjection.fromWgs84(new MapPos(24.643409, 59.411922)));
        polygon3DHolePoses.add(baseProjection.fromWgs84(new MapPos(24.651207, 59.412896)));
        polygon3DHolePoses.add(baseProjection.fromWgs84(new MapPos(24.643207, 59.414411)));
        MapPosVectorVector polygon3DHoles = new MapPosVectorVector();
        polygon3DHoles.add(polygon3DHolePoses);
        // Add to datasource
        Polygon3D polygon3D = new Polygon3D(polygon3DPoses, polygon3DHoles, polygon3DStyleBuilder.buildStyle(), 150);
        polygon3D.setMetaDataElement("ClickText", new Variant("Polygon 3D"));
        vectorDataSource.add(polygon3D);
        
        // 4. Add 3D city (NMLDB), this is own separate layer
        
        /*
        // The NMLDB file must be copied to sdcard first. 3D city files can be very big and 
        // there's no way to open files from the bundle without fully extracting them to memory.
        String fileName = "saku_ios_4bpp.nmldb"; 
        String dir = getExternalFilesDir(null).toString();
        try {
            AssetCopy.copyAssetToSDCard(getAssets(), fileName, dir);
            Log.i(Const.LOG_TAG,"copy done to " + dir + "/"
                    + fileName);
            NMLModelLODTreeDataSource nmlDataSource = new SqliteNMLModelLODTreeDataSource(baseProjection, dir + "/" + fileName);
            NMLModelLODTreeLayer nmlLayer = new NMLModelLODTreeLayer(nmlDataSource);
            nmlLayer.setVisibleZoomRange(new MapRange(12.0f, 25.0f));
            mapView.getLayers().add(nmlLayer);

        } catch (IOException e) {
            Log.e(Const.LOG_TAG, "Failed to copy asset file: " + fileName, e);
        }
        */
        
        // 4. add also maplistener to detect click on model
        mapView.setMapEventListener(new MyMapEventListener(mapView, vectorDataSource));
        
        // finally animate map to the marker
        MapPos modelPosC = baseProjection.fromWgs84(new MapPos(24.646469, 59.423939));
        mapView.setFocusPos(modelPosC, 1);
        mapView.setZoom(14, 2);
        mapView.setTilt(50, 1);
    }
    
    @Override
    public void onDestroy() {
        mapView.setMapEventListener(null);
    	super.onDestroy();
    }
}
