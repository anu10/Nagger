package edu.ohio_state.cse.nagger;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCM";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Harsh Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }


    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param refreshedToken
     */
    public static void sendRegistrationToServer(final String refreshedToken) {
        // Add custom implementation, as needed.
        //Async Task
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                User user = UserManager.getUser();
                if (user != null) {
                    OkHttpClient mClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().
                            add("Email", user.getEmail()).
                            add("Token", refreshedToken).build();
                    try {
                        mClient.retryOnConnectionFailure();
                        Response responses = null;
                        try {
                            mClient.retryOnConnectionFailure();
                            responses = mClient.newCall(new Request.Builder().url("http://192.168.43.8/storeUser.php").
                                    post(requestBody).build()).execute();

                            //Log.e("Bhai.........",responses.toString());
                            String jsonData = responses.body().string();

                            JSONObject Jobject = new JSONObject(jsonData);
                            //Log.e("Bhai.........",jsonData);
                            int status = Jobject.getInt("success");
                            String message = Jobject.getString("message");
                            if(status==1)
                                Log.e("User hai in Database", message);
                            if(status == 0)
                                Log.e("Database Error", message);

                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }

                        Log.e(FirebaseIDService.class.getName(), "Sent token to server");
                    } catch (IOException e) {
                        Log.e(FirebaseIDService.class.getName(), "Unable to send registration token", e);
                    }
                }
                return null;
            }
        };
        asyncTask.execute();
    }
}