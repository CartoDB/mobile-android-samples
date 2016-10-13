package com.carto.cartomap.android.sections.sqlapi;

import android.os.Bundle;

import com.carto.cartomap.android.sections.BaseMapActivity;
import com.carto.cartomap.android.util.Description;
import com.carto.core.Variant;
import com.carto.services.CartoSQLService;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

@Description(value = "Displays cities on the map via SQL query")
public class SQLServiceActivity extends BaseMapActivity {

    static final String query = "SELECT cartodb_id,the_geom_webmercator AS the_geom,name,address,bikes,slot,field_8,field_9,field_16,field_17,field_18 FROM stations_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CartoSQLService service = new CartoSQLService();

        // Sample url:
        // https://cartomobile-team.carto.com/u/nutiteq/viz/f1407ed4-84b8-11e6-96bc-0ee66e2c9693/public_map

        service.setUsername("nutiteq");

        Variant result = null;

        try {
            result = service.queryData(query);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result);
    }
}
