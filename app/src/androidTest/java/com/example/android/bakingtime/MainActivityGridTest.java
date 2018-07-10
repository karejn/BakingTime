package com.example.android.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;



@RunWith(AndroidJUnit4.class)
public class MainActivityGridTest {
    public static final String HEAD_NAME = "Ingredients";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void scrollGridViewItem() {
        onView(withId(R.id.gridviewRecycler)).perform(RecyclerViewActions.scrollToPosition(3));
    }
    @Test
    public void clickGridViewItem(){
        for(int i=0;i<MainActivity.dishNames.length;i++) {
            Espresso.onView(ViewMatchers.withId(R.id.gridviewRecycler))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.click()));
            onView(withId(R.id.ingred_textView)).check(matches(withText(HEAD_NAME)));
            onView(isRoot()).perform(ViewActions.pressBack());
        }
    }
}
