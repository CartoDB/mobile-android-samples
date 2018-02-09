package com.carto.advanced.kotlin.main;

import android.Manifest;
import android.app.Activity;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.carto.advanced.kotlin.sections.groundoverlay.GroundOverlayActivity;
import com.carto.advanced.kotlin.sections.offlinerouting.OfflineRoutingActivity;
import com.carto.advanced.kotlin.sections.styles.StyleChoiceActivity;
import com.carto.advanced.kotlin.sections.vectorelement.VectorElementActivity;
import com.carto.core.MapPos;
import com.carto.graphics.Bitmap;
import com.carto.projections.Projection;
import com.carto.renderers.RendererCaptureListener;
import com.carto.ui.MapView;
import com.carto.utils.BitmapUtils;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MapActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> splashRule = new ActivityTestRule<>(SplashActivity.class);

    // Activity holder:
    // We open several activities, instances are temporarily stored here
    static final Activity[] activities = new Activity[1];

    // Screenshot holder:
    // Several screenshots are taken and temporarily stored in this array
    static final android.graphics.Bitmap[] screenshots = new android.graphics.Bitmap[1];

    @Test
    public void mapActivityTest() {
        /**
         * Android 6.0 requires runtime permission for write access as well, god damnit.
         * and folder creation is a context-based operation, so it'll need to be in the activity.
         * ... and since we're saving screenshots, just do it at the initial step
         */
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                MainActivity activity = (MainActivity) getCurrentActivity();
                activities[0] = activity;
            }
        });

        grantWritePermission();

        ViewInteraction galleryRow = onView(withText("BASEMAP STYLES"));
        galleryRow.perform(click());

        stall(500);

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                StyleChoiceActivity activity = (StyleChoiceActivity) getCurrentActivity();
                activities[0] = activity;
            }
        });

        waitForStyleChoiceScreenshot(true);

        ViewInteraction popupButton = onView(withContentDescription("basemap_button"));
        popupButton.perform(click());

        stall(300);

        ViewInteraction positronStyle = onView(withContentDescription("style_positron"));
        positronStyle.perform(click());

        stall(500);

        /**
         * I actually wanted to create bitchin' zoom animation,
         * but it turns out multi-touch gestures aren't really supported in Espresso
         * and I would've had to rewrite most of its touch event logic.

         * So in the end I decided that animated zoom does exactly what I needed to achieve,
         * but with a much simpler implementation
         */
        MapView map = ((StyleChoiceActivity)activities[0]).getMapView();
        Projection projection = map.getOptions().getBaseProjection();
        MapPos washingtonDC = projection.fromWgs84(new MapPos(-77.0369, 38.9072));
        map.setFocusPos(washingtonDC, 1.0f);
        map.setZoom(8.0f, 1.0f);

        stall(1500);

        ViewInteraction mapView = onView(withContentDescription("map_view"));

        waitForStyleChoiceScreenshot(true);

        mapView.perform(click());

        waitForStyleChoiceScreenshot(true);

        mapView.perform(swipe());

        waitForStyleChoiceScreenshot(false);

        pressBack();

        galleryRow = onView(withText("GROUND OVERLAY"));
        galleryRow.perform(click());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                GroundOverlayActivity activity = (GroundOverlayActivity) getCurrentActivity();
                activities[0] = activity;
            }
        });

        stall(1000);
        map = ((GroundOverlayActivity)activities[0]).getContentView().getMap();
        waitForScreenshot(map, false);

        pressBack();

        galleryRow = onView(withText("VECTOR ELEMENTS"));
        galleryRow.perform(click());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                VectorElementActivity activity = (VectorElementActivity) getCurrentActivity();
                activities[0] = activity;
            }
        });

        stall(1000);
        map = ((VectorElementActivity)activities[0]).getContentView().getMap();
        waitForScreenshot(map, false);

        pressBack();

        galleryRow = onView(withText("OFFLINE ROUTING"));
        galleryRow.perform(click());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                OfflineRoutingActivity activity = (OfflineRoutingActivity) getCurrentActivity();
                activities[0] = activity;
            }
        });

        stall(1000);

        map = ((OfflineRoutingActivity)activities[0]).getContentView().getMap();
        map.setFocusPos(washingtonDC, 0.0f);
        map.setZoom(10.0f, 0.0f);

        mapView = onView(withContentDescription("map_view"));

        mapView.perform(longClick());

        stall(1000);

        // Since it's difficult to emulate clicks in different positions,
        // simply move the map slightly and long click again to initialize route calculation
        MapPos position = projection.fromWgs84(new MapPos(-77.0259, 38.9282));
        map.setFocusPos(position, 0.0f);

        mapView.perform(longClick());

        stall(1000);
        waitForScreenshot(map, false);
    }

    public static void waitForStyleChoiceScreenshot(boolean waitForRendering) {
       final MapView map = ((StyleChoiceActivity)activities[0]).getMapView();
        waitForScreenshot(map, waitForRendering);
    }

    static void waitForScreenshot(final MapView map, boolean waitForRendering) {
        map.getMapRenderer().captureRendering(new RendererCaptureListener() {

                    @Override
                    public void onMapRendered(Bitmap bitmap) {
                        super.onMapRendered(bitmap);
                        screenshots[0] = BitmapUtils.createAndroidBitmapFromBitmap(bitmap);
                        map.getMapRenderer().setMapRendererListener(null);
                    }
                }, waitForRendering);
        stall(1500);

        while (screenshots[0] == null) {
            stall(1000);
        }

        Screenshot.INSTANCE.take(activities[0], screenshots[0]);
        screenshots[0] = null;
    }

    public static void grantWritePermission() {

        while (activities[0] == null) {
            stall(1000);
        }

        String path = Screenshot.INSTANCE.getDirectory() + Screenshot.INSTANCE.getFOLDER();
        ((MainActivity)activities[0]).mkFolder(path);

        PermissionGranter granter = new PermissionGranter();
        if (!granter.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            granter.allowPermissionsIfNeeded();
        }
    }

    private static ViewAction swipe() {

        CoordinatesProvider start = GeneralLocation.TOP_CENTER;
        CoordinatesProvider end = GeneralLocation.BOTTOM_CENTER;
        return new GeneralSwipeAction(Swipe.SLOW, start, end, Press.FINGER);
    }

    public static void stall(final long duration) {
        onView(isRoot()).perform(waitFor(duration));
    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    static Activity getCurrentActivity() {

        ActivityLifecycleMonitor registry = ActivityLifecycleMonitorRegistry.getInstance();
        Collection<Activity> activities = registry.getActivitiesInStage(Stage.RESUMED);

        if (activities.size() > 0) {
            return (Activity) activities.toArray()[0];
        }
        return null;
    }
}
