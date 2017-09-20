
package com.carto.advancedmap.sections.offlinemap;

import android.os.Bundle;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.shared.activities.BoundingBoxActivity;
import com.carto.advancedmap.utils.BoundingBox;
import com.carto.advancedmap.utils.Sources;
import com.carto.packagemanager.CartoPackageManager;

import java.io.IOException;

/**
 * Created by aareundo on 10/11/16.
 */

@ActivityData(name = "Basic Package Manager", description = "Download a bounding box of London")
public class BasicPackageManagerActivity extends BoundingBoxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView.setBaseLayer();
    }

    @Override
    protected String createPackageFolder() {
        return createPackageFolder("citypackages");
    }

    @Override
    protected CartoPackageManager getPackageManager(String folder) {

        try {
            return new CartoPackageManager(Sources.CARTO_VECTOR, folder);
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    protected BoundingBox getBoundingBox() {
        // London
        return new BoundingBox(-0.8164, 51.2382, 0.6406, 51.7401);
    }
}
