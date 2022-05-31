package com.javeriana.bicisupport.models;

import java.io.Serializable;

public class UserList implements Serializable {

    private String name;
    private String user;
    private String localId;
    private String cloudToken;

    public UserList(String name, String user, String localId, String cloudToken) {
        this.name = name;
        this.user = user;
        this.localId = localId;
        this.cloudToken = cloudToken;
    }

    public UserList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getCloudToken() {
        return cloudToken;
    }

    public void setCloudToken(String cloudToken) {
        this.cloudToken = cloudToken;
    }

    @Override
    public String toString() {
        return name;
    }
}
