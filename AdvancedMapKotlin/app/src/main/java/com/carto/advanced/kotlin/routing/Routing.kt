package com.carto.advanced.kotlin.routing

import android.content.Context
import com.carto.advanced.kotlin.R
import com.carto.advanced.kotlin.utils.Utils
import com.carto.core.MapPos
import com.carto.core.MapPosVector
import com.carto.core.MapRange
import com.carto.datasources.LocalVectorDataSource
import com.carto.graphics.Color
import com.carto.layers.VectorLayer
import com.carto.routing.*
import com.carto.styles.LineStyleBuilder
import com.carto.styles.MarkerStyle
import com.carto.styles.MarkerStyleBuilder
import com.carto.styles.PolygonStyleBuilder
import com.carto.ui.MapView
import com.carto.vectorelements.Line
import com.carto.vectorelements.Marker
import com.carto.vectorelements.Polygon
import org.jetbrains.anko.displayMetrics

/**
 * Convenience class to move routing logic outside of the activity
 *
 * Created by aareundo on 17/07/2017.
 */
class Routing(val context: Context, val mapView: MapView) {

    companion object {
        val ROUTING_TAG = "routing:"
        val OFFLINE_ROUTING_SOURCE = "carto.streets"
    }

    var service: RoutingService? = null

    var startMarker: Marker? = null
    var stopMarker: Marker? = null

    var showTurns: Boolean = true
    var instructionUp: MarkerStyle? = null
    var instructionLeft: MarkerStyle? = null
    var instructionRight: MarkerStyle? = null

    var projection = mapView.options.baseProjection

    var routeDataSource = LocalVectorDataSource(projection)
    var routeStartStopDataSource = LocalVectorDataSource(projection)

    init {

        val start = Utils.resourceToBitmap(context.resources, R.drawable.icon_pin_red)
        val stop = Utils.resourceToBitmap(context.resources, R.drawable.icon_pin_red)

        val up = Utils.resourceToBitmap(context.resources, R.drawable.direction_up)
        val upLeft = Utils.resourceToBitmap(context.resources, R.drawable.direction_upthenleft)
        val upRight = Utils.resourceToBitmap(context.resources, R.drawable.direction_upthenright)

        // Define layer and datasource for route line and instructions
        val routeLayer = VectorLayer(routeDataSource)
        mapView.layers.add(routeLayer)

        // Define layer and datasource for route start and stop markers
        val vectorLayer = VectorLayer(routeStartStopDataSource)
        mapView.layers.add(vectorLayer)

        // Set visible zoom range for the vector layer
        vectorLayer.visibleZoomRange = MapRange(0.0f, 22.0f)

        val builder = MarkerStyleBuilder()
        builder.bitmap = start
        builder.isHideIfOverlapped = false
        builder.size = 5 * context.displayMetrics.density

        val defaultPosition = MapPos(0.0, 0.0)

        startMarker = Marker(defaultPosition, builder.buildStyle())
        startMarker!!.isVisible = false
        routeStartStopDataSource.add(startMarker)

        builder.bitmap = stop
        stopMarker = Marker(defaultPosition, builder.buildStyle())
        stopMarker!!.isVisible = false
        routeStartStopDataSource.add(stopMarker)

        builder.bitmap = up
        instructionUp = builder.buildStyle()

        builder.bitmap = upLeft
        instructionLeft = builder.buildStyle()

        builder.bitmap = upRight
        instructionRight = builder.buildStyle()
    }

    /*
     * Return bounds on complete so we can start downloading the BBOX
     */
    fun show(result: RoutingResult, lineColor: Color, complete: (route: Route) -> Unit) {

        routeDataSource.clear()
        startMarker?.isVisible = true

        val line = createPolyLine(result, lineColor)
        routeDataSource.add(line)

        val instructions = result.instructions
        val vector = MapPosVector()
        val count = instructions.size().toInt()

        for (i in 0..count - 1) {
            val instruction = instructions[i]
            val index = instruction.pointIndex
            val position = result.points[index]

            if (showTurns) {
                createRoutePoint(position, instruction, routeDataSource)
            }
            vector.add(position)
        }

        val polygon = Polygon(vector, PolygonStyleBuilder().buildStyle())
        val route = Route()

        route.bounds = polygon.bounds
        route.length = result.totalDistance

        complete(route)
    }

    fun getMessage(result: RoutingResult): String {
        val km = (result.totalDistance / 10000) * 10
        return "Your route is " + (km * 10).toInt() / 10.0f + "km"
    }

    fun getResult(startPos: MapPos, stopPos: MapPos): RoutingResult? {

        val positions = MapPosVector()
        positions.add(startPos)
        positions.add(stopPos)

        val request = RoutingRequest(projection, positions)

        var result: RoutingResult?

        try {
            result = service?.calculateRoute(request)
        } catch (exception: Exception) {
            result = null
        }

        return result
    }

    fun createRoutePoint(position: MapPos, instruction: RoutingInstruction, source: LocalVectorDataSource) {
        val action = instruction.action

        var style = instructionUp

        if (action == RoutingAction.ROUTING_ACTION_TURN_LEFT) {
            style = instructionLeft
        } else if (action == RoutingAction.ROUTING_ACTION_TURN_RIGHT) {
            style = instructionRight
        }

        val marker = Marker(position, style)
        source.add(marker)
    }

    fun createPolyLine(result: RoutingResult, color: Color): Line {
        val builder = LineStyleBuilder()
        builder.color = color
        builder.width = 7.0f

        return  Line(result.points, builder.buildStyle())
    }

    fun setStartMarker(position: MapPos) {
        routeDataSource.clear()
        stopMarker?.isVisible = false
        startMarker?.setPos(position)
        startMarker?.isVisible = true
    }

    fun setStopMarker(position: MapPos) {
        stopMarker?.setPos(position)
        stopMarker?.isVisible = true
    }
}