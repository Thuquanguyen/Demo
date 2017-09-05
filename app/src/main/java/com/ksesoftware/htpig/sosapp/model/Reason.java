package com.ksesoftware.htpig.sosapp.model;

import java.io.Serializable;

/**
 * Created by atbic on 7/1/2017.
 */

public class Reason implements Serializable {
    String reason;

    public Reason() {
    }

    public Reason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Reason{" +
                "reason='" + reason + '\'' +
                '}';
    }
}
