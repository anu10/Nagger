package edu.ohio_state.cse.nagger;

/**
 * Created by Harsh Gupta on 11/27/2016.
 */


        import edu.ohio_state.cse.nagger.LoginActivity;
        import android.annotation.SuppressLint;
        import android.app.Instrumentation;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.test.espresso.action.GeneralLocation;
        import android.support.test.espresso.action.GeneralSwipeAction;
        import android.support.test.espresso.action.Press;
        import android.support.test.espresso.action.Swipe;
        import android.support.test.espresso.action.ViewActions;
        import android.support.test.espresso.contrib.RecyclerViewActions;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.test.ActivityInstrumentationTestCase2;
        import android.test.UiThreadTest;
        import android.util.Log;

        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
        import com.google.android.gms.common.SignInButton;

        import static android.support.test.espresso.Espresso.onView;
        import static android.support.test.espresso.assertion.ViewAssertions.matches;
        import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
        import static android.support.test.espresso.matcher.ViewMatchers.withId;

        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.mockito.Mock;
        //import org.mockito.cglib.proxy.CallbackGenerator;
        import org.mockito.runners.MockitoJUnitRunner;

        import java.io.File;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityRule = new ActivityTestRule(SplashActivity.class);

    private DatabaseHelper mDatabaseHelper;
    private SplashActivity splashActivity;

   // private LoginActivity loginActivity;
   // private SignInButton mButtonSignIn;
    //private GoogleTransactions mGoogleTransactions;
   // private User mUser;
   // private DatabaseHelper databaseHelper;

    @Before
    public void setUp() throws Exception {

        //DatabaseHelper databaseHelper = new DatabaseHelper(getInstrumentation().getTargetContext());
        splashActivity = mActivityRule.getActivity();
        mDatabaseHelper = new DatabaseHelper(splashActivity.getApplicationContext());
        mDatabaseHelper.deleteUser(null);
        mDatabaseHelper.insertUser("test", "test");
       // mButtonSignIn = (SignInButton) loginActivity.findViewById(R.id.button_sign_in);


    }

    @Test
    public void testHandleSignInResult() throws Exception {
        Thread.sleep(2000);
        try {
            onView(withId(R.id.button_sign_in)).check(
                    matches(isDisplayed()));
        }
        catch(Exception e)
        {
            Log.e("Error aaya","error");
        }
       // onView(withId(R.id.button_sign_in)).perform(ViewActions.click());
              //  .perform(RecyclerViewActions.actionOnItemAtPosition(0, new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_RIGHT,
                //        GeneralLocation.CENTER_LEFT, Press.FINGER)));
        //Thread.sleep(2000);
    }




    /*

    public void testPreconditions() {
        assertNotNull(loginActivity);
        assertNotNull(mUser);
    }



    @Test
    public void testHandleSignInResult() throws Exception {
        mButtonSignIn.performClick();
    }

    protected void tearDown() throws Exception {
        loginActivity.finish();
    }

    */




}

