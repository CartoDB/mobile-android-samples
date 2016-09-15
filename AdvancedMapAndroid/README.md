# Android sample app with CARTO Mobile SDK


This project shows API and features of CARTO Mobile SDK 4.x

[![Build Status](https://travis-ci.org/CartoDB/mobile-android-samples.svg?branch=master)](https://travis-ci.org/CartoDB/mobile-android-samples)

## IMPORTANT - Get started
  1. After downloading/cloning project you need to get SDK itself
  1. Get SDK package latest dev build: [sdk4-android-snapshot-latest.zip](https://nutifront.s3.amazonaws.com/sdk_snapshots/sdk4-android-snapshot-latest.zip)
  1. Unzip it and copy *carto-mobile-sdk.aar* file to app/libs/ folder.
  1. Open project in Android Studio, wait for some time (update everything) and then try to run on your USB-connected phone

## SDK documentation
  * See **javadoc** folder in SDK download zip file

## Use in your own app
  * You need license code based on your app ID from carto.com account, see **API Keys > Mobile Apps** section in user profile. 
  * Enabling Mobile Apps currently requires feature flag definition by superadmin!


## Samples
### com.carto.advancedmap3
The project contains several samples demonstrating various APIs of the SDK

 1. Basic pin map, same as hellomap3d
 1. Vector overlay with lines, points, polygons, markers, texts, balloons
 1. Map event listener - show balloon for clicks on vector elements or map. Separate single, long, double and dual click events
 1. 3D overlays: NML model, 3D city and 3D polygon
 1. Show offline vector map, global general map is bundled with app
 1. MBTiles offline map (vector or raster)
 1. Animated raster overlay, looping sample with online weather
 1. Static raster overlay - hillshading
 2. Aerial map (from Bing satellite)
 3. Custom raster datasource
 4. Custom popup balloon
 1. Offline routing using Graphhopper library. First select a graphhopper .map file from SD card. It requires 0.3 version graphhopper data, get sample package for [Ontario, Canada](https://dl.dropboxusercontent.com/u/3573333/mapdata/graphhopper-0.3/canada-ontario-gh3.zip). Click 2 points on map: route start and end, and it shows route and instructions.
 2. PackageManager for offline package downloads
 3. Basic WMS datasource


![screenshot](http://share.gifyoutube.com/yan3Ll.gif)
