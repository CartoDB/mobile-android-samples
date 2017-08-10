package com.carto.advanced.kotlin.sections.groundoverlay

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.model.Texts
import com.carto.advanced.kotlin.sections.base.views.MapBaseView
import com.carto.advanced.kotlin.sections.base.utils.toCartoColor
import com.carto.advanced.kotlin.utils.Colors
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.datasources.BitmapOverlayRasterTileDataSource
import com.carto.layers.CartoBaseMapStyle
import com.carto.layers.CartoOnlineVectorTileLayer
import com.carto.layers.RasterTileLayer
import com.carto.core.ScreenPos
import com.carto.core.ScreenPosVector
import com.carto.utils.BitmapUtils
import com.carto.layers.TileSubstitutionPolicy

/**
 * Created by mark on 10/08/2017.
 */
class GroundOverlayView(context: Context) : MapBaseView(context) {

    var baseLayer: CartoOnlineVectorTileLayer? = null
    var overlaySource: BitmapOverlayRasterTileDataSource? = null
    var overlayLayer: RasterTileLayer? = null

    init {

        title = Texts.groundOverlayInfoHeader
        description = Texts.groundOverlayInfoContainer

        baseLayer = addBaseLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_POSITRON)

        // Load ground overlay bitmap
        val overlayBitmap = BitmapUtils.loadBitmapFromAssets("jefferson-building-ground-floor.jpg")

        val proj = map.options.baseProjection

        // Create two vectors containing geographical positions and corresponding raster image pixel coordinates.
        // 2, 3 or 4 points may be specified. Usually 2 points are enough (for conformal mapping).
        val pos = proj.fromWgs84(MapPos(-77.004590, 38.888702))
        val sizeNS = 110.0
        val sizeWE = 100.0

        val mapPoses = MapPosVector()
        mapPoses.add(MapPos(pos.getX() - sizeWE, pos.getY() + sizeNS))
        mapPoses.add(MapPos(pos.getX() + sizeWE, pos.getY() + sizeNS))
        mapPoses.add(MapPos(pos.getX() + sizeWE, pos.getY() - sizeNS))
        mapPoses.add(MapPos(pos.getX() - sizeWE, pos.getY() - sizeNS))

        val bitmapPoses = ScreenPosVector()
        bitmapPoses.add(ScreenPos(0f, 0f))
        bitmapPoses.add(ScreenPos(0f, overlayBitmap.height.toFloat()))
        bitmapPoses.add(ScreenPos(overlayBitmap.width.toFloat(), overlayBitmap.height.toFloat()))
        bitmapPoses.add(ScreenPos(overlayBitmap.width.toFloat(), 0f))

        overlaySource = BitmapOverlayRasterTileDataSource(0, 20, overlayBitmap, proj, mapPoses, bitmapPoses)

        overlayLayer = RasterTileLayer(overlaySource)

        // Apply zoom level bias to the raster layer.
        // By default, bitmaps are upsampled on high-DPI screens.
        // We will correct this by applying appropriate bias
        val zoomLevelBias = (Math.log(map.options.dpi / 160.0) / Math.log(2.0)).toFloat()

        overlayLayer!!.zoomLevelBias = zoomLevelBias * 0.75f
        overlayLayer!!.tileSubstitutionPolicy = TileSubstitutionPolicy.TILE_SUBSTITUTION_POLICY_VISIBLE

        map.layers.add(overlayLayer)

        // Set specific focus/zoom for an optimal initial view
        map.setFocusPos(pos, 0.0f)
        map.setZoom(15.5f, 0.0f)

        layoutSubviews()
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
    }
}