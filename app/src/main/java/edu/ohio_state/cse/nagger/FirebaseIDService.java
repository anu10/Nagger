package edu.ohio_state.cse.nagger;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCM";
    private String refreshedToken;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("piyush", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        //Async Task
        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                User user = UserManager.getUser();
                OkHttpClient mClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder().
                        add("Email",user.getEmail()).
                        add("Token",refreshedToken).build();
                try{
                    mClient.newCall(new Request.Builder().url("http://192.168.0.9/storeUser.php").
                            post(requestBody).build()).execute();
                }
                catch (IOException e){

                }
                return null;
            }
        };
        asyncTask.execute();
    }
}