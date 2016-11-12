package edu.ohio_state.cse.nagger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";
    private Reminder mReminder;
    DatabaseHelper mDatabaseHelper;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        Log.d(TAG,remoteMessage.getData().get("Sender"));
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification From: " + remoteMessage.getData().get("Sender"));
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mReminder = new Reminder(remoteMessage.getData().get("Sender"),remoteMessage.getData().get("Description"),
                    simpleDateFormat.parse(remoteMessage.getData().get("Date")), Time.valueOf(remoteMessage.getData().get("Time")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("In Here");
        Log.d("5236","In Here");
        DatabaseHelper.setTableName(DatabaseHelper.REMINDER_TABLE);
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.insertReminder(mReminder);
        ReminderList reminderList = ReminderList.get(this);
        reminderList.updateReminderList(mReminder);
    }



}