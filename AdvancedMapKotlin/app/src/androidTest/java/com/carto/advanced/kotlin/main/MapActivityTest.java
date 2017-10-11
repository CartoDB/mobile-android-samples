package com.carto.advanced.kotlin.main;

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

import com.carto.advanced.kotlin.sections.styles.StyleChoiceActivity;
import com.carto.core.MapPos;
import com.carto.core.ScreenPos;
import com.carto.graphics.Bitmap;
import com.carto.graphics.ViewState;
import com.carto.projections.Projection;
import com.carto.renderers.RendererCaptureListener;
import com.carto.ui.ClickType;
import com.carto.ui.MapView;
import com.carto.utils.BitmapUtils;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MapActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> splashRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void mapActivityTest() {

        // Activity holder:
        // We open several activities, instances are temporarily stored here
        final Activity[] activities = new Activity[1];

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

        String path = Screenshot.INSTANCE.getDirectory() + Screenshot.INSTANCE.getFOLDER();
        ((MainActivity)activities[0]).mkFolder(path);
        new PermissionGranter().allowPermissionsIfNeeded();

        // Screenshot holder:
        // Several screenshots are taken and temporarily stored in this array
        final android.graphics.Bitmap[] screenshots = new android.graphics.Bitmap[1];

        ViewInteraction textView = onView(withText("BASEMAP STYLES"));
        textView.perform(click());

        stallFor(500);

        ViewInteraction popupButton = onView(withContentDescription("basemap_button"));
        popupButton.perform(click());

        stallFor(300);

        ViewInteraction positronStyle = onView(withContentDescription("style_positron"));
        positronStyle.perform(click());

        stallFor(500);

        /**
         * I actually wanted to create bitchin' zoom animation,
         * but it turns out multi-touch gestures aren't really supported in Espresso
         * and I would've had to rewrite most of it's touch event logic.
         * e.g: https://android.googlesource.com/platform/frameworks/testing/+/
         * android-support-test/espresso/core/src/main/java/android/support/test/espresso/action/Swipe.java
         *
         * So in the end I decided that animated zoom does exactly what I needed to achieve,
         * but with a much simpler implementation
         */
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                StyleChoiceActivity activity = (StyleChoiceActivity) getCurrentActivity();
                activities[0] = activity;
                MapView map = activity.getMapView();

                Projection projection = map.getOptions().getBaseProjection();

                MapPos washingtonDC = projection.fromWgs84(new MapPos(-77.0369, 38.9072));
                map.setFocusPos(washingtonDC, 1.0f);
                map.setZoom(8.0f, 1.0f);
            }
        });

        stallFor(1500);

        ViewInteraction mapView = onView(withContentDescription("map_view"));

        ((StyleChoiceActivity)activities[0]).getMapView().getMapRenderer()
                .captureRendering(new RendererCaptureListener() {

                    @Override
                    public void onMapRendered(Bitmap bitmap) {
                        super.onMapRendered(bitmap);
                        screenshots[0] = BitmapUtils.createAndroidBitmapFromBitmap(bitmap);
                        ((StyleChoiceActivity)activities[0]).getMapView().getMapRenderer().setMapRendererListener(null);
                    }
                }, true);

        stallFor(1500);

        if (screenshots[0] == null) {
            stallFor(1500);
        }

        Screenshot.INSTANCE.take(activities[0], screenshots[0]);
        screenshots[0] = null;

        mapView.perform(click());

        ((StyleChoiceActivity)activities[0]).getMapView().getMapRenderer()
                .captureRendering(new RendererCaptureListener() {

            @Override
            public void onMapRendered(Bitmap bitmap) {
                super.onMapRendered(bitmap);
                screenshots[0] = BitmapUtils.createAndroidBitmapFromBitmap(bitmap);
                ((StyleChoiceActivity)activities[0]).getMapView().getMapRenderer().setMapRendererListener(null);
            }
        }, true);

        stallFor(1500);

        if (screenshots[0] == null) {
            stallFor(1500);
        }

        Screenshot.INSTANCE.take(activities[0], screenshots[0]);
        screenshots[0] = null;

        mapView.perform(swipe());

        stallFor(500);

        ((StyleChoiceActivity)activities[0]).getMapView().getMapRenderer()
                .captureRendering(new RendererCaptureListener() {

                    @Override
                    public void onMapRendered(Bitmap bitmap) {
                        super.onMapRendered(bitmap);
                        screenshots[0] = BitmapUtils.createAndroidBitmapFromBitmap(bitmap);
                        ((StyleChoiceActivity)activities[0]).getMapView().getMapRenderer().setMapRendererListener(null);
                    }
                }, true);

        stallFor(1500);

        if (screenshots[0] == null) {
            stallFor(1500);
        }

        Screenshot.INSTANCE.take(activities[0], screenshots[0]);
    }

    public static void stallFor(final long duration) {
        onView(isRoot()).perform(waitFor(duration));
    }

    private static ViewAction swipe() {

        CoordinatesProvider start = GeneralLocation.TOP_CENTER;
        CoordinatesProvider end = GeneralLocation.BOTTOM_CENTER;
        return new GeneralSwipeAction(Swipe.SLOW, start, end, Press.FINGER);
    }

    /**
     * Perform action of waiting for a specific time.
     */
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
