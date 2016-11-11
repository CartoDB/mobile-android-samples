package com.carto.cartomap.list;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.carto.cartomap.R;
import com.carto.cartomap.sections.BaseMapActivity;
import com.carto.cartomap.sections.cartojsapi.CountriesVisMapActivity;
import com.carto.cartomap.sections.cartojsapi.DotsVisMapActivity;
import com.carto.cartomap.sections.cartojsapi.FontsVisMapActivity;
import com.carto.cartomap.sections.header.CartoJSHeader;
import com.carto.cartomap.sections.header.MapsHeader;
import com.carto.cartomap.sections.header.SQLHeader;
import com.carto.cartomap.sections.header.TorqueHeader;
import com.carto.cartomap.sections.mapsapi.AnonymousVectorTableActivity;
import com.carto.cartomap.sections.mapsapi.NamedMapActivity;
import com.carto.cartomap.sections.mapsapi.AnonymousRasterTableActivity;
import com.carto.cartomap.sections.sqlapi.SQLServiceActivity;
import com.carto.cartomap.sections.torqueapi.TorqueShipActivity;
import com.carto.cartomap.util.ActivityData;

public class LauncherListActivity extends ListActivity {

    private Class[] samples = {

            CartoJSHeader.class,
            CountriesVisMapActivity.class,
            DotsVisMapActivity.class,
            FontsVisMapActivity.class,

//            ImportHeader.class,
//            TilePackagerActivity.class,

            MapsHeader.class,
            AnonymousRasterTableActivity.class,
            AnonymousVectorTableActivity.class,
            NamedMapActivity.class,

            SQLHeader.class,
            SQLServiceActivity.class,

            TorqueHeader.class,
            TorqueShipActivity.class,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable background = new ColorDrawable(BaseMapActivity.BACKGROUND_COLOR);
        getActionBar().setBackgroundDrawable(background);

        setContentView(R.layout.activity_launcher_list);

        ListView lv = this.getListView();
        lv.setBackgroundColor(Color.BLACK);
        lv.setAdapter(new MapListAdapter(this, android.R.layout.two_line_list_item, getListItems()));

        setTitle("CARTO Mobile Samples");
    }

    private MapListItem[] getListItems()
    {
        MapListItem[] items = new MapListItem[samples.length];

        for(int i = 0; i < samples.length; i++) {

            String name = "";
            String description = "";

            java.lang.annotation.Annotation[] annotations = samples[i].getAnnotations();

            if (annotations.length > 0 && annotations[0] instanceof ActivityData) {
                name = ((ActivityData) annotations[0]).name();
                description = ((ActivityData) annotations[0]).description();
            }

            MapListItem item = new MapListItem();


            if (description.equals("")) {
                item.name = name;
                item.isHeader = true;
            } else {
                item.name = name;
                item.description = description;
            }

            items[i] = item;
        }

        return  items;
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {

        Class sample = samples[position];

        ActivityData data = ((ActivityData) sample.getAnnotations()[0]);
        String name = data.name();
        String description = data.description();

        if (description.equals("")) {
            // Headers don't have descriptions and aren't clickable
            return;
        }

        Intent intent = new Intent(LauncherListActivity.this, sample);

        intent.putExtra("title", name);
        intent.putExtra("description", description);

        this.startActivity(intent);
    }
}
