package edu.ohio_state.cse.nagger;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Sayam Ganguly on 10/19/2016.
 */
public class Reminder {

    private int mReminderID;
    private String mReminderTitle;
    private String mReminderDesc;
    private Date mDate;
    private java.sql.Time mTime;

    public Reminder(String reminderTitle, String reminderDesc) {
        mReminderID = (int)Math.random();
        mReminderTitle = reminderTitle;
        mReminderDesc = reminderDesc;
        mDate = new Date();
//        mTime = (Time)Calendar.getInstance().getTime();
    }

    public Reminder(int reminderID, String reminderTitle, String reminderDesc, Date date, Time time) {
        mReminderID = reminderID;
        mReminderTitle = reminderTitle;
        mReminderDesc = reminderDesc;
        mDate = date;
        mTime = time;
    }


    public int getReminderID() {
        return mReminderID;
    }

    public String getReminderTitle() {
        return mReminderTitle;
    }

    public String getReminderDesc() {
        return mReminderDesc;
    }

    public Date getDate() {
        return mDate;
    }

    public Time getTime() {
        return mTime;
    }
}
