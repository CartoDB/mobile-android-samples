package com.carto.advancedmap.test;


import android.app.Activity;
import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.carto.advancedmap.list.ActivityData;
import com.carto.advancedmap.list.LauncherListActivity;
import com.carto.advancedmap.test.Utils.PermissionGranter;
import com.carto.advancedmap.test.Utils.Screenshot;

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

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                list = (LauncherListActivity) getCurrentActivity();
                list.unlockScreen();
            }
        });

        list.mkFolder(Screenshot.getDirectory() + Screenshot.DEVICE_FARM_ESPRESSO_SCREEN_DIRECTORY);
        new PermissionGranter().allowPermissionsIfNeeded();

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

        for (Integer j = 0; j < maps.size(); j++) {

            try {
                interaction.atPosition(j).perform(click());
                new PermissionGranter().allowPermissionsIfNeeded();
                Thread.sleep(2000);
                getInstrumentation().runOnMainSync(new Runnable() {
                    @Override
                    public void run() {
                        Activity current = getCurrentActivity();
                        Screenshot.take(current);
                    }

                });

                pressBack();

            } catch (Exception e) {
                Log.d("TEST: ", e.getLocalizedMessage());
                Log.d("COUNTER: ", j.toString());
            }
        }

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
