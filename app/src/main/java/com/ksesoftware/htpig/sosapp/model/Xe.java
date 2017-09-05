package com.ksesoftware.htpig.sosapp.model;

import android.widget.CheckBox;

/**
 * Created by thuqu on 8/11/2017.
 */

public class Xe {
    String idXe;
    String tenXe;
    public boolean state = false;
    public Xe() {
    }

    public Xe(String idXe, String tenXe, boolean state) {
        this.idXe = idXe;
        this.tenXe = tenXe;
        this.state = state;
    }

    public String getIdXe() {
        return idXe;
    }

    public void setIdXe(String idXe) {
        this.idXe = idXe;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return getTenXe();
    }
}
