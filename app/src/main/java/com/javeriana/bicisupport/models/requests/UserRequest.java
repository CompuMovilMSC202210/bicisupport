package com.javeriana.bicisupport.models.requests;

public class UserRequest {

    private String imageUrl;
    private String localId;
    private String name;
    private String user;
    private BiciRequest bici;

    public UserRequest(String name, String user) {
        this.name = name;
        this.user = user;
    }

    public UserRequest(String imageUrl, String localId, String name, String user) {
        this.imageUrl = imageUrl;
        this.localId = localId;
        this.name = name;
        this.user = user;
    }

    public UserRequest(String imageUrl, String localId, String name, String user, BiciRequest bici) {
        this.imageUrl = imageUrl;
        this.localId = localId;
        this.name = name;
        this.user = user;
        this.bici = bici;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
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

    public BiciRequest getBici() {
        return bici;
    }

    public void setBici(BiciRequest bici) {
        this.bici = bici;
    }
}
