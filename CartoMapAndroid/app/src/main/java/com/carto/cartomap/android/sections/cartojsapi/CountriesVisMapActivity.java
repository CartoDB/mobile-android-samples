package com.carto.cartomap.android.sections.cartojsapi;

import android.os.Bundle;

import com.carto.cartomap.android.util.Description;

/**
 * Created by aareundo on 13/10/16.
 */
@Description(value = "Vis displaying countries in different colors")
public class CountriesVisMapActivity extends VisJsonBaseMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateVis("http://documentation.cartodb.com/api/v2/viz/2b13c956-e7c1-11e2-806b-5404a6a683d5/viz.json");
    }
}
