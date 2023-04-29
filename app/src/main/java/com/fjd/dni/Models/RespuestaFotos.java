package com.fjd.dni.Models;

import com.google.gson.annotations.SerializedName;

public class RespuestaFotos {
    @SerializedName("S_1")
    private  String s_1;

    @SerializedName("S_2")
    private  String s_2;

    public String getS_1() {
        return s_1;
    }

    public void setS_1(String s_1) {
        this.s_1 = s_1;
    }

    public String getS_2() {
        return s_2;
    }

    public void setS_2(String s_2) {
        this.s_2 = s_2;
    }
}
