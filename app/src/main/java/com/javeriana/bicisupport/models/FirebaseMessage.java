package com.javeriana.bicisupport.models;

import java.io.Serializable;

public class FirebaseMessage implements Serializable {
    private String msg;
    private String sender;

    public FirebaseMessage(String msg, String sender) {
        this.msg = msg;
        this.sender = sender;
    }

    public FirebaseMessage() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
