package edu.ohio_state.cse.nagger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private SignInButton mButtonSignIn;
    private GoogleTransactions mGoogleTransactions;
    private User mUser;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG,"Inside Method OnCreate");

        DatabaseHelper.setTableNAme(DatabaseHelper.USER_TABLE);
        databaseHelper = new DatabaseHelper(this);
        mGoogleTransactions = new GoogleTransactions(this);

        mButtonSignIn = (SignInButton) findViewById(R.id.button_sign_in);
        mButtonSignIn.setSize(SignInButton.SIZE_WIDE);

        mButtonSignIn.setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult conncectionResult){

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_sign_in:
                signIn();
                break;
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleTransactions.signIn();
        startActivityForResult(signInIntent, 9001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            Toast.makeText(this, account.getDisplayName(), Toast.LENGTH_SHORT).show();
            UserManager userManager = new UserManager();
            mUser = userManager.createUser(account.getDisplayName(),account.getEmail());
            databaseHelper.insert(mUser.getEmail(),mUser.getUserName());
            startActivity(new Intent(this,ListActivity.class));
        }
        else
            Toast.makeText(this,"Wrong Email",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"Inside Method OnStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"Inside Method OnResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"Inside Method OnPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"Inside Method OnStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"Inside Method OnDestroy");
    }
}
