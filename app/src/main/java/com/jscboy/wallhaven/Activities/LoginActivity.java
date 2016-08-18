package com.jscboy.wallhaven.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {

    //private CallbackManager mCallbackManager;

    //private GoogleSignInOptions mGoogleSignInOptions;
    //private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//         mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
  //              .requestEmail() //specify scopes??
    //            .build();

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();*/


        /*FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton authButton = (LoginButton) findViewById(R.id.login_button);
        authButton.setReadPermissions("email");

        authButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("result", "success");
                Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });*/
    }
}
