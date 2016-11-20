package edu.ohio_state.cse.nagger;

import android.os.AsyncTask;
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
                        mClient.newCall(new Request.Builder().url("http://172.31.225.113/storeUser.php").
                                post(requestBody).build()).execute();
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