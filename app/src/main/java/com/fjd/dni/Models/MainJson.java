package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class MainJson {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Datos_Upload_Prueba getDatos() {
        return datos;
    }

    public void setDatos(Datos_Upload_Prueba datos) {
        this.datos = datos;
    }

    @SerializedName("data")
    private Datos_Upload_Prueba datos;


}
