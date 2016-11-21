package edu.ohio_state.cse.nagger;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

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
    int year,month,day,hour,minute;
    String formatedDate;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 2000;

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
        day  = mDate.getDayOfMonth();
        month= mDate.getMonth();
        year = mDate.getYear();
        hour = mTime.getCurrentHour();
        minute = mTime.getCurrentMinute();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formatedDate = sdf.format(new Date(year-1900, month, day));
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//        String formatted = format.format();
//        Log.d("Date","Hour " + hour + "Minute " + minute);
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                OkHttpClient mClient = new OkHttpClient();
                mClient.retryOnConnectionFailure();
                Calendar calendar = Calendar.getInstance();
                RequestBody requestBody = new FormBody.Builder().
                        add("Sender", mUser.getUserName()).
                        add("Email",mEmail).
//                        add("Email",String.valueOf(mRecipientText)).
        add("Description",desc).
                                add("Date", formatedDate).
                                add("Time",String.format("%02d:%02d:%02d",hour,minute,calendar.get(Calendar.SECOND))).build();
//                Toast.makeText(getActivity(),"Reminder Sent!",Toast.LENGTH_SHORT).show();
//                        add("Time",String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))).build();

//                String emailAddress = "eeqUrb08aAM:APA91bHyS0KsCt1R0qODBpE4JZ49AlOJOuVvLVE48stxYDo8zSt5W7mn7MJQhuMXM_labRKvDfjSt_y5wRaJYsV8GcpzgZ-Z-kHJ4tz3W_rjmgDALvg1m7z7qDofjUQHsNVSMG-uwQ_8";

                try {

                    Response responses = null;
                    try {
                        mClient.newCall(new Request.Builder().url("http://192.168.43.8/sendpush.php").
                                post(requestBody).build()).execute();

                        String jsonData = responses.body().string();

                        JSONObject Jobject = new JSONObject(jsonData);
                        //Log.e("Bhai.........",jsonData);
                        int status = Jobject.getInt("success");
                        String message = Jobject.getString("message");
                        if(status==1)
                            Log.e("User hai in Database", message);
                        if(status == 0)
                            Log.e("Database Error", message);
                        if(status==-1)
                            Log.e("User not registered", message);

                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
//                    mClient.newCall(new Request.Builder().get().url("http://192.168.0.9/index1.php?user_id=" + emailAddress + "&message=asdlkfjasdlkfjalsdfkjaslfj").build()).execute();
                } catch (IOException e) {
                    Log.e("CreateReminderFragment", "Unable to send message", e);
//                    Toast.makeText(getActivity(),"Unable to send reminder..Please try again",Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        };
        asyncTask.execute();
        startActivity(new Intent(this.getActivity(),ListActivity.class));
        Toast.makeText(getActivity(),"Reminder Sent!",Toast.LENGTH_SHORT).show();
    }

}
