package com.carto.cartomap.android.sections.sqlapi;

import android.os.Bundle;

import com.carto.cartomap.android.sections.BaseMapActivity;
import com.carto.cartomap.android.util.Description;
import com.carto.core.Variant;
import com.carto.services.CartoSQLService;
import com.carto.utils.Log;

import java.io.IOException;

/**
 * Created by aareundo on 13/10/16.
 */

@Description(value = "Displays cities on the map via SQL query")
public class SQLServiceActivity extends BaseMapActivity {

    static final String query = "SELECT * FROM cities15000 WHERE population > 100000";
    Variant result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CartoSQLService service = new CartoSQLService();

        // Sample url:
        // https://cartomobile-team.carto.com/u/nutiteq/viz/f1407ed4-84b8-11e6-96bc-0ee66e2c9693/public_map

//        service.setUsername("nutiteq");
        service.setAPITemplate("https://nutiteq.cartodb.com");

        // Be sure to make network queries on another thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result = service.queryData(query);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Variant rows = result.getObjectElement("rows");

                for (int i = 0; i < rows.getArraySize(); i++) {

                    Variant row = rows.getArrayElement(i);
                    System.out.println("Geom: " + row.getObjectElement("the_geom"));
                }
            }
        });

        thread.start();
    }
}
