package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class SendDatosFotos {

    @SerializedName("token")
    private String token;
    @SerializedName("ID")
    private String id;
    @SerializedName("CUENTA")
    private String cuenta;
    @SerializedName("NUMERO")
    private String numero;
    @SerializedName("ANIO")
    private String anio;
    @SerializedName("MES")
    private String mes;
    @SerializedName("CARPETA")
    private String carpeta;
    @SerializedName("PROCEDIMIENTO")
    private String procedimiento;
    @SerializedName("FOTO64")
    private String fotoB64;

    public SendDatosFotos(){

    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFotoB64() {
        return fotoB64;
    }
    public void setFotoB64(String fotoB64) {
        this.fotoB64 = fotoB64;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
