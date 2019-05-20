package com.carto.advancedmap.sections.basemap;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.carto.advanced.kotlin.components.popupcontent.languagepopupcontent.LanguageCell;
import com.carto.advanced.kotlin.components.popupcontent.mapoptioncontent.MapOptionCell;
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContent;
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSection;
import com.carto.advanced.kotlin.components.popupcontent.stylepopupcontent.StylePopupContentSectionItem;
import com.carto.advancedmap.baseclasses.activities.MapBaseActivity;
import com.carto.advancedmap.model.Languages;
import com.carto.advancedmap.model.MapOptions;
import com.carto.core.MapPos;

import java.util.Arrays;

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
        ((BaseMapsView)contentView).languageContent.addItems(Arrays.asList(Languages.LIST));
        ((BaseMapsView)contentView).mapOptionsContent.addItems(Arrays.asList(MapOptions.LIST));
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

        ((BaseMapsView)contentView).languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseMapsView)contentView).setLanguageContent();
                contentView.popup.show();
            }
        });

        ((BaseMapsView)contentView).mapOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseMapsView)contentView).setMapOptionsContent();
                contentView.popup.show();
            }
        });

        final StylePopupContent styleContent = ((BaseMapsView)contentView).styleContent;
        for (final StylePopupContentSection section : styleContent.getItems()) {
            for (final StylePopupContentSectionItem item : section.getList()) {
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (styleContent.getPrevious() != null) {
                            styleContent.getPrevious().normalize();
                        }

                        item.highlight();
                        styleContent.setPrevious(item);

                        contentView.popup.hide(200);
                        String text = (String) item.getLabel().getText();
                        ((BaseMapsView)contentView).updateBaseLayer(text, section.getSource());
                    }
                });
            }
        }

        ((BaseMapsView)contentView).languageContent.getList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LanguageCell cell = (LanguageCell)view;
                contentView.popup.hide(200);
                ((BaseMapsView)contentView).updateLanguage(cell.getItem().value);
            }
        });

        ((BaseMapsView)contentView).mapOptionsContent.getList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MapOptionCell cell = (MapOptionCell)view;
                cell.getItem().value = !cell.getItem().value;
                contentView.popup.hide(200);
                ((BaseMapsView)contentView).updateMapOption(cell.getItem().tag, cell.getItem().value);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        ((BaseMapsView)contentView).styleButton.setOnClickListener(null);

        ((BaseMapsView)contentView).languageButton.setOnClickListener(null);

        ((BaseMapsView)contentView).mapOptionsButton.setOnClickListener(null);

        final StylePopupContent content = ((BaseMapsView)contentView).styleContent;
        for (final StylePopupContentSection section : content.getItems()) {
            for (final StylePopupContentSectionItem item : section.getList()) {
                item.setOnClickListener(null);
            }
        }
    }
}
