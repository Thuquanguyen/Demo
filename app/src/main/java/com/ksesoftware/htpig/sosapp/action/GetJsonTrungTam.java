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

public class GetJsonTrungTam extends AsyncTask<Void, Void, Void> {

    Activity activity;
    String url;
    ArrayList<HashMap<String, String>> contaclist;


    public GetJsonTrungTam(Activity activity, String url, ArrayList<HashMap<String, String>> contaclist) {
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
                JSONArray array = object.getJSONArray("tbl_trungtam");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String id = String.valueOf(jsonObject.getInt("id"));
                    String tenTrungTam = jsonObject.getString("tentrungtam");
                    String diaChi = jsonObject.getString("diachi");
                    String website = jsonObject.getString("website");
                    String sdt = jsonObject.getString("sdt");
                    String thongtinchitiet = jsonObject.getString("thongtinchitiet");
                    String loaixe = jsonObject.getString("loaixe");
                    String tenxe = jsonObject.getString("tenxe");
                    //  String hinhanh = jsonObject.getString("hinhanh");
                    String dichvu = jsonObject.getString("dichvu");
                    String danhgia = jsonObject.getString("danhgia");
                    String kinhDo = jsonObject.getString("kinhdo");
                    String viDo = jsonObject.getString("vido");

                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("id", id);
                    contact.put("tentrungtam", tenTrungTam);
                    contact.put("diachi", diaChi);
                    contact.put("website", website);
                    contact.put("sdt", sdt);
                    contact.put("thongtinchitiet", thongtinchitiet);
                    contact.put("loaixe", loaixe);
                    contact.put("tenxe", tenxe);
                    // contact.put("hinhanh", hinhanh);
                    contact.put("dichvu", dichvu);
                    contact.put("danhgia", danhgia);
                    contact.put("kinhdo", kinhDo);
                    contact.put("vido", viDo);
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
