package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HTPIG on 7/17/2017.
 */

public class GetJsonUser extends AsyncTask<Void, Void, Void> {

    Activity activity;
    String url;
    ArrayList<HashMap<String, String>> contaclist;


    public GetJsonUser(Activity activity, String url, ArrayList<HashMap<String, String>> contaclist) {
        contaclist.clear();
        this.activity = activity;
        this.url = url;
        this.contaclist = contaclist;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpHandler handler = new HttpHandler();
        String json = handler.makeServiceCall(url);
        if (json != null) {
            try {
                JSONObject object = new JSONObject(json);
                JSONArray array = object.getJSONArray("tbl_inforuser");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String email = jsonObject.getString("email");
                    String gioitinh = jsonObject.getString("gioitinh");
                    String linkanh = jsonObject.getString("linkanh");

                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("id", id);
                    contact.put("name", name);
                    contact.put("email", email);
                    contact.put("gioitinh", gioitinh);
                    contact.put("linkanh", linkanh);
                    contaclist.add(contact);
                }
            } catch (final JSONException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
