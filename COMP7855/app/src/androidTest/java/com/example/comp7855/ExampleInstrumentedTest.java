package com.example.comp7855;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void DateFilterTest() {

        onView(withId(R.id.captionButton)).perform(click());
        onView(withId(R.id.uploadButton)).perform(click());

        onView(withId(R.id.rightButton)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.rightButton)).perform(click());
        }
        onView(withId(R.id.leftButton)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.leftButton)).perform(click());
        }

        onView(withId(R.id.clearButton)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());
        // Test search
        onView(withId(R.id.editFrom)).perform(typeText("20183101050505"), closeSoftKeyboard());
        onView(withId(R.id.editTo)).perform(typeText("20200101050505"), closeSoftKeyboard());
        onView(withId(R.id.buttonGo)).perform(click());


        onView(withId(R.id.rightButton)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.rightButton)).perform(click());
        }
        onView(withId(R.id.leftButton)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.leftButton)).perform(click());
        }
    }

    //        onView(withId(R.id.search_city)).perform(typeText("Surrey"), closeSoftKeyboard());
}

