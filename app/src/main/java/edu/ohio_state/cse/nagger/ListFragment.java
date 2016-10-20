package edu.ohio_state.cse.nagger;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class ListFragment extends Fragment {

    private GoogleTransactions mGoogleTransactions;
    private User mUser;
    private RecyclerView mCrimeRecyclerView;
    private ReminderAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleTransactions = GoogleTransactions.getGoogleTransaction();
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
        setHasOptionsMenu(true);

        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.reminder_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.list_menu,menu);
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

    @Override
    public void onStop() {
        this.mGoogleTransactions = null;
        super.onStop();
    }

    @Override
    public void onResume() {
        mGoogleTransactions = GoogleTransactions.getGoogleTransaction();
        super.onResume();
    }

    private class ReminderHolder extends RecyclerView.ViewHolder {

        private Reminder mReminder;
        private TextView mReminderTitle;
        private TextView mReminderFrom;
        private TextView mReminderDate;
        private TextView mReminderTime;

        public ReminderHolder(View itemView){
            super(itemView);
            mReminderTitle = (TextView) itemView.findViewById(R.id.reminder_title);
            mReminderFrom = (TextView) itemView.findViewById(R.id.reminder_from);
            mReminderDate = (TextView) itemView.findViewById(R.id.reminder_date);
            mReminderTime = (TextView) itemView.findViewById(R.id.reminder_time);
        }

        public void bindReminder(Reminder reminder){
            mReminder = reminder;
            mReminderTitle.setText(mReminder.getReminderTitle());
            mReminderFrom.setText(mReminder.getReminderDesc());
            mReminderDate.setText(mReminder.getDate().toString());
//            mReminderDate.setText(mReminder.getTime().toString());
        }
    }

    private class ReminderAdapter extends RecyclerView.Adapter<ReminderHolder>{

        private List<Reminder> mReminderList;

        @Override
        public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.activity_single_reminder,parent,false);
            return new ReminderHolder(view);
        }

        @Override
        public void onBindViewHolder(ReminderHolder holder, int position) {
            Reminder reminder = mReminderList.get(position);
            holder.bindReminder(reminder);
        }

        @Override
        public int getItemCount(){
            return mReminderList.size();
        }

        public ReminderAdapter(List<Reminder> reminderList){
            mReminderList = reminderList;
        }
    }

    public void updateUI(){
        ReminderList reminderList = ReminderList.get(getActivity());
        List<Reminder> reminders = reminderList.getReminders();

        mAdapter = new ReminderAdapter(reminders);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }
}
