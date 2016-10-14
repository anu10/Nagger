package edu.ohio_state.cse.nagger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashActivity extends AppCompatActivity {

    private int mSleepTime = 5000;
    private int mElapsedTime = 0;
    private int mTimeStep = 100;
    private boolean isTouched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
}
