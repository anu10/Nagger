package edu.ohio_state.cse.nagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.database.Cursor;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private int mSleepTime = 5000;
    private int mElapsedTime = 0;
    private int mTimeStep = 100;
    private boolean isTouched = false;
    private final String TAG = getClass().getSimpleName();
    private ImageView mImageView;
    private DatabaseHelper databaseHelper;
    private GoogleTransactions mGoogleTransactions;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startIntentService();
        DatabaseHelper.setTableName(DatabaseHelper.All_TABLE);
        databaseHelper = new DatabaseHelper(this);
        DatabaseHelper.setTableName(DatabaseHelper.USER_TABLE);
        mGoogleTransactions = new GoogleTransactions(this);
        setContentView(R.layout.activity_splash);
        mImageView = (ImageView) findViewById(R.id.image_view);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_splash);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mImageView != null)
                    mImageView.setAnimation(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.setAnimation(animation);
        Log.d(TAG, "Inside Method OnCreate");

        boolean noSplash = false;
        this.getIntent().getBooleanExtra("NoSplash",noSplash);
        if(!noSplash) {
            Thread splashThread = new Thread() {
                public void run() {
                    try {
                        while (!isTouched && mElapsedTime < mSleepTime) {
                            sleep(mTimeStep);
                            mElapsedTime = mElapsedTime + mTimeStep;
                        }
                    } catch (InterruptedException e) {

                    } finally {
                        finish();
                        launchActivity();
                    }
                }
            };
            splashThread.start();
        }
        else
            launchActivity();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void launchActivity(){
        Cursor cur = databaseHelper.selectAll(DatabaseHelper.USER_TABLE);
        if (cur.getCount() <= 0) {
            SplashActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            });
        } else {
            cur.moveToFirst();
            UserManager userManager = new UserManager();

            User mUser = userManager.createUser(cur.getString(1), cur.getString(0));
            SplashActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, ListActivity.class));
                }
            });
        }
    }

    private void startIntentService() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult conncectionResult) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isTouched = true;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Log.d(TAG, "Inside Method OnStart");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.ohio_state.cse.nagger/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Inside Method OnResume");
        mImageView.setImageResource(R.drawable.nagger);

    }

    @Override
    public void onPause() {
        super.onPause();
        mImageView.setImageDrawable(null);
        Log.d(TAG, "Inside Method OnPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://edu.ohio_state.cse.nagger/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        Log.d(TAG, "Inside Method OnStop");
        mImageView.setImageDrawable(null);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Inside Method OnDestroy");
        mImageView = null;
    }
}