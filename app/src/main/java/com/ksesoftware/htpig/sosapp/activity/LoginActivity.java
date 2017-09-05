/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.ksesoftware.htpig.sosapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ToolTipPopup;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.action.GetJsonUser;
import com.ksesoftware.htpig.sosapp.action.LoginJsonUser;
import com.ksesoftware.htpig.sosapp.app.AppConfig;
import com.ksesoftware.htpig.sosapp.app.AppController;
import com.ksesoftware.htpig.sosapp.databinding.ActivitySplashBinding;
import com.ksesoftware.htpig.sosapp.helper.SQLiteHandler;
import com.ksesoftware.htpig.sosapp.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_SELECT;
import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_UPDATE;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 001;
    static String name;
    static String email;
    static String idFB;
    static String idGG;
    static String checkFB;
    static String checkGG;
    static String gender;
    static String picture;
    private LoginButton btnFacebookrg;
    private CallbackManager callbackManager;
    Button btnSignIn;
    LinearLayout btnLinkSingUp;
    EditText edtEmail, edtPass;
    ArrayList<HashMap<String, String>> contaclist;
    JSONObject jsonObject = null;
    static boolean check = false;
    static String kiemTra, kiemtra1 = "";
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String PREF_KEY_FIRST_START = "com.ksesoftware.htping.sosapp.PREF_KEY_FIRST_START";
    public static final int REQUEST_CODE_INTRO = 1;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        boolean firstStart = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PREF_KEY_FIRST_START, true);

        if (firstStart) {
            Intent intent = new Intent(this, SplashIntroActivity.class);
            startActivityForResult(intent, REQUEST_CODE_INTRO);
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (MapsActivity.check2.equalsIgnoreCase("check")) {
            db = new SQLiteHandler(getApplicationContext());
            db.deleteUsers();
            // Session manager
            session = new SessionManager(getApplicationContext());
            session.setLogin(false);
        } else {
            db = new SQLiteHandler(getApplicationContext());
            session = new SessionManager(getApplicationContext());
        }
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPassword);
        btnLinkSingUp = (LinearLayout) findViewById(R.id.btnSingUp);
        btnLinkSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        Anhxa();
        contaclist = new ArrayList<>();
        new GetJsonUser(LoginActivity.this, AppConfig.URL_SELECT, contaclist).execute();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();
                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        //Tim keyhash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.atbic.ediyv", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        //Yeu cau ng dung cung cap thong tin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(signInButton, "GooglePlus");
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    private void Anhxa() {
        btnFacebookrg = (LoginButton) findViewById(R.id.login_button);

        btnFacebookrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiemtra1 = "facebook";
                if (checkFB != null && !checkFB.equals("")) {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else {
                    setLogin_Button();
                }
            }
        });
        btnFacebookrg.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnFacebookrg.setToolTipStyle(ToolTipPopup.Style.BLACK);
        btnFacebookrg.setLoginBehavior(LoginBehavior.WEB_ONLY);
        SharedPreferences sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
        checkFB = sharedPreferences.getString("idFacebook", "");
        checkGG = sharedPreferences.getString("idGoogle", "");

    }

    //facebook
    private void setLogin_Button() {

        btnFacebookrg.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                refus();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Bạn vừa thoát đăng nhập facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Doi ten nut sign-in
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    //facebook
    private void refus() {
        kiemTra = "facebook";
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    object = response.getJSONObject();
                    Intent intent = null;
                    name = object.getString("name");
                    email = object.getString("email");
                    gender = object.getString("gender");
                    idFB = object.getString("id");
                    if (object.has("picture")) {
                        picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    }
                    System.out.println(picture);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("email", email);
                    bundle.putString("gender", gender);
                    bundle.putString("picture", picture);
                    bundle.putString("idFB", idFB);
                    for (int i = 0; i < contaclist.size(); i++) {
                        if (idFB.equals(contaclist.get(i).get("id"))) {
                            check = true;
                            intent = new Intent(getApplicationContext(), MapsActivity.class);
                            break;
                        }
                    }
                    if (check == false) {
                        for (int i = 0; i < contaclist.size(); i++) {
                            if (!idFB.equals(contaclist.get(i).get("id"))) {
                                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                intent.putExtra("bundle", bundle);
                                break;
                            }
                        }
                    }
                    LoginManager.getInstance().logOut();
                    startActivity(intent);
                    Log.d("Facebook", idFB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "name,email,gender,picture.type(large),first_name");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (kiemtra1.equals("facebook")) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (kiemtra1.equals("gmail")) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        } else {
            if (requestCode == REQUEST_CODE_INTRO) {
                if (resultCode == RESULT_OK) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                            .putBoolean(PREF_KEY_FIRST_START, false)
                            .apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                            .putBoolean(PREF_KEY_FIRST_START, true)
                            .apply();
                    //User cancelled the intro so we'll finish this activity too.
                    finish();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Failed", connectionResult + "");
    }

    private void handleSignInResult(GoogleSignInResult result) {
        kiemTra = "gmail";
        if (result.isSuccess()) {
            Intent intent = null;
            int gioitinh;
            Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            gioitinh = person.getGender();
            name = acct.getDisplayName();
            email = acct.getEmail();
            idGG = acct.getId();
            if (gioitinh == 0) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            picture = String.valueOf(person.getImage().getUrl());

            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("email", email);
            bundle.putString("gender", gender);
            bundle.putString("picture", picture);
            bundle.putString("idGG", idGG);

            for (int i = 0; i < contaclist.size(); i++) {
                String a = contaclist.get(i).get("id");
                System.out.println(a);
                if (idGG.equals(contaclist.get(i).get("id"))) {
                    check = true;
                    intent = new Intent(getApplicationContext(), MapsActivity.class);
                    break;
                }
            }
            if (check == false) {
                for (int i = 0; i < contaclist.size(); i++) {
                    if (!idGG.equals(contaclist.get(i).get("id"))) {
                        intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        intent.putExtra("bundle", bundle);
                        break;
                    }
                }
            }
            startActivity(intent);
            Log.d("GOOGLE", idGG);
        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                kiemtra1 = "gmail";
                if (checkGG != null && !checkGG.equals("")) {
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else {
                    signIn();
                }
                break;
        }
    }

    /*Login*/
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("tbl_userregister");
                        String name = user.getString("sodienthoai");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}