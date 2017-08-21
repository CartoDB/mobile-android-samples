package com.carto.advancedmap.sections.geocoding;

import com.carto.core.Address;
import com.carto.geocoding.GeocodingResult;

/**
 * Created by aareundo on 21/08/2017.
 */

public class GeocodingUtils {

    public static String getPrettyAddres(Address address) {

        String string = "";

        if (!address.getName().isEmpty()) {
            string += address.getName();
        }

        if (!address.getStreet().isEmpty()) {
            if (string.length() > 0) {
                string += ", ";
            }
            string += address.getStreet();
        }

        if (!address.getHouseNumber().isEmpty()) {
            string += " " + address.getHouseNumber();
        }

        if (!address.getNeighbourhood().isEmpty()) {
            if (string.length() > 0) {
                string += ", ";
            }
            string += address.getNeighbourhood();
        }

        if (!address.getLocality().isEmpty()) {
            if (string.length() > 0) {
                string += ", ";
            }
            string += address.getLocality();
        }

        if (!address.getCounty().isEmpty()) {
            if (string.length() > 0) {
                string += ", ";
            }
            string += address.getCounty();
        }

        if (!address.getRegion().isEmpty()) {
            if (string.length() > 0) {
                string += ", ";
            }
            string += address.getRegion();
        }

        if (!address.getCountry().isEmpty()) {
            if (string.length() > 0) {
                string += ", ";
            }
            string += address.getCountry();
        }

        return string;
    }
}
