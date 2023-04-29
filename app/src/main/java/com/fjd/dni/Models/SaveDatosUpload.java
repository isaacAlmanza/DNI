package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class SaveDatosUpload {
    @SerializedName("procedimientos")
    private ProcedureUpload procedure;

    @SerializedName("valores")
    private ModelSaveEncuesta  valores;

    @SerializedName("token")
    private String token;

    public SaveDatosUpload(ProcedureUpload procedure, ModelSaveEncuesta valores, String token) {
        this.procedure = procedure;
        this.valores = valores;
        this.token = token;
    }
    public SaveDatosUpload(){

    }

    public ProcedureUpload getProcedure() {
        return procedure;
    }

    public void setProcedure(ProcedureUpload procedure) {
        this.procedure = procedure;
    }

    public ModelSaveEncuesta getValores() {
        return valores;
    }

    public void setValores(ModelSaveEncuesta valores) {
        this.valores = valores;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
