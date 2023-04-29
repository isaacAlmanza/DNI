package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class DatosUpload {

    @SerializedName("procedimientos")
    private ProcedureUpload procedure;

    @SerializedName("valores")
    private DatosSync  valores;

    @SerializedName("token")
    private String token;

    public DatosUpload(ProcedureUpload procedure, DatosSync valores, String token) {
        this.procedure = procedure;
        this.valores = valores;
        this.token = token;
    }
    public DatosUpload(){

    }

    public ProcedureUpload getProcedure() {
        return procedure;
    }

    public void setProcedure(ProcedureUpload procedure) {
        this.procedure = procedure;
    }

    public DatosSync getValores() {
        return valores;
    }

    public void setValores(DatosSync valores) {
        this.valores = valores;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
