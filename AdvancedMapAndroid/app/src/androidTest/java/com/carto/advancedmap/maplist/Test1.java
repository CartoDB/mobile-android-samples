package com.carto.advancedmap.maplist;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test1 {

    @Rule
    public ActivityTestRule<LauncherListActivity> mActivityTestRule = new ActivityTestRule<>(LauncherListActivity.class);

    @Test
    public void test1() {
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.list),
                                withParent(withId(android.R.id.content))),
                        2),
                        isDisplayed()));
        linearLayout.perform(click());

        pressBack();

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.list),
                                withParent(withId(android.R.id.content))),
                        1),
                        isDisplayed()));
        linearLayout2.perform(click());

        pressBack();

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.list),
                                withParent(withId(android.R.id.content))),
                        7),
                        isDisplayed()));
        linearLayout3.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
