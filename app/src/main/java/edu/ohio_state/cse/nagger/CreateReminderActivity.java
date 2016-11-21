package edu.ohio_state.cse.nagger;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class CreateReminderActivity extends FragmentActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_reminder);


        final FragmentManager fm = getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentById(R.id.reminder_fragment);

        if (fragment == null){
            CreateReminderFragment createReminderFragment = new CreateReminderFragment();
            fm.beginTransaction().add(R.id.reminder_fragment,createReminderFragment).commit();
        }
    }
}
