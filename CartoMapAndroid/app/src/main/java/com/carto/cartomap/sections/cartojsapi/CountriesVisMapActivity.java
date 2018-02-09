package com.carto.cartomap.sections.cartojsapi;

import android.os.Bundle;

import com.carto.cartomap.util.ActivityData;

/**
 * Created by aareundo on 13/10/16.
 */

@ActivityData(name = "Countries Viz", description = "Vis displaying countries in different colors using UTFGrid")
public class CountriesVisMapActivity extends VisJsonMapBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateVis("http://documentation.cartodb.com/api/v2/viz/2b13c956-e7c1-11e2-806b-5404a6a683d5/viz.json");
    }
}
