package com.carto.advancedmap.sections.basemap.model;

import com.carto.advancedmap.utils.Sources;

import java.util.ArrayList;

/**
 * Created by aareundo on 08/11/16.
 */

public class Sections
{
    private static ArrayList<Section> list;

    public static ArrayList<Section> getList()
    {
        if (list == null) {
            initializeList();
        }
        return list;
    }

    public static Section getDefault()
    {
        if (list == null) {
            initializeList();
        }
        return list.get(0);
    }

    public static Section getLanguage()
    {
        if (list == null) {
            initializeList();
        }
        return list.get(list.size() - 1);
    }

    public static String getBaseLanguageCode()
    {
        return "";
    }

    public static String getBaseStyleValue()
    {
        return getDefault().getStyles().get(0).getValue();
    }

    private static void initializeList()
    {
        NameValuePair osm = new NameValuePair("Carto Vector", Sources.CARTO_VECTOR);
        SectionType type = SectionType.VECTOR;

        ArrayList<NameValuePair> styles = new ArrayList<NameValuePair>(){{
            add(new NameValuePair("Voyager", "voyager"));
            add(new NameValuePair("Positron", "positron"));
            add(new NameValuePair("Darkmatter", "darkmatter"));
        }};

        final Section cartoVector = new Section(osm, type, styles);

        osm = new NameValuePair("MapZeN", Sources.MAPZEN);
        type = SectionType.VECTOR;

        styles = new ArrayList<NameValuePair>(){{
            add(new NameValuePair("Bright", "styles_mapzen:style"));
            add(new NameValuePair("Positron", "styles_mapzen:positron"));
            add(new NameValuePair("Dark Matter", "styles_mapzen:positron_dark"));
        }};

        final Section mapzen = new Section(osm, type, styles);

        osm = new NameValuePair("Carto Raster", Sources.CARTO_RASTER);
        type = SectionType.RASTER;

        styles = new ArrayList<NameValuePair>() {{
            add(new NameValuePair("Positron", "http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png"));
            add(new NameValuePair("Dark Matter", "http://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}.png"));
        }};

        final Section cartoRaster = new Section(osm, type, styles);

        osm = new NameValuePair("Language", "lang");
        type = SectionType.LANGUAGE;

        styles = new ArrayList<NameValuePair>() {{
            add(new NameValuePair("Default", ""));
            add(new NameValuePair("English", "en"));
            add(new NameValuePair("German", "de"));
            add(new NameValuePair("Spanish", "es"));
            add(new NameValuePair("Italian", "it"));
            add(new NameValuePair("French", "fr"));
            add(new NameValuePair("Russian", "ru"));

            // Chinese is supported, but disabled in our example,
            // as it requires an extra asset package (nutibright-v3-full.zip)
            // add(new NameValuePair("Chinese", "zh"));
        }};

        final Section language = new Section(osm, type, styles);

        list = new ArrayList<Section>(){ {
            add(cartoVector);
            add(mapzen);
            add(cartoRaster);
            add(language);
        } };
    }
}
