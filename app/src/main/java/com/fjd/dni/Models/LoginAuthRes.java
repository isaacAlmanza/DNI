package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class LoginAuthRes {

    @SerializedName("jwt")
    private String token;

    @SerializedName("msg")
    private String msgError;

    @SerializedName("TE_NOMBRES")
    private String salida;

    @SerializedName("TE_CEDULA")
    private String te_cedula;

    @SerializedName("TE_CODIGO")
    private String te_codigo;

    @SerializedName("URL")
    private String url;

    @SerializedName("usuario")
    private String usu_movil;

    @SerializedName("CLAVE_MOVIL")
    private String clave_m;

    @SerializedName("NOMBRE")
    private String nombre;

    @SerializedName("S_COLOR1_R")
    private String color1_r;

    @SerializedName("S_COLOR1_G")
    private String color1_g;

    @SerializedName("S_COLOR1_B")
    private String color1_b;

    @SerializedName("S_COLOR2_R")
    private String color2_r;

    @SerializedName("S_COLOR2_B")
    private String color2_b;

    @SerializedName("S_COLOR2_G")
    private String color2_g;

    @SerializedName("S_ETIQUETA_SERIE")
    private String etiqueta;

    @SerializedName("S_APLICA_PREFIJO")
    private String  apli_prefijo;

    public LoginAuthRes(String token, String msgError, String salida, String te_cedula, String te_codigo,
                        String url, String usu_movil, String clave_m, String nombre, String color1_r,
                        String color1_g, String color1_b, String color2_r, String color2_b, String color2_g,
                        String etiqueta, String apli_prefijo) {
        this.token = token;
        this.msgError = msgError;
        this.salida = salida;
        this.te_cedula = te_cedula;
        this.te_codigo = te_codigo;
        this.url = url;
        this.usu_movil = usu_movil;
        this.clave_m = clave_m;
        this.nombre = nombre;
        this.color1_r = color1_r;
        this.color1_g = color1_g;
        this.color1_b = color1_b;
        this.color2_r = color2_r;
        this.color2_b = color2_b;
        this.color2_g = color2_g;
        this.etiqueta = etiqueta;
        this.apli_prefijo = apli_prefijo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getTe_cedula() {
        return te_cedula;
    }

    public void setTe_cedula(String te_cedula) {
        this.te_cedula = te_cedula;
    }

    public String getTe_codigo() {
        return te_codigo;
    }

    public void setTe_codigo(String te_codigo) {
        this.te_codigo = te_codigo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsu_movil() {
        return usu_movil;
    }

    public void setUsu_movil(String usu_movil) {
        this.usu_movil = usu_movil;
    }

    public String getClave_m() {
        return clave_m;
    }

    public void setClave_m(String clave_m) {
        this.clave_m = clave_m;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor1_r() {
        return color1_r;
    }

    public void setColor1_r(String color1_r) {
        this.color1_r = color1_r;
    }

    public String getColor1_g() {
        return color1_g;
    }

    public void setColor1_g(String color1_g) {
        this.color1_g = color1_g;
    }

    public String getColor1_b() {
        return color1_b;
    }

    public void setColor1_b(String color1_b) {
        this.color1_b = color1_b;
    }

    public String getColor2_r() {
        return color2_r;
    }

    public void setColor2_r(String color2_r) {
        this.color2_r = color2_r;
    }

    public String getColor2_b() {
        return color2_b;
    }

    public void setColor2_b(String color2_b) {
        this.color2_b = color2_b;
    }

    public String getColor2_g() {
        return color2_g;
    }

    public void setColor2_g(String color2_g) {
        this.color2_g = color2_g;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getApli_prefijo() {
        return apli_prefijo;
    }

    public void setApli_prefijo(String apli_prefijo) {
        this.apli_prefijo = apli_prefijo;
    }
}
