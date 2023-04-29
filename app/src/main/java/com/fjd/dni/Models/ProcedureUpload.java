package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class ProcedureUpload {

    @SerializedName("procedimiento1")
    private  String procedimiento1;

    @SerializedName("procedimiento2")
    private  String mpio;

    @SerializedName("procedimiento3")
    private  String barrio;


    public ProcedureUpload(String procedimiento1, String mpio, String barrio) {
        this.procedimiento1 = procedimiento1;
        this.mpio = mpio;
        this.barrio = barrio;
    }
    public ProcedureUpload(){

    }
    public String getProcedimiento1() {
        return procedimiento1;
    }

    public void setProcedimiento1(String procedimiento1) {
        this.procedimiento1 = procedimiento1;
    }

    public String getMpio() {
        return mpio;
    }

    public void setMpio(String mpio) {
        this.mpio = mpio;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}
