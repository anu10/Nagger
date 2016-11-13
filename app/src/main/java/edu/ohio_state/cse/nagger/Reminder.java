package edu.ohio_state.cse.nagger;

import java.sql.Time;
import java.util.Date;

public class Reminder {

    private int mReminderID;
    private String mSender;
    private String mReminderDesc;
    private String mDate;
    private String mTime;

    public String getSender() {
        return mSender;
    }

    public Reminder(int reminderId,String sender,String reminderDesc, String date, String time) {
        mReminderID = reminderId;
        mSender = sender;
        mReminderDesc = reminderDesc;
        mDate = date;
        mTime = time;
    }

    public Reminder(String sender,String reminderDesc, String date, String time) {
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

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

}
