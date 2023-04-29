package com.fjd.dni.Controllers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public interface GetSharedPreferences {
    static ArrayList<String> getSharedData(Context context){
        SharedPreferences preferences =context.getSharedPreferences("LOGIN",context.MODE_PRIVATE);
        String token =  preferences.getString("TOKEN","");
        String user =   preferences.getString(String.valueOf("user"),"");
        String url = preferences.getString("url", "");
        String imei = preferences.getString("imei", "");
        String sync = preferences.getString("d_user", "");
        String cedula = preferences.getString("cedula", "");
        ArrayList<String> list =  new ArrayList<>();
        list.add(0, user);
        list.add(1,token);
        list.add(2, url);
        list.add(3, imei);
        list.add(4, cedula);
        list.add(5, sync);




        return  list;
    }
    static ArrayList<String> getSharedEncuesta(Context context){
        SharedPreferences preferences =context.getSharedPreferences("ENCUESTA",context.MODE_PRIVATE);
        String idOrden = preferences.getString("idOrden", "");
        String fecha = preferences.getString("fecha_eje", "");
        String hora = preferences.getString("hora_eje", "");
        String categoria = preferences.getString("categoria", "");
        String titulo = preferences.getString("titulo", "");
        ArrayList<String> list =  new ArrayList<>();
        list.add(0, idOrden);
        list.add(1, fecha);
        list.add(2, hora);
        list.add(3, categoria);
        list.add(4, titulo);
        return  list;
    }

}
