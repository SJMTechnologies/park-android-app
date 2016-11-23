package com.sjmtechs.park.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sjmtechs.park.R;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 0x1;

    //email
    @InjectView(R.id.etEmail)
    EditText etEmail;

    @InjectView(R.id.etPassword)
    EditText etPassword;

    @InjectView(R.id.btnLogin)
    Button btnLogin;

    @InjectView(R.id.txtNeedAnAccount)
    TextView txtNeedAnAccount;

    @InjectView(R.id.btnFacebook)
    ImageView btnFacebook;

    @InjectView(R.id.btnGooglePlus)
    ImageView btnGooglePlus;

    SharedPreferences pref;
    private String strEmail = "", strPassword = "";
//    private Databasehelper db;

    private CallbackManager mCallBackManager;
    private LoginManager loginManager;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        printKeyHash();
//        db = new Databasehelper(LoginActivity.this);
//        pref = getSharedPreferences(Constant.PREF_NAME, 0);
//        boolean isLoggedIn = pref.getBoolean(Constant.USER_LOGIN, false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        if (isLoggedIn) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        }
    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    this.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.txtNeedAnAccount)
    public void onRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        boolean isAllDataEntered = false;

        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();
        if (strEmail != null && strEmail.length() == 0) {
            Snackbar.make(btnLogin, "Please enter email address.", Snackbar.LENGTH_SHORT).show();
        } else if (strPassword != null && strPassword.length() == 0) {
            Snackbar.make(btnLogin, "Please enter password.", Snackbar.LENGTH_SHORT).show();
        } else {
            isAllDataEntered = true;
        }
        Log.e(TAG, "onLogin: " + isAllDataEntered);
//        if (isAllDataEntered) {
//            boolean isUserExists = db.checkLoginUser(strEmail, strPassword);
//            if (isUserExists) {
//                etEmail.setText("");
//                etPassword.setText("");
//                String username = db.getUsername(strEmail);
//                Snackbar.make(btnLogin, "Login Successfully done !", Snackbar.LENGTH_SHORT).show();
//                pref.edit().putBoolean(Constant.USER_LOGIN, true).putString(Constant.USER_NAME, username).apply();
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            } else {
//                Snackbar.make(btnLogin, "Username or password is incorrect", Snackbar.LENGTH_SHORT).show();
//                pref.edit().putBoolean(Constant.USER_LOGIN, false).apply();
//            }
//        }
    }

//    @OnClick(R.id.btnFacebook)
//    public void onFacebookPressed(){
//        Log.e(TAG, "onFacebookPressed: ");
//        facebookLogin();
//    }

    @OnClick(R.id.txtNeedAnAccount)
    public void onNeedAccountPressed(){
//        startActivity(new Intent(LoginActivity.this, ParkPreferencesActivity.class));
    }

    public void facebookLogin() {
        mCallBackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.logOut();
        loginManager = LoginManager.getInstance();
//        Log.e(TAG, "facebookLogin: " + loginManager.getLoginBehavior().toString());
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));
        loginManager.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Success + " + loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e(TAG, "onCompleted: Facebook Logged In ... " + object.toString());
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        loginManager.logOut();
                        facebookLogin();
                    }
                }
            }
        });
    }

    @OnClick(R.id.btnGooglePlus)
    void signInWithGooglePlus() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        if (mCallBackManager != null) {
            mCallBackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("name", acct.getDisplayName());
            startActivity(intent);
            Log.e(TAG, "handleSignInResult: acct.getDisplayName() " + acct.getDisplayName());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
