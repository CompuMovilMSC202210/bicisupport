package com.javeriana.bicisupport.models;

import java.util.ArrayList;
import java.util.List;

public class FirebaseChat {
    private String destinataryId;
    private String senderId;
    private List<FirebaseMessage> messages;

    public FirebaseChat(String destinataryId, String senderId) {
        this.destinataryId = destinataryId;
        this.senderId = senderId;
        this.messages = new ArrayList<>();
    }

    public FirebaseChat() {
        this.messages = new ArrayList<>();
    }

    public FirebaseChat(String destinataryId, String senderId, List<FirebaseMessage> messages) {
        this.destinataryId = destinataryId;
        this.senderId = senderId;
        this.messages = messages;
    }

    public String getDestinataryId() {
        return destinataryId;
    }

    public void setDestinataryId(String destinataryId) {
        this.destinataryId = destinataryId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public List<FirebaseMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<FirebaseMessage> messages) {
        this.messages = messages;
    }
}
