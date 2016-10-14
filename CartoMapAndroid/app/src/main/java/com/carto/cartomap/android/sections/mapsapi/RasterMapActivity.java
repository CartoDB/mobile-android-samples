package com.carto.cartomap.android.sections.mapsapi;

import android.os.Bundle;
import android.widget.Toast;

import com.carto.cartomap.android.sections.BaseMapActivity;
import com.carto.cartomap.android.util.Description;

/**
 * Created by aareundo on 14/10/16.
 */

@Description(value = "Data as Raster Tiles, using CartoCSS styling")
public class RasterMapActivity extends BaseMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Currently under development", Toast.LENGTH_LONG).show();

        finish();
    }
}
