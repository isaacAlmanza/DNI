package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class DatosSync {


    @SerializedName("1")
    private String opcion;

    @SerializedName("2")
    private String e1;

    @SerializedName("3")
    private String e2;

    @SerializedName("4")
    private String e3;

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getE1() {
        return e1;
    }

    public void setE1(String e1) {
        this.e1 = e1;
    }

    public String getE2() {
        return e2;
    }

    public void setE2(String e2) {
        this.e2 = e2;
    }

    public String getE3() {
        return e3;
    }

    public void setE3(String e3) {
        this.e3 = e3;
    }
}
