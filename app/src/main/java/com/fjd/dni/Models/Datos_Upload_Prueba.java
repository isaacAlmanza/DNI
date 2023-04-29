package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Datos_Upload_Prueba {

    @SerializedName("procedimiento")
    private String procedure;

    @SerializedName("valores")
    private ArrayList<ModelSaveEncuesta>  valores;


    public Datos_Upload_Prueba(String procedure, ArrayList<ModelSaveEncuesta> valores) {
        this.procedure = procedure;
        this.valores = valores;

    }
    public Datos_Upload_Prueba(){

    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public ArrayList<ModelSaveEncuesta> getValores() {
        return valores;
    }

    public void setValores(ArrayList<ModelSaveEncuesta> valores) {
        this.valores = valores;
    }
}



