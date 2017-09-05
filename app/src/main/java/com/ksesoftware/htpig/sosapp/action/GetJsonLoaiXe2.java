package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ksesoftware.htpig.sosapp.model.Xe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by thuqu on 8/1/2017.
 */

public class GetJsonLoaiXe2 extends AsyncTask<Void, Void, Void> {
    Activity activity;
    String url;
    ArrayList<Xe> list = new ArrayList<>();


    public GetJsonLoaiXe2(Activity activity, String url, ArrayList<Xe> list) {
        list.clear();
        this.activity = activity;
        this.url = url;
        this.list = list;
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
                JSONArray array = object.getJSONArray("tbl_loaixe");
                for (int i = 0; i < array.length(); i++) {
                    Xe xe = new Xe();
                    JSONObject jsonObject = array.getJSONObject(i);
                    String idxe = jsonObject.getString("loaixe");
                    String tenxe = jsonObject.getString("tenxe");
                    xe.setIdXe(idxe);
                    xe.setTenXe(tenxe);
                    list.add(xe);
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
        activity.sendBroadcast(new Intent("ketqua"));
    }
}
