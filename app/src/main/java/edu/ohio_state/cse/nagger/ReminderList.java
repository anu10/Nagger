package edu.ohio_state.cse.nagger;

import android.content.Context;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sayam Ganguly on 10/19/2016.
 */
public class ReminderList {

    private static ReminderList sReminderList;

    private List<Reminder> mReminders;

    public static ReminderList get(Context context){
        if(sReminderList == null)
            sReminderList = new ReminderList();
        return sReminderList;
    }

    private ReminderList(){
        mReminders = new ArrayList<Reminder>();
        Reminder reminder;
        for(int i = 1; i <= 20 ; i++){
            reminder = new Reminder("Title #" + i,"Descrption #" + i);
            mReminders.add(reminder);
        }
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
