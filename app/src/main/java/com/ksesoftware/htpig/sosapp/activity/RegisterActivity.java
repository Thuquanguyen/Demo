/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.ksesoftware.htpig.sosapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.action.CustomHttpClient;
import com.ksesoftware.htpig.sosapp.action.RegisterJsonUser;
import com.ksesoftware.htpig.sosapp.app.AppConfig;
import com.ksesoftware.htpig.sosapp.app.AppController;
import com.ksesoftware.htpig.sosapp.helper.SQLiteHandler;
import com.ksesoftware.htpig.sosapp.helper.SessionManager;
import com.ksesoftware.htpig.sosapp.model.FacebookGmail;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_INSERT;
import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_REGISTER;
import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_SELECT;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static String name, email, idFB, idGG, gender, picture;
    private EditText edtSDT, edtEmail, edtPass, edtNhaplaiPass;
    Button btnRegister;
    FacebookGmail person;
    CircleImageView imageView;
    LinearLayout btnLinkLogin;
    ArrayList<HashMap<String, String>> contactList;
    public static String temp = "";
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSDT = (EditText) findViewById(R.id.edtSoDienThoai);
        edtPass = (EditText) findViewById(R.id.edtNhapPass);
        edtNhaplaiPass = (EditText) findViewById(R.id.edtNhapLaiPass);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        imageView = (CircleImageView) findViewById(R.id.imgView);
        btnLinkLogin = (LinearLayout) findViewById(R.id.btnLinkLogin);

        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        if (LoginActivity.kiemtra1.equals("facebook") || LoginActivity.kiemtra1.equals("gmail")) {
            final Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("bundle");
            name = bundle.getString("name");
            email = bundle.getString("email");
            gender = bundle.getString("gender");
            if (LoginActivity.kiemtra1.equals("facebook")) {
                idFB = bundle.getString("idFB");
            } else if (LoginActivity.kiemtra1.equals("gmail")) {
                idGG = bundle.getString("idGG");
            }
            picture = bundle.getString("picture");
            edtPass.setInputType(View.TEXT_DIRECTION_RTL);
            edtNhaplaiPass.setInputType(View.TEXT_DIRECTION_RTL);
            edtEmail.setText(email);
            edtSDT.setText(name);
            edtPass.setText(gender);
            if (LoginActivity.kiemtra1.equals("facebook")) {
                edtNhaplaiPass.setText(idFB);
                Picasso.with(imageView.getContext()).load(picture).into(imageView);
            } else if (LoginActivity.kiemtra1.equals("gmail")) {
                edtNhaplaiPass.setText(idGG);
                Picasso.with(imageView.getContext()).load(picture).into(imageView);
            }
            if (isConnected()) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Connect Fail", Toast.LENGTH_SHORT).show();
            }

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (LoginActivity.kiemtra1.equals("facebook")) {
                        editor.putString("idFacebook", idFB);
                    } else if (LoginActivity.kiemtra1.equals("gmail")) {
                        editor.putString("idGoogle", idGG);
                    }
                    editor.commit();
                    Intent intent1 = new Intent(RegisterActivity.this, MapsActivity.class);
                    startActivity(intent1);
                    if (LoginActivity.kiemtra1.equals("facebook")) {
                        if (!validate())
                            Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                    } else if (LoginActivity.kiemtra1.equals("gmail")) {
                        if (!validate1())
                            Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                    }
                    new HttpAsyncTask().execute(AppConfig.URL_INSERT);
                }
            });
            LoginActivity.kiemtra1="";

        } else {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = edtSDT.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String password = edtPass.getText().toString().trim();
                    String nhaplaipass = edtNhaplaiPass.getText().toString();
                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(nhaplaipass)) {
                        registerUser(name, email, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /*Phương thức POST*/
    public static String POST(String url, FacebookGmail person) throws JSONException, IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", person.getId()));
        params.add(new BasicNameValuePair("name", person.getName()));
        params.add(new BasicNameValuePair("email", person.getEmail()));
        params.add(new BasicNameValuePair("gioitinh", person.getGioitinh()));
        params.add(new BasicNameValuePair("linkanh", person.getLinkanh()));
        String result = CustomHttpClient.makeHttpRequest(AppConfig.URL_INSERT, "POST", params).toString();
        return result;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String a = null;
            person = new FacebookGmail();
            if (LoginActivity.kiemtra1.equals("facebook")) {
                person.setId(idFB);
            } else if (LoginActivity.kiemtra1.equals("gmail")) {
                person.setId(idGG);
            }
            person.setName(name);
            person.setEmail(email);
            person.setGioitinh(gender);
            person.setLinkanh(picture);
            try {
                a = POST(urls[0], person);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return a;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate() {
        if (idFB.trim().equals(""))
            return false;
        else if (name.trim().equals(""))
            return false;
        else if (email.trim().equals(""))
            return false;
        else if (gender.trim().equals(""))
            return false;
        else if (picture.trim().equals(""))
            return false;
        else
            return true;
    }

    private boolean validate1() {
        if (idGG.trim().equals(""))
            return false;
        else if (name.trim().equals(""))
            return false;
        else if (email.trim().equals(""))
            return false;
        else if (gender.trim().equals(""))
            return false;
        else if (picture.trim().equals(""))
            return false;
        else
            return true;
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String sdt, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("tbl_userregister");
                        String sdt = user.getString("sodienthoai");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        // Inserting row in users table
                        db.addUser(sdt, email, uid, created_at);
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("sodienthoai", sdt);
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
