package com.javeriana.bicisupport.models;

import java.io.Serializable;

public class BEMessage implements Serializable {

    private String msg;
    private String status;

    public BEMessage(String msg, String status) {
        this.msg = msg;
        this.status = status;
    }

    public BEMessage() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
