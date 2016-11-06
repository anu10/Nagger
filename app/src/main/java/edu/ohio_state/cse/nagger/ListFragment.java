package edu.ohio_state.cse.nagger;

import android.*;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class ListFragment extends Fragment {

    private GoogleTransactions mGoogleTransactions;
    private User mUser;
    private RecyclerView mCrimeRecyclerView;
    private ReminderAdapter mAdapter;
    ReminderList reminderList;
    ContentResolver cr;
    ContentValues values;
    private DatabaseHelper mDatabaseHelper;

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
        setHasOptionsMenu(true);

        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.reminder_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabaseHelper = new DatabaseHelper(getContext());

        if(updateUI())
            textview.setText("Welcome " + mUser.getUserName() + "!! You have following reminders");
        else
            textview.setText("Welcome " + mUser.getUserName() + "!! Right now you don't have any reminders");

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

    private class ReminderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Reminder mReminder;
        private TextView mReminderID;
        private TextView mReminderTitle;
        private TextView mReminderFrom;
        private TextView mReminderDate;
        private TextView mReminderTime;
        private ImageButton mAcceptButton;
        private ImageButton mRejectButton;

        public ReminderHolder(View itemView){
            super(itemView);
            mReminderID = (TextView) itemView.findViewById(R.id.reminder_id);
            mReminderTitle = (TextView) itemView.findViewById(R.id.reminder_title);
            mReminderFrom = (TextView) itemView.findViewById(R.id.reminder_from);
            mReminderDate = (TextView) itemView.findViewById(R.id.reminder_date);
            mReminderTime = (TextView) itemView.findViewById(R.id.reminder_time);
            mAcceptButton = (ImageButton) itemView.findViewById(R.id.button_accept);
            mAcceptButton.setOnClickListener(this);
            mRejectButton = (ImageButton) itemView.findViewById(R.id.button_reject);
            mRejectButton.setOnClickListener(this);
        }

        public void bindReminder(Reminder reminder){
            mReminder = reminder;
            mReminderID.setText(String.valueOf(mReminder.getReminderID()));
            mReminderTitle.setText(mReminder.getSender());
            mReminderFrom.setText(mReminder.getReminderDesc());
            mReminderDate.setText(mReminder.getDate().toString());
            mReminderDate.setText(mReminder.getTime().toString());
        }

        @Override
        public void onClick(View v) {
            Reminder reminder;
            int remId = Integer.parseInt(mReminderID.getText().toString());
            reminder = reminderList.getSingleReminder(remId);
            switch (v.getId()){
                case R.id.button_accept:{
                    Update_Calendar(v,reminder);
                    break;
                }
                case R.id.button_reject:{
//                    Log.d("Delete", String.valueOf(reminder.getReminderID()));
                    if(mDatabaseHelper.deleteReminder(reminder)){
                        Toast.makeText(getContext(),"Delete Successful",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
            reminderList.removeReminder(reminder);
            mAdapter.notifyItemRemoved(getAdapterPosition());
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

    public boolean updateUI(){
        reminderList = ReminderList.get(getActivity());
        List<Reminder> reminders = reminderList.getReminders();

        mAdapter = new ReminderAdapter(reminders);
        mCrimeRecyclerView.setAdapter(mAdapter);
        return !reminders.isEmpty();
    }

    public void Update_Calendar(View v,Reminder reminder){
//        long startMillis = 0;
//        long endMillis = 0;
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2016, 11, 14, 7, 30);
//        startMillis = beginTime.getTimeInMillis();
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(2016, 11, 14, 7, 45);
//        endMillis = endTime.getTimeInMillis();
//        values.put(CalendarContract.Events.DTEND,(new Date()).getTime() + 60 * 60 * 10);
//        values.put(CalendarContract.Events.DTSTART,(new Date()).getTime() + 60 * 60 * 1);
        Date date = reminder.getDate();
        Time time = reminder.getTime();
        Calendar eventTime = Calendar.getInstance();
        eventTime.setTime(date);
        Calendar cal = Calendar.getInstance();
        eventTime.set(eventTime.get(Calendar.YEAR),eventTime.get(Calendar.MONTH),eventTime.get(Calendar.DATE),
                time.getHours(),time.getMinutes());
        long startMillis = eventTime.getTimeInMillis();
        cr = v.getContext().getContentResolver();
        values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART,startMillis);
        values.put(CalendarContract.Events.DTEND,startMillis);
        values.put(CalendarContract.Events.TITLE,reminder.getSender());
        values.put(CalendarContract.Events.DESCRIPTION,reminder.getReminderDesc());
        values.put(CalendarContract.Events.CALENDAR_ID,1);
        values.put(CalendarContract.Events.STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        int permissionCheck = ContextCompat.checkSelfPermission(v.getContext(),
                android.Manifest.permission.WRITE_CALENDAR);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_CALENDAR}, 1);
        }
        else {
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Toast.makeText(getContext(),"Update Successful",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.WRITE_CALENDAR);
                    if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        cr.insert(CalendarContract.Events.CONTENT_URI, values);
                        Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}
