package com.javeriana.bicisupport.models.responses;

public class LoginResponse {

    private String token;
    private String localId;

    public LoginResponse(String token, String localId) {
        this.token = token;
        this.localId = localId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }
}
