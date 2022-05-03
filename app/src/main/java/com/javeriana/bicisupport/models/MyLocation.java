package com.javeriana.bicisupport.models;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MyLocation {

    Date fecha;
    double latitud;
    double longitud;

    public MyLocation(Date fecha, double latitud, double longitud) {
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public MyLocation(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public MyLocation() {
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public JSONObject toJSON () {
        JSONObject obj = new JSONObject();
        try {
            obj.put("latitud", getLatitud());
            obj.put("longitud", getLongitud());
            obj.put("date", getFecha());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public String toString() {
        return String.valueOf(getLatitud()+"-"+getLongitud()+"-"+getFecha());
    }
}
