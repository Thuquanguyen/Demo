package com.ksesoftware.htpig.sosapp.model;

/**
 * Created by thuqu on 8/7/2017.
 */

public class FacebookGmail {
    String id;
    String name;
    String email;
    String gioitinh;
    String linkanh;

    public FacebookGmail() {
    }

    public FacebookGmail(String id, String name, String email, String gioitinh, String linkanh) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gioitinh = gioitinh;
        this.linkanh = linkanh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getLinkanh() {
        return linkanh;
    }

    public void setLinkanh(String linkanh) {
        this.linkanh = linkanh;
    }
}
