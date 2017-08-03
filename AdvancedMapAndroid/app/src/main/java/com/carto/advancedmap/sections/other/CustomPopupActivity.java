package com.carto.advancedmap.sections.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.carto.advancedmap.MapApplication;
import com.carto.advancedmap.sections.basemap.views.BaseMapsView;
import com.carto.advancedmap.shared.activities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.R;
import com.carto.core.MapPos;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.VectorLayer;
import com.carto.styles.MarkerStyle;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.styles.PopupStyle;
import com.carto.styles.PopupStyleBuilder;
import com.carto.ui.PopupClickInfo;
import com.carto.ui.PopupDrawInfo;
import com.carto.utils.BitmapUtils;
import com.carto.vectorelements.CustomPopup;
import com.carto.vectorelements.CustomPopupHandler;
import com.carto.vectorelements.Marker;

@ActivityData(name = "Custom Popup", description = "Create custom popups")
public class CustomPopupActivity extends MapBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MapBaseActivity creates and configures mapView
        super.onCreate(savedInstanceState);

        // Add default base layer
        addBaseLayer(BaseMapsView.DEFAULT_STYLE);

        // Initialize a local vector data source
        LocalVectorDataSource vectorDataSource1 = new LocalVectorDataSource(baseProjection);

        // Initialize a vector layer with the previous data source
        VectorLayer vectorLayer1 = new VectorLayer(vectorDataSource1);

        // Add the previous vector layer to the map
        mapView.getLayers().add(vectorLayer1);
        
        // Create marker style
        Bitmap androidMarkerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        com.carto.graphics.Bitmap markerBitmap = BitmapUtils.createBitmapFromAndroidBitmap(androidMarkerBitmap);
        
        MarkerStyleBuilder markerStyleBuilder = new MarkerStyleBuilder();
        markerStyleBuilder.setBitmap(markerBitmap);
        markerStyleBuilder.setSize(30);
        MarkerStyle markerStyle = markerStyleBuilder.buildStyle();
        
        // Add marker
        MapPos markerPos = mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(13.38933, 52.51704)); // Berlin
        Marker marker1 = new Marker(markerPos, markerStyle);
        vectorDataSource1.add(marker1);
        
        // Add popup
    	PopupStyleBuilder popupStyleBuilder = new PopupStyleBuilder();
    	popupStyleBuilder.setAttachAnchorPoint(0.5f, 0);
    	PopupStyle popupStyle = popupStyleBuilder.buildStyle();

    	MyCustomPopupHandler popupHandler = new MyCustomPopupHandler("custom popup");

        CustomPopup popup1 = new CustomPopup(marker1, popupStyle, popupHandler);
        popup1.setAnchorPoint(-1, 0);
        vectorDataSource1.add(popup1);
        
        // Animate map to the marker
        mapView.setFocusPos(markerPos, 1);
        mapView.setZoom(12, 1);
    }

    /**
     * Simple text-based popup.
     */
    private class MyCustomPopupHandler extends CustomPopupHandler {

        private static final int SCREEN_PADDING = 10;
        private static final int POPUP_PADDING = 10;
        private static final int FONT_SIZE = 15;
        private static final int STROKE_WIDTH = 2;
        private static final int TRIANGLE_SIZE = 10;
        private static final int TEXT_COLOR = android.graphics.Color.BLACK;
        private static final int STROKE_COLOR = android.graphics.Color.BLACK;
        private static final int BACKGROUND_COLOR = android.graphics.Color.WHITE;

        private String text;

        MyCustomPopupHandler(String text) {
            this.text = text;
        }

        public synchronized String getText() {
            return text;
        }

        public synchronized void setText(String text) {
            this.text = text;
        }

        @Override
        public com.carto.graphics.Bitmap onDrawPopup(PopupDrawInfo drawInfo) {
            PopupStyle style = drawInfo.getPopup().getStyle();

            // Calculate scaled dimensions
            float dpToPX = drawInfo.getDPToPX();
            float pxToDP = 1 / dpToPX;

            if (style.isScaleWithDPI()) {
                dpToPX = 1;
            } else {
                pxToDP = 1;
            }

            float screenWidth = drawInfo.getScreenBounds().getWidth() * pxToDP;
            float screenHeight = drawInfo.getScreenBounds().getHeight() * pxToDP;

            int fontSize = (int) (FONT_SIZE * dpToPX);

            int triangleWidth = (int) (TRIANGLE_SIZE * dpToPX);
            int triangleHeight = (int) (TRIANGLE_SIZE * dpToPX);

            int strokeWidth = (int) (STROKE_WIDTH * dpToPX);
            int screenPadding = (int) (SCREEN_PADDING * dpToPX);

            // Get fonts
            Typeface font = Typeface.create("HelveticaNeue-Light", Typeface.NORMAL);

            // Calculate the maximum popup size, adjust with dpi
            int maxPopupWidth = (int) Math.min(screenWidth, screenHeight);

            float halfStrokeWidth = strokeWidth * 0.5f;
            int maxTextWidth = maxPopupWidth - screenPadding * 2 - strokeWidth;

            // Create paint object
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            // Measure text size
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(TEXT_COLOR);
            textPaint.setTextSize(fontSize);
            textPaint.setTypeface(font);
            android.graphics.Point textSize = new  android.graphics.Point(0, 0);
            StaticLayout textLayout = null;

            if (text != "") {
                textLayout = new StaticLayout(text, textPaint, maxTextWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                textSize.set((int) Math.min(textPaint.measureText(text), textLayout.getWidth()), textLayout.getHeight());
            }

            // Calculate bitmap size and create graphics context
            int popupWidth = textSize.x;
            popupWidth += 2 * POPUP_PADDING + strokeWidth + triangleWidth;
            int popupHeight = textSize.y;
            popupHeight += 2 * POPUP_PADDING + strokeWidth;

            // Create bitmap and canvas
            android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(
                    popupWidth, popupHeight, android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            // Prepare triangle path
            Path trianglePath = new Path();
            trianglePath.moveTo(triangleWidth, 0);
            trianglePath.lineTo(halfStrokeWidth, triangleHeight * 0.5f);
            trianglePath.lineTo(triangleWidth, triangleHeight);
            trianglePath.close();

            // Calculate anchor point and triangle position
            int triangleOffsetX = 0;
            int triangleOffsetY = (popupHeight - triangleHeight) / 2;

            // Stroke background
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(STROKE_COLOR);
            RectF backgroundRect = new RectF(triangleWidth, halfStrokeWidth, popupWidth - strokeWidth, popupHeight - strokeWidth);
            canvas.drawRect(backgroundRect, paint);

            // Stroke triangle
            canvas.save();
            canvas.translate(triangleOffsetX, triangleOffsetY);
            canvas.drawPath(trianglePath, paint);
            canvas.restore();

            // Fill background
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(BACKGROUND_COLOR);
            canvas.drawRect(backgroundRect, paint);

            // Fill triangle
            canvas.save();
            canvas.translate(triangleOffsetX, triangleOffsetY);
            canvas.drawPath(trianglePath, paint);
            canvas.restore();

            if (textLayout != null) {
                // Draw text
                canvas.save();
                canvas.translate(halfStrokeWidth + triangleWidth + POPUP_PADDING, halfStrokeWidth + POPUP_PADDING);
                textLayout.draw(canvas);
                canvas.restore();
            }

            return BitmapUtils.createBitmapFromAndroidBitmap(bitmap);
        }

        @Override
        public boolean onPopupClicked(PopupClickInfo clickInfo) {
            Log.d(MapApplication.LOG_TAG, "Popup clicked: " + clickInfo.getElementClickPos());
            return true;
        }
    }
}
