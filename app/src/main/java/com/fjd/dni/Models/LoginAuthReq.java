package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class LoginAuthReq {

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("cedula")
    private String cedula;

    @SerializedName("imei")
    private String imei;

    public LoginAuthReq(String codigo, String cedula, String imei) {
        this.codigo = codigo;
        this.cedula = cedula;
        this.imei = imei;
    }
    public LoginAuthReq(){

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
