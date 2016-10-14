package edu.ohio_state.cse.nagger;

import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Sayam Ganguly on 10/13/2016.
 */
public class GoogleTransactions {
    private static GoogleApiClient mGoogleApiClient;
    private boolean isLoggedOut;

    public GoogleTransactions(LoginActivity loginActivity){
        if(mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            mGoogleApiClient = new GoogleApiClient.Builder(loginActivity)
                    .enableAutoManage(loginActivity, loginActivity).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }
    }

    public GoogleTransactions(){
        return;
    }

    public Intent signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        return signInIntent;
    }

    public boolean signOut(){
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
