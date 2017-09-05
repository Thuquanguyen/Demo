package com.ksesoftware.htpig.sosapp.activity;

import android.app.ExpandableListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;

import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.action.GetJsonChiTietXe;
import com.ksesoftware.htpig.sosapp.action.GetJsonLoaiXe;
import com.ksesoftware.htpig.sosapp.action.GetJsonLoaiXe2;
import com.ksesoftware.htpig.sosapp.adapter.XeAdapter;
import com.ksesoftware.htpig.sosapp.model.Xe;

import java.util.ArrayList;

public class DoiTuongXe extends ExpandableListActivity {
    private static final String LOG_TAG = "ElistCBox2";
    XeAdapter expListAdapter;
    public static String URL_LOAI = "http://appcuuho.ksesoft.com/select_loaixe.php";
    String URL_SELECT_CHITIETXE = "http://appcuuho.ksesoft.com/select_chitietxe.php";
    ArrayList<Xe> listLoaiXe;
    ArrayList<Xe> listLoaiXe1;
    ArrayList<Xe> listChiTiet;
    ArrayList<ArrayList<Xe>> colors;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hienthi();
        }
    };
    Button btnLuaChon;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        colors = new ArrayList<ArrayList<Xe>>();

        btnLuaChon = (Button) findViewById(R.id.btnLuaChon);
        btnLuaChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        listLoaiXe = new ArrayList<>();
        listLoaiXe1 = new ArrayList<>();
        listChiTiet = new ArrayList<>();

        new GetJsonChiTietXe(DoiTuongXe.this, URL_SELECT_CHITIETXE, listChiTiet).execute();
        new GetJsonLoaiXe(DoiTuongXe.this, URL_LOAI, listLoaiXe).execute();
        new GetJsonLoaiXe2(DoiTuongXe.this, URL_LOAI, listLoaiXe1).execute();

        while (listLoaiXe1.size() == 0) {
            registerReceiver(broadcastReceiver, new IntentFilter("ketqua"));
        }

    }

    public void onContentChanged() {
        super.onContentChanged();

    }

    public boolean onChildClick(
            ExpandableListView parent,
            View v,
            int groupPosition,
            int childPosition,
            long id) {
        CheckBox cb = (CheckBox) v.findViewById(R.id.check1);
        if (cb != null)
            cb.toggle();
        return false;
    }

    public void hienthi() {
        ArrayList<String> groupNames = new ArrayList<String>();
        for (int i = 0; i < listLoaiXe.size(); i++) {
            groupNames.add(listLoaiXe.get(i).getTenXe().toString());
        }
        for (int i = 0; i < listLoaiXe.size(); i++) {
            colors.add(displayTenXe(i));
        }
        expListAdapter = new XeAdapter(this, groupNames, colors);
        setListAdapter(expListAdapter);
        expListAdapter.notifyDataSetChanged();
    }

    /*xe của hãng*/
    public ArrayList<Xe> displayTenXe(int k) {
        ArrayList<Xe> listChiTietXe_copy = new ArrayList<>();
        String b[];
        if (listLoaiXe1.size() > 0) {
            b = listLoaiXe1.get(k).getTenXe().split(",");
            for (int j = 0; j < b.length; j++) {
                for (int i = 0; i < listChiTiet.size(); i++) {
                    if (listChiTiet.get(i).getIdXe().equals(b[j])) {
                        listChiTietXe_copy.add(listChiTiet.get(i));
                    }
                }
            }
        }
        return listChiTietXe_copy;
    }
}
