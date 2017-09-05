package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ksesoftware.htpig.sosapp.model.DichVu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thuqu on 8/1/2017.
 */

public class GetJsonDichVu extends AsyncTask<Void, Void, Void> {
    Activity activity;
    String url;
    List<DichVu> list = new ArrayList<>();

    public GetJsonDichVu(Activity activity, String url, List<DichVu> list) {
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
                JSONArray array = object.getJSONArray("tbl_dichvu");
                for (int i = 0; i < array.length(); i++) {
                    DichVu dichVu = new DichVu();
                    JSONObject jsonObject = array.getJSONObject(i);
                    String idDichVu = String.valueOf(jsonObject.getInt("iddichvu"));
                    String tenDichVu = jsonObject.getString("tendichvu");
                    dichVu.setTenDichVu(tenDichVu);
                    dichVu.setIdDichVu(idDichVu);
                    dichVu.setCheckBox(false);
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
    }
}
