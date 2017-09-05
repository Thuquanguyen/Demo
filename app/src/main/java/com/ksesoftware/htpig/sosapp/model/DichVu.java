package com.ksesoftware.htpig.sosapp.model;

/**
 * Created by thuqu on 8/17/2017.
 */

public class DichVu {
    String idDichVu;
    String tenDichVu;
    boolean checkBox;

    public DichVu() {
    }

    public DichVu(String idDichVu, String tenDichVu, boolean checkBox) {
        this.idDichVu = idDichVu;
        this.tenDichVu = tenDichVu;
        this.checkBox = checkBox;
    }

    public String getIdDichVu() {
        return idDichVu;
    }

    public void setIdDichVu(String idDichVu) {
        this.idDichVu = idDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    public String toString() {
        return getTenDichVu();
    }
}
