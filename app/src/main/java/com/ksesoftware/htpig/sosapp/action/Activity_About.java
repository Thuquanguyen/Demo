package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.adapter.ShopAdapter;
import com.ksesoftware.htpig.sosapp.model.DataShop;

import java.util.ArrayList;

/**
 * Created by atbic on 7/3/2017.
 */

public class Activity_About extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imgBack;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        init();

    }

    private void init() {
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rcView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<DataShop> arrayList=new ArrayList<>();
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        arrayList.add(new DataShop("Tính năng và hướng dẫn"));
        arrayList.add(new DataShop("Đánh giá ứng dụng"));
        arrayList.add(new DataShop("Webside: http://ksesoft.com "));
        arrayList.add(new DataShop("Email góp ý: ksesoft@ksesoft.com"));
        arrayList.add(new DataShop("Hỗ trợ trực tuyến"));
        ShopAdapter shopAdapter=new ShopAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(shopAdapter);





        toolbar=(Toolbar) findViewById(R.id.tbarTop);
        setSupportActionBar(toolbar);
        MaterialMenuDrawable materialMenu = new MaterialMenuDrawable(this, Color.BLACK, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setNavigationIcon(materialMenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }
    }

