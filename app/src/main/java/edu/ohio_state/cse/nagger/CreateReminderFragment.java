package edu.ohio_state.cse.nagger;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CreateReminderFragment extends Fragment implements SensorEventListener {

    private User mUser;
    private Reminder mRemnider;
    private String mEmail;
    String desc;
    private Button mSendReminder;
    private EditText mRecipientText;
    private EditText mDescriptionText;
    private DatePicker mDate;
    private TimePicker mTime;
    ContentResolver cr;
    ContentValues values;
    String projection[] = {"_id", "calendar_displayName"};
    android.net.Uri calendars;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        senSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_reminder_fragment, container, false);
//        return super.onCreateView(inflater, container, savedInstanceState);
        mUser = UserManager.getUser();
        mSendReminder = (Button) v.findViewById(R.id.button_send_reminder);
        mRecipientText = (EditText) v.findViewById(R.id.recipient_text);
        mDescriptionText = (EditText) v.findViewById(R.id.description_text);
        mDate = (DatePicker) v.findViewById(R.id.date);
        mTime = (TimePicker) v.findViewById(R.id.time);
        mSendReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Insert Here for Sending to Sever
                sendServer();
            }
        });
        return v;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    sendServer();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void sendServer() {
        mEmail = mRecipientText.getText().toString();
        desc = mDescriptionText.getText().toString();
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                OkHttpClient mClient = new OkHttpClient();
                Calendar calendar = Calendar.getInstance();
                RequestBody requestBody = new FormBody.Builder().
                        add("Sender", mUser.getUserName()).
                        add("Email",mEmail).
//                        add("Email",String.valueOf(mRecipientText)).
                        add("Description",desc).
                        add("Date", new SimpleDateFormat("MM/dd/yyyy").format(new Date())).
                        add("Time",String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))).build();

//                String emailAddress = "eeqUrb08aAM:APA91bHyS0KsCt1R0qODBpE4JZ49AlOJOuVvLVE48stxYDo8zSt5W7mn7MJQhuMXM_labRKvDfjSt_y5wRaJYsV8GcpzgZ-Z-kHJ4tz3W_rjmgDALvg1m7z7qDofjUQHsNVSMG-uwQ_8";
                try {
                    mClient.newCall(new Request.Builder().url("http://192.168.0.9/sendpush.php").
                    post(requestBody).build()).execute();
//                    mClient.newCall(new Request.Builder().get().url("http://192.168.0.9/index1.php?user_id=" + emailAddress + "&message=asdlkfjasdlkfjalsdfkjaslfj").build()).execute();
                } catch (IOException e) {
                    Log.e("CreateReminderFragment", "Unable to send message", e);
                }
                return null;
            }

        };
        asyncTask.execute();
    }
}
