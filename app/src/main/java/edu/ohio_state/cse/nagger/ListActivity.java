package edu.ohio_state.cse.nagger;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Button mCreateReminder;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final FragmentManager fm = getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentById(R.id.list_fragment);

//        if(fragment == null) {
            ListFragment listFragment = new ListFragment();
            fm.beginTransaction().add(R.id.list_fragment, listFragment)
                    .commit();
//        }
        mCreateReminder = (Button) findViewById(R.id.button_create_rem);
        mCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ListActivity.this, CreateReminderActivity.class));
                    }
                });
            }
        });
    }


}
