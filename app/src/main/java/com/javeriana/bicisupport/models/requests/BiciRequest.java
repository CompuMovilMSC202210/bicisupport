package com.javeriana.bicisupport.models.requests;

public class BiciRequest {

    private String brand;
    private int model;
    private String color;
    private String type;

    public BiciRequest(String brand, int model, String color, String type) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
