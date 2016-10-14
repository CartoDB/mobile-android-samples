package com.carto.advancedmap.test;


import android.app.Activity;
import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.suitebuilder.annotation.LargeTest;

import com.carto.advancedmap.list.MapListItem;
import com.carto.advancedmap.list.LauncherListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test1 {

    @Rule
    public ActivityTestRule<LauncherListActivity> mActivityTestRule = new ActivityTestRule<>(LauncherListActivity.class);

    static LauncherListActivity list;

    @Test
    public void test1() {

        setActivity();

        for (Integer i = 0; i < list.samples.length; i++) {
            DataInteraction interaction = onData(allOf(is(instanceOf(MapListItem.class))));
            interaction.atPosition(i).perform(click());
            pressBack();
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
}
