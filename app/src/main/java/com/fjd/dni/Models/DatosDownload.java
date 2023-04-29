package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DatosDownload {

    @SerializedName("resultado1")
    private ArrayList<CamposR_1> resultado1;

    @SerializedName("resultado2")
    private ArrayList<CamposR_1> resultado2;

    @SerializedName("resultado3")
    private ArrayList<CamposR_1> resultado3;

    public ArrayList<CamposR_1> getResultado1() {
        return resultado1;
    }

    public void setResultado1(ArrayList<CamposR_1> resultado) {
        this.resultado1 = resultado;
    }

    public ArrayList<CamposR_1> getResultado2() {
        return resultado2;
    }

    public void setResultado2(ArrayList<CamposR_1> resultado2) {
        this.resultado2 = resultado2;
    }

    public ArrayList<CamposR_1> getResultado3() {
        return resultado3;
    }

    public void setResultado3(ArrayList<CamposR_1> resultado3) {
        this.resultado3 = resultado3;
    }


}
