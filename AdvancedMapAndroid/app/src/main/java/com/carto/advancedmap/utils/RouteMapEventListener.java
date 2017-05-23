package com.carto.advancedmap.utils;

/**
 * Created by aareundo on 11/04/2017.
 */


import com.carto.core.MapPos;
import com.carto.ui.ClickType;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;

/**
 * This MapListener waits for two clicks on map - first to set routing start point, and then
 * second to mark end point and start routing service.
 */
public class RouteMapEventListener extends MapEventListener {

    private MapPos startPos;
    private MapPos stopPos;

    RouteCalculator calculator;

    public RouteMapEventListener(RouteCalculator calculator) {
        this.calculator = calculator;
    }

    // Map View manipulation handlers
    @Override
    public void onMapClicked(MapClickInfo mapClickInfo) {

        if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {

            MapPos clickPos = mapClickInfo.getClickPos();

            if (startPos == null) {

                // set start, or start again
                startPos = clickPos;
                calculator.setStartMarker(clickPos);

            } else if (stopPos == null) {

                // set stop and calculate
                stopPos = clickPos;
                calculator.setStopMarker(clickPos);
                calculator.showRoute(startPos, stopPos);

                // restart to force new route next time
                startPos = null;
                stopPos = null;
            }
        }
    }

    @Override
    public void onMapMoved() {
    }
}