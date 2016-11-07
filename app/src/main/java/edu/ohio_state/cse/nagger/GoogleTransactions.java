package edu.ohio_state.cse.nagger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class GoogleTransactions {
    private GoogleApiClient mGoogleApiClient;
    private boolean isLoggedOut;
    private static GoogleTransactions mGoogleTransactions;

    public GoogleTransactions(SplashActivity splashActivity){
        if(mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            mGoogleApiClient = new GoogleApiClient.Builder(splashActivity).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
//                    .enableAutoManage(splashActivity, splashActivity).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }
        mGoogleTransactions = this;
    }

    public static GoogleTransactions getGoogleTransaction(){
        return mGoogleTransactions;
    }

    public Intent signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        return signInIntent;
    }

    public boolean signOut(){
        Log.d("Signout", "IsSignin api null ? " + (mGoogleApiClient == null));
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                GoogleTransactions.this.isLoggedOut = status.isSuccess();
            }
        });
        return isLoggedOut;
    }
}
