package edu.ohio_state.cse.nagger;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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
        Log.d("TAG",remoteMessage.getData().get("Date"));
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mReminder = new Reminder(remoteMessage.getData().get("Sender"),remoteMessage.getData().get("Description"),
                    remoteMessage.getData().get("Date"), remoteMessage.getData().get("Time"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        DatabaseHelper.setTableName(DatabaseHelper.REMINDER_TABLE);
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.insertReminder(mReminder);

        SharedPreferences mPrefs = getSharedPreferences("Foreground", Activity.MODE_PRIVATE);
        if(!mPrefs.getBoolean("Fore",false)) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.nagger)
                            .setContentTitle(mReminder.getSender())
                            .setContentText(mReminder.getReminderDesc());

            Intent resultIntent = new Intent(this, SplashActivity.class).putExtra("NoSplash", true);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            mBuilder.setAutoCancel(true);

            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
        else{
            ReminderList reminderList = ReminderList.get(this);
            reminderList.updateReminderList(mReminder);
            PubSub pubsub = PubSub.getInstance();
            pubsub.publish("Mes");
        }
    }
}