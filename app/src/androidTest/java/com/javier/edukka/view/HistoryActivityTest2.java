package com.javier.edukka.view;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.javier.edukka.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HistoryActivityTest2 {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void historyActivityTest2() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), replaceText("test-1"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.username), withText("test-1"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText2.perform(pressImeActionButton());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText3.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.password), withText("1234"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText4.perform(pressImeActionButton());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.cardView),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        cardView.perform(scrollTo(), click());

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.toolbar_title), withText("test-1"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar_user),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("test-1")));

        ViewInteraction tabView = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tabs),
                                0),
                        2),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView2 = onView(
                allOf(instanceOf(TextView.class),
                        withParent(withId(R.id.toolbar))));
        textView2.check(matches(withText("History")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.list_game), withText("Basic Grammar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_recycler_view),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Basic Grammar")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.list_user), withText("test-4"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_recycler_view),
                                        1),
                                2),
                        isDisplayed()));
        textView4.check(matches(withText("test-4")));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recyclerview),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView5 = onView(
                allOf(instanceOf(TextView.class),
                        withParent(withId(R.id.toolbar))));
        textView5.check(matches(withText("Statistics")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.best), withText("English (2)"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        4),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("English (2)")));

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
