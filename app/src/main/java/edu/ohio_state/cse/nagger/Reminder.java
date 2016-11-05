package edu.ohio_state.cse.nagger;

import android.text.style.TtsSpan;

import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.util.UUID;

/**
 * Created by Sayam Ganguly on 10/19/2016.
 */
public class Reminder {

    private int mReminderID;
    private String mSender;
    private String mReminderDesc;
    private Date mDate;
    private java.sql.Time mTime;

    public String getSender() {
        return mSender;
    }

    public Reminder(int reminderId,String sender,String reminderDesc, Date date, Time time) {
        mReminderID = reminderId;
        mSender = sender;
        mReminderDesc = reminderDesc;
        mDate = date;
        mTime = time;
    }

    public Reminder(String sender,String reminderDesc, Date date, Time time) {
        mSender = sender;
        mReminderDesc = reminderDesc;
        mDate = date;
        mTime = time;
    }


    public int getReminderID() {
        return mReminderID;
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
