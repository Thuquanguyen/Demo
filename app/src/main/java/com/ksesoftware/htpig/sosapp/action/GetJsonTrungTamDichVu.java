package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ksesoftware.htpig.sosapp.model.DichVu;
import com.ksesoftware.htpig.sosapp.model.Xe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by thuqu on 8/1/2017.
 */

public class GetJsonTrungTamDichVu extends AsyncTask<Void, Void, Void> {
    Activity activity;
    String url;
    ArrayList<DichVu> list = new ArrayList<DichVu>();


    public GetJsonTrungTamDichVu(Activity activity, String url, ArrayList<DichVu> list) {
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
                JSONArray array = object.getJSONArray("tbl_trungtam");
                for (int i = 0; i < array.length(); i++) {
                    DichVu dichVu = new DichVu();
                    JSONObject jsonObject = array.getJSONObject(i);
                    String idxe = jsonObject.getString("id");
                    String tenxe = jsonObject.getString("dichvu");
                    dichVu.setIdDichVu(idxe);
                    dichVu.setTenDichVu(tenxe);
                    list.add(dichVu);
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
