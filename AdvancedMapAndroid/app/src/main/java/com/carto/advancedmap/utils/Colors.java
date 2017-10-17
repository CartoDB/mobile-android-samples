package com.carto.advancedmap.utils;

import android.graphics.Color;

/**
 * Created by aareundo on 08/11/16.
 */

public class Colors {

    public static int ACTION_BAR = Color.argb(255, 215, 82, 75);

    public static int DARK_TRANSPARENT_GRAY = Color.argb(200, 50, 50, 50);

    public static int LIGHT_TRANSPARENT_GRAY = Color.argb(120, 50, 50, 50);

    public static int LIGHT_TRANSPARENT_APPLE_BLUE = Color.argb(70, 14, 122, 254);

    public static int DARK_TRANSPARENT_APPLE_BLUE = Color.argb(150, 14, 122, 254);

    public static com.carto.graphics.Color toCartoColor(int color) {

        short red = (short)Color.red(color);
        short green = (short)Color.green(color);
        short blue = (short)Color.blue(color);
        short alpha = (short)Color.alpha(color);

        return new com.carto.graphics.Color(red, green, blue, alpha);
    }
}
