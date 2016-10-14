package edu.ohio_state.cse.nagger;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ListFragment extends Fragment {

    GoogleTransactions mGoogleTransactions;
    User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mGoogleTransactions = new GoogleTransactions();
        mUser = UserManager.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_list_fragment,container,false);
        Button button = (Button) v.findViewById(R.id.button_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        TextView textview = (TextView) v.findViewById(R.id.welcome_message);
        textview.setText("Welcome " + mUser.getUserName() + "!! Right now you don't have any reminders");
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
        menuInflater.inflate(R.menu.list_menu,menu);
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.menu.list_menu:{
                signOut();
                break;
            }
        }
        return true;
    }

    private void signOut(){
        boolean isLoggedOut = mGoogleTransactions.signOut();
        if(isLoggedOut){
            Toast.makeText(this.getContext(),"Log Out Successful",Toast.LENGTH_LONG).show();
        }
    }
}
