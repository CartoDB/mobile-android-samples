package com.carto.cartomap.sections.importapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.carto.cartomap.util.ActivityData;

/**
 * Created by aareundo on 14/10/16.
 */

@ActivityData(name = "", description = "Packaging tiles (?)")
public class TilePackagerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Currently under development", Toast.LENGTH_LONG).show();

        finish();
    }
}
