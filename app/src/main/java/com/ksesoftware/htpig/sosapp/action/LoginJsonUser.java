package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ksesoftware.htpig.sosapp.activity.LoginActivity;
import com.ksesoftware.htpig.sosapp.activity.MapsActivity;
import com.ksesoftware.htpig.sosapp.app.AppConfig;
import com.ksesoftware.htpig.sosapp.app.AppController;
import com.ksesoftware.htpig.sosapp.helper.SQLiteHandler;
import com.ksesoftware.htpig.sosapp.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ksesoftware.htpig.sosapp.app.AppConfig.URL_SELECT;

/**
 * Created by HTPIG on 7/14/2017.
 */

public class LoginJsonUser extends Application {
    private ProgressDialog pDialog;
    private static final String TAG = LoginActivity.class.getSimpleName();
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

    public LoginJsonUser(Activity activity) {
        pDialog =new ProgressDialog(activity);
        Context context = activity.getApplicationContext();
        if (context != null) {
            // SQLite database handler
            db = new SQLiteHandler(activity.getApplicationContext());
            // Session manager
            session = new SessionManager(activity.getApplicationContext());
            // Check if user is already logged in or not
            if (session.isLoggedIn()) {
                activity.startActivity(new Intent(activity.getApplicationContext(), MapsActivity.class));
            }
        }
    }

    public void checkLogin(final Activity activity,String url, final String sdt, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        showDialog(activity);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
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
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("sdt");
                        String email = user.getString("name");
                        String created_at = user.getString("created_at");
                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);
                        // Launch main activity
                        Intent intent = new Intent(activity.getApplicationContext(),MapsActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(activity.getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog(activity);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                        Toast.makeText(activity.getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(activity.getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog(activity);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("sdt", sdt);
                params.put("password", password);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
