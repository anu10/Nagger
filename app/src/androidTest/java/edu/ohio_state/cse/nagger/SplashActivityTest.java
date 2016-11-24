package edu.ohio_state.cse.nagger;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.common.SignInButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityRule = new ActivityTestRule(SplashActivity.class);

    private DatabaseHelper mDatabaseHelper;
    private SplashActivity splashActivity;

    @Before
    public void setUp() throws Exception {
        splashActivity = mActivityRule.getActivity();
        mDatabaseHelper = new DatabaseHelper(splashActivity.getApplicationContext());
        mDatabaseHelper.deleteUser(null);
        Reminder reminder = new Reminder("Sayam", "Meet me tmorrow", "2016/11/22", "08:00:00");
        mDatabaseHelper.insertReminder(reminder);
        mDatabaseHelper.insertUser("test", "test");


    }

    @Test
    public void testHandleSignInResult() throws Exception {
        Thread.sleep(2000);
        onView(withId(R.id.reminder_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_RIGHT,
                        GeneralLocation.CENTER_LEFT, Press.FINGER)));
        Thread.sleep(2000);
    }

}

