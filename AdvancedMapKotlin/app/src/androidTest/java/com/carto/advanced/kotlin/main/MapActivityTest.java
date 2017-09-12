package com.carto.advanced.kotlin.main;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
        ViewInteraction textView = onView(withText("STYLES"));
        textView.perform(click());

        stallFor(500);

        ViewInteraction popupButton = onView(withContentDescription("basemap_button"));
        popupButton.perform(click());

        stallFor(300);

        ViewInteraction stylePopupContentSectionItem = onView(withContentDescription("style_positron"));
        stylePopupContentSectionItem.perform(click());

        stallFor(300);

        ViewInteraction mapView = onView(withContentDescription("map_view"));
        mapView.perform(swipe());

        stallFor(500);
    }

    public static void stall() {
        final long TWO_SECONDS = 2000;
        onView(isRoot()).perform(waitFor(TWO_SECONDS));
    }

    public static void stallFor(final long duration) {
        onView(isRoot()).perform(waitFor(duration));
    }

    private static ViewAction swipe() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_CENTER,
                GeneralLocation.BOTTOM_CENTER, Press.FINGER);
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
}
