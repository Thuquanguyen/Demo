package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.google.android.gms.maps.GoogleMap;
import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.activity.MapsActivity;
import com.ksesoftware.htpig.sosapp.dialog.RadiusDialog;
import com.rey.material.widget.Switch;

/**
 * Created by atbic on 7/3/2017.
 */

public class Activity_Setting extends AppCompatActivity implements RadiusDialog.ListenDialog {
    private Toolbar toolbar;
    private RelativeLayout relativeLayout;
    private Switch swMap,swNgonngu;
    private RelativeLayout llRadius;
    private TextView txtRadius;
    private RadiusDialog radiusDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        init();



    }

    private void init() {
        txtRadius= (TextView) findViewById(R.id.txtRadius);

        llRadius= (RelativeLayout) findViewById(R.id.llRadius);
        swMap= (Switch) findViewById(R.id.swMap);
        swNgonngu= (Switch) findViewById(R.id.swNgonngu);
        final SharedPreferences sharedPreferences=getSharedPreferences("my_data",MODE_PRIVATE);
        Boolean isCheckMap=sharedPreferences.getBoolean("checkMap",false);
         Boolean isCheckNgonngu=sharedPreferences.getBoolean("checkNgonngu",false);
        swMap.setChecked(isCheckMap);
        swNgonngu.setChecked(isCheckNgonngu);
        if(isCheckMap==false){
            MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }else  if(isCheckMap==true){
            MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }



        swMap.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                SharedPreferences sharedPreferences=getSharedPreferences("my_data",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("checkMap",checked);
                editor.commit();
                if(checked){MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);}else
                {MapsActivity.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);}
            }
        });
        swNgonngu.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                SharedPreferences sharedPreferences1=getSharedPreferences("my_data",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences1.edit();
                editor.putBoolean("checkNgonngu",checked);
                editor.commit();
            }
        });

        radiusDialog = new RadiusDialog(Activity_Setting.this);
        radiusDialog.setListenDialog(this);


        llRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiusDialog.show();
            }
        });

        toolbar=(Toolbar) findViewById(R.id.toolBar);
        relativeLayout= (RelativeLayout) findViewById(R.id.llRadius);
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

    @Override
    public void getStringDialog(String content) {
        txtRadius.setText(content.trim());
        Intent intent = new Intent(Activity_Setting.this, MapsActivity.class);
        MapsActivity.values = Integer.parseInt(content.trim());
        startActivity(intent);
    }
}

