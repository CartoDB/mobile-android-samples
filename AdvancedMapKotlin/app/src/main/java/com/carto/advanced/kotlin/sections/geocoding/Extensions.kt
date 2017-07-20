package com.carto.advanced.kotlin.sections.geocoding

import com.carto.geocoding.GeocodingResult

/**
 * Created by aareundo on 20/07/2017.
 */
fun GeocodingResult.getPrettyAddress(): String {

    var string = ""

    if (address.name.isNotEmpty()) {
        string += address.name
    }

    if (address.street.isNotEmpty()) {
        string += string.addCommaIfNecessary()
        string += address.street
    }

    if (address.houseNumber.isNotEmpty()) {
        string += " " + address.houseNumber
    }

    if (address.neighbourhood.isNotEmpty()) {
        string += string.addCommaIfNecessary()
        string += address.neighbourhood
    }

    if (address.locality.isNotEmpty()) {
        string += string.addCommaIfNecessary()
        string += address.locality
    }

    if (address.county.isNotEmpty()) {
        string += string.addCommaIfNecessary()
        string += address.county
    }

    if (address.region.isNotEmpty()) {
        string += string.addCommaIfNecessary()
        string += address.region
    }

    if (address.country.isNotEmpty()) {
        string += string.addCommaIfNecessary()
        string += address.country
    }

    return string
}

fun String.addCommaIfNecessary(): String {

    if (length > 0) {
        return ", "
    }

    return ""
}