package com.carto.cartomap.android.datasource;

import android.util.Log;

import com.carto.core.MapEnvelope;
import com.carto.core.MapPos;
import com.carto.core.Variant;
import com.carto.datasources.VectorDataSource;
import com.carto.datasources.components.VectorData;
import com.carto.geometry.GeoJSONGeometryReader;
import com.carto.geometry.Geometry;
import com.carto.geometry.LineGeometry;
import com.carto.geometry.PointGeometry;
import com.carto.geometry.PolygonGeometry;
import com.carto.projections.Projection;
import com.carto.renderers.components.CullState;
import com.carto.styles.LineStyle;
import com.carto.styles.MarkerStyle;
import com.carto.styles.PointStyle;
import com.carto.styles.PolygonStyle;
import com.carto.styles.Style;
import com.carto.vectorelements.Line;
import com.carto.vectorelements.Marker;
import com.carto.vectorelements.Point;
import com.carto.vectorelements.Polygon;
import com.carto.vectorelements.VectorElement;
import com.carto.vectorelements.VectorElementVector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Locale;

/**
 * A custom vector data source making queries to http://docs.cartodb.com/cartodb-platform/sql-api/
 * 
*/
public class CartoDBSQLDataSource extends VectorDataSource {


    private String baseUrl;
    private String query;
    private Style style;


	public CartoDBSQLDataSource(Projection proj, String baseUrl, String query, Style style) {
		super(proj);
        this.baseUrl = baseUrl;
        this.style = style;
        this.query = query;
	}


    @Override
    public VectorData loadElements(CullState cullState) {
        VectorElementVector elements = new VectorElementVector();

        MapEnvelope mapViewBounds = cullState.getProjectionEnvelope(this.getProjection());
        MapPos min = mapViewBounds.getBounds().getMin();
        MapPos max = mapViewBounds.getBounds().getMax();

        //run query here
        loadData(elements, min,max,cullState.getViewState().getZoom());

        return new VectorData(elements);

    }

    private void loadData(VectorElementVector elements, MapPos min, MapPos max, float zoom) {

        // load and parse JSON
        String base = "ST_SetSRID(ST_MakeEnvelope(%f,%f,%f,%f),3857) && the_geom_webmercator";
        String bbox = String.format(Locale.US, base, min.getX(), min.getY(), max.getX(), max.getY());

        String unencodedQuery = query.replace("!bbox!", bbox);

        Log.d("LOG", "SQL query: " + query);

        unencodedQuery = unencodedQuery.replace("zoom('!scale_denominator!')", String.valueOf(zoom));

        String encodedQuery = null;
        try {
            encodedQuery = URLEncoder.encode(unencodedQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String urlAddress = baseUrl + "?format=GeoJSON&q="+ encodedQuery;

        Log.d("LOG", "SQL API: " + urlAddress);

        try {

            URL url = new URL(urlAddress);

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject json = new JSONObject(responseStrBuilder.toString());

            GeoJSONGeometryReader geoJsonParser = new GeoJSONGeometryReader();

            JSONArray features = json.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = (JSONObject) features.get(i);
                JSONObject geometry = feature.getJSONObject("geometry");

                // use SDK GeoJSON parser
                Geometry ntGeom = geoJsonParser.readGeometry(geometry.toString());

                JSONObject properties = feature.getJSONObject("properties");
                VectorElement element;

                // create object based on given style
                if(style instanceof PointStyle){
                    element = new Point((PointGeometry) ntGeom,(PointStyle) style);
                }else if(style instanceof MarkerStyle){
                    element = new Marker(ntGeom, (MarkerStyle) style);
                }else if(style instanceof LineStyle) {
                    element = new Line((LineGeometry) ntGeom, (LineStyle) style);
                }else if(style instanceof PolygonStyle) {
                    element = new Polygon((PolygonGeometry) ntGeom, (PolygonStyle) style);
                }else{
                    Log.e("LOG", "Object creation not implemented yet for style: " + style.swigGetClassName());
                    break;
                }

                // add all properties as MetaData, so you can use it with click handling
                for (Iterator<?> j = properties.keys(); j.hasNext();){
                    String key = (String)j.next();
                    String val = properties.getString(key);
                    element.setMetaDataElement(key, new Variant(val));
                }

                elements.add(element);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
