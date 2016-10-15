package edu.ohio_state.cse.nagger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class SplashActivity extends AppCompatActivity {

    private int mSleepTime = 5000;
    private int mElapsedTime = 0;
    private int mTimeStep = 100;
    private boolean isTouched = false;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }
            }
        };
        splashThread.start();
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
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"Inside Method OnDestroy");
    }
}
