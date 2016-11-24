package edu.ohio_state.cse.nagger;

import edu.ohio_state.cse.nagger.LoginActivity;
import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity loginActivity;
    private SignInButton mButtonSignIn;
    private GoogleTransactions mGoogleTransactions;
    private User mUser;
    private DatabaseHelper databaseHelper;

    public LoginActivityTest() {
        super("edu.ohio_state.cse.nagger.LoginActivity",
                LoginActivity.class);
        DatabaseHelper databaseHelper = new DatabaseHelper(getInstrumentation().getTargetContext());
        databaseHelper.insertUser("test", "test");
    }

    public void testPreconditions() {
        assertNotNull(loginActivity);
        assertNotNull(mUser);
    }

    protected void setUp() throws Exception {
        super.setUp();
        loginActivity = getActivity();
//        DatabaseHelper.setTableName(DatabaseHelper.USER_TABLE);
//        databaseHelper = new DatabaseHelper(loginActivity);
//        mGoogleTransactions = GoogleTransactions.getGoogleTransaction();
        mButtonSignIn = (SignInButton) loginActivity.findViewById(R.id.button_sign_in);
        mButtonSignIn.setSize(SignInButton.SIZE_WIDE);

    }

    @Test
    public void testHandleSignInResult() throws Exception {
        mButtonSignIn.performClick();
    }

    protected void tearDown() throws Exception {
        loginActivity.finish();
    }




}

