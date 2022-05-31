package com.javeriana.bicisupport.models;

public class LocationSharing {

    String userSender;
    String userReceiver;
    double latitud;
    double longitud;
    boolean isActive;

    public LocationSharing() {
    }

    public String getUserSender() {
        return userSender;
    }

    public void setUserSender(String userSender) {
        this.userSender = userSender;
    }

    public String getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(String userReceiver) {
        this.userReceiver = userReceiver;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
