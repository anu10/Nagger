package edu.ohio_state.cse.nagger;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.database.Cursor;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private int mSleepTime = 5000;
    private int mElapsedTime = 0;
    private int mTimeStep = 100;
    private boolean isTouched = false;
    private final String TAG = getClass().getSimpleName();
    private ImageView mImageView;
    private DatabaseHelper databaseHelper;
    private GoogleTransactions mGoogleTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper.setTableNAme(DatabaseHelper.USER_TABLE);
        databaseHelper = new DatabaseHelper(this);
        mGoogleTransactions = new GoogleTransactions(this);
        setContentView(R.layout.activity_splash);
        mImageView =(ImageView)findViewById(R.id.image_view);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation_splash);

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
        Log.d(TAG,"Inside Method OnCreate");


        Thread splashThread = new Thread(){
            public void run(){
                try {
                    while (!isTouched && mElapsedTime < mSleepTime) {
                        sleep(mTimeStep);
                        mElapsedTime = mElapsedTime + mTimeStep;
                    }
                }
                catch(InterruptedException e){

                }
                finally {
                    finish();
                    Cursor cur = databaseHelper.selectAll();
                        if (cur.getCount() <= 0) {
                            SplashActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                                }
                            });
                        }
                        else{
                            cur.moveToFirst();
                            UserManager userManager = new UserManager();

                            User mUser = userManager.createUser(cur.getString(1),cur.getString(0));
                            SplashActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashActivity.this,ListActivity.class));
                                }
                            });
                        }
//                    }


                }
            }
        };
        splashThread.start();
    }

    @Override
    public void onConnectionFailed(ConnectionResult conncectionResult){

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            isTouched = true;
        }
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"Inside Method OnStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"Inside Method OnResume");
        mImageView.setImageResource(R.drawable.nagger);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"Inside Method OnPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"Inside Method OnStop");
        mImageView.setImageDrawable(null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"Inside Method OnDestroy");
    }
}