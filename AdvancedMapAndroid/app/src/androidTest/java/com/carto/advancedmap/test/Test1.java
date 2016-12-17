package com.carto.advancedmap.test;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.carto.advancedmap.shared.activities.MapBaseActivity;
import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.list.LauncherListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test1 {

    @Rule
    public ActivityTestRule<LauncherListActivity> mActivityTestRule = new ActivityTestRule<>(LauncherListActivity.class);

    private static LauncherListActivity list;

    @Test
    public void test1() {

        setActivity();

        ArrayList<Class> maps = new ArrayList<>();

        for (Integer i = 0; i < list.samples.length; i++) {

            Class sample = list.samples[i];

            java.lang.annotation.Annotation[] annotations = sample.getAnnotations();
            String description = ((ActivityData) annotations[0]).description();

            if (!description.equals("")) {
                // Headers have no description
                maps.add(sample);
            }
        }

        DataInteraction interaction = onData(allOf(is(instanceOf(LauncherListActivity.MapListMap.class))));

        for (Integer i = 0; i < maps.size(); i++) {

            try {
                interaction.atPosition(i).perform(click());
                new PermissionGranter().allowPermissionsIfNeeded();
                MapBaseActivity.takeScreenshot("screenshot-001", list);

                pressBack();
            } catch (Exception e) {
                Log.d("TEST: ", e.getLocalizedMessage());
                Log.d("COUNTER: ", i.toString());
            }
        }
    }

    static void setActivity() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (activities.size() > 0) {
                    list = (LauncherListActivity)activities.toArray()[0];
                    list.unlockScreen();
                }
            }
        });
    }

    public class PermissionGranter {

        private static final int PERMISSIONS_DIALOG_DELAY = 3000;
        private static final int GRANT_BUTTON_INDEX = 1;

        public void allowPermissionsIfNeeded() {
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    sleep(PERMISSIONS_DIALOG_DELAY);
                    UiDevice device = UiDevice.getInstance(getInstrumentation());
                    UiObject allowPermissions = device.findObject(new UiSelector()
                            .clickable(true)
                            .checkable(false)
                            .index(GRANT_BUTTON_INDEX));

                    if (allowPermissions.exists()) {
                        allowPermissions.click();
                    }
                }

            } catch (UiObjectNotFoundException e) {
                System.out.println("There is no permissions dialog to interact with");
            }
        }

        private boolean hasNeededPermission(String permissionNeeded) {
            Context context = InstrumentationRegistry.getTargetContext();
            int permissionStatus = ContextCompat.checkSelfPermission(context, permissionNeeded);
            return permissionStatus == PackageManager.PERMISSION_GRANTED;
        }

        private void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException("Cannot execute Thread.sleep()");
            }
        }
    }
}
