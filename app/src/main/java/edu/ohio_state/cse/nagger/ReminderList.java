package edu.ohio_state.cse.nagger;

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Date;
import java.sql.Time;

/**
 * Created by Sayam Ganguly on 10/19/2016.
 */
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
//        reminder = new Reminder("Sayam","Meet me tmorrow",new Date(System.currentTimeMillis()),
//                                new Time(System.currentTimeMillis()));
        mDatabaseHelper = new DatabaseHelper(context);
//        mDatabaseHelper.insertReminder(reminder);
        Cursor cursor = mDatabaseHelper.selectAll(DatabaseHelper.REMINDER_TABLE);
        if(cursor.getCount() >= 1){
            while(cursor.moveToNext()){
//                String name = cursor.getString(1);
                reminder = new Reminder(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                        new Date(System.currentTimeMillis()),new Time(System.currentTimeMillis()));
                mReminders.add(reminder);
            }
        }
//        for(int i = 1; i <= 20 ; i++){
//            reminder = new Reminder("Title #" + i,"Descrption #" + i);
//        }
    }

    public List<Reminder> getReminders(){
        return mReminders;
    }

    public Reminder getSingleReminder(int reminderID){
        for (Reminder reminder: mReminders) {
            if (reminder.getReminderID() == reminderID)
                return reminder;
        }
        return null;
    }
}
