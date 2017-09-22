package com.carto.advancedmap.sections.basemap;

import android.os.Bundle;
import android.view.View;

import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContent;
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSection;
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSectionItem;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.core.MapPos;

/**
 * Created by aareundo on 08/11/16.
 */

public class BaseMapActivity extends MapBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView = new BaseMapsView(this);
        setContentView(contentView);

        // Zoom to Central Europe so some texts would be visible
        MapPos europe = contentView.projection.fromWgs84(new MapPos(15.2551, 54.5260));
        contentView.mapView.setFocusPos(europe, 0);
        contentView.mapView.setZoom(5, 0);

        ((BaseMapsView)contentView).styleContent.highlightDefault();
        ((BaseMapsView)contentView).updateBaseLayer(
                StylePopupContent.getVoyager(),
                StylePopupContent.getCartoVectorSource()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();


        ((BaseMapsView)contentView).styleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseMapsView)contentView).setBasemapContent();
                contentView.popup.show();
            }
        });

        final StylePopupContent content = ((BaseMapsView)contentView).styleContent;
        for (final StylePopupContentSection section : content.getItems()) {
            for (final StylePopupContentSectionItem item : section.getList()) {
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (content.getPrevious() != null) {
                            content.getPrevious().normalize();
                        }

                        item.highlight();
                        content.setPrevious(item);

                        contentView.popup.hide(200);
                        String text = (String) item.getLabel().getText();
                        ((BaseMapsView)contentView).updateBaseLayer(text, section.getSource());
                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((BaseMapsView)contentView).styleButton.setOnClickListener(null);

        final StylePopupContent content = ((BaseMapsView)contentView).styleContent;
        for (final StylePopupContentSection section : content.getItems()) {
            for (final StylePopupContentSectionItem item : section.getList()) {
                item.setOnClickListener(null);
            }
        }
    }
}
