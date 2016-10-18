package com.carto.cartomap.sections.cartojsapi;

import android.os.Bundle;

import com.carto.cartomap.util.Description;

/**
 * Created by aareundo on 13/10/16.
 */
@Description(value = "Vis showing dots on the map")
public class DotsVisMapActivity extends VisJsonBaseMapActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateVis("https://documentation.cartodb.com/api/v2/viz/236085de-ea08-11e2-958c-5404a6a683d5/viz.json");
    }
}
