package com.javeriana.bicisupport.models;

public class Mensaje {
    String mensaje;
    String emisor;

    public Mensaje(String mensaje, String emisor) {
        this.mensaje = mensaje;
        this.emisor = emisor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }
}
