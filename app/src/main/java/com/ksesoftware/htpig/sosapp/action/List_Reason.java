package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.adapter.ReasonAdapter;
import com.ksesoftware.htpig.sosapp.model.Reason;

import java.util.ArrayList;

/**
 * Created by atbic on 7/1/2017.
 */

public class List_Reason extends Activity {
    private ListView listView;
    private ReasonAdapter reasonAdapter;
    static ArrayList<Reason> reasons;
    private ImageView btnReasonBack;
    ArrayList<Reason> sinhVienModels;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_reason);
        sinhVienModels= new ArrayList<>();

        init();
        setData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reason reason = reasons.get(position);
                Intent intent = new Intent();
                intent.putExtra("reason",reason.getReason());
                setResult(123,intent);
                finish();
            }
        });
    }

    private void setData() {
        reasons = getSinhVienModels();
        reasonAdapter = new ReasonAdapter(getApplicationContext(), reasons);
        listView.setAdapter(reasonAdapter);
        btnReasonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });
    }

    private ArrayList<Reason> getSinhVienModels() {
        Reason hoaHoan = new Reason();
        hoaHoan.setReason("Hỏa Hoạn");
        sinhVienModels.add(hoaHoan);

        Reason tromCap = new Reason();
        tromCap.setReason("Trộm Cắp");
        sinhVienModels.add(tromCap);
        Reason taiNanGiaoThong = new Reason();
        tromCap.setReason("Tai Nạn Giao Thông");
        sinhVienModels.add(taiNanGiaoThong);


        return sinhVienModels;
    }


    private void init() {

        listView= (ListView) findViewById(R.id.lvReason);
        btnReasonBack= (ImageView) findViewById(R.id.imgReasonBack);
    }
}
