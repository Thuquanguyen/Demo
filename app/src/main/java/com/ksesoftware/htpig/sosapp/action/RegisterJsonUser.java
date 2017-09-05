package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ksesoftware.htpig.sosapp.activity.LoginActivity;
import com.ksesoftware.htpig.sosapp.activity.RegisterActivity;
import com.ksesoftware.htpig.sosapp.app.AppConfig;
import com.ksesoftware.htpig.sosapp.app.AppController;
import com.ksesoftware.htpig.sosapp.helper.SQLiteHandler;
import com.ksesoftware.htpig.sosapp.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HTPIG on 7/14/2017.
 */

public class RegisterJsonUser extends Application {

    private ProgressDialog pDialog;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    public static String temp = "";
    private SessionManager session;
    private SQLiteHandler db;

    private void showDialog(Activity activity) {
        pDialog =new ProgressDialog(activity);
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog(Activity activity) {
        pDialog =new ProgressDialog(activity);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public RegisterJsonUser(Activity activity) {
       pDialog =new ProgressDialog(activity);
        Context context = activity.getApplicationContext();
        if (context != null) {
            // Session manager
            session = new SessionManager(context);
            // SQLite database handler
            db = new SQLiteHandler(context);
            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
            }
        }
    }

    public void registerUser(final Activity activity, String url, final String name, final String email,
                             final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog(activity);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog(activity);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("sdt");
                        String email = user.getString("name");
                        String created_at = user.getString("created_at");
                        temp = email;
                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(activity.getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(activity.getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(activity.getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog(activity);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("sdt", email);
                params.put("name", name);
                params.put("password", password);
                temp = email;
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
