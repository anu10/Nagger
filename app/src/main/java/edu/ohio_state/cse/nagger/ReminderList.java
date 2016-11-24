package edu.ohio_state.cse.nagger;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Date;
import java.sql.Time;


public class ReminderList {

    private static ReminderList sReminderList;

    private List<Reminder> mReminders;

    private DatabaseHelper mDatabaseHelper;

    public static ReminderList get(Context context){
        if(sReminderList == null)
            sReminderList = new ReminderList(context);
        return sReminderList;
    }

    private ReminderList(Context context){
        mReminders = new ArrayList<Reminder>();
        Reminder reminder;
        mDatabaseHelper = new DatabaseHelper(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor = mDatabaseHelper.selectAll(DatabaseHelper.REMINDER_TABLE);
        if(cursor.getCount() >= 1){
            while(cursor.moveToNext()){
//                try {
                Log.d("Date1",cursor.getString(3));
                    reminder = new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4));
//                        new Date(System.currentTimeMillis()),new Time(System.currentTimeMillis()));
                    mReminders.add(reminder);
//                }
//                catch (ParseException e){
//                    Log.d("Parser",e.toString());
//                }
            }
        }
    }

    public List<Reminder> getReminders(){
        return mReminders;
    }

    public void updateReminderList(Reminder reminder){
//        Log.d("Abhi","Reminder update");
        mReminders.add(reminder);
    }

    public Reminder getSingleReminder(int reminderID){
        return mReminders.get(reminderID);
//        for (Reminder reminder: mReminders) {
//            if (reminder.getReminderID() == reminderID)
//                return reminder;
//        }
//        return null;
    }

    public void removeReminder(Reminder reminder){
        mReminders.remove(reminder);
    }
}
