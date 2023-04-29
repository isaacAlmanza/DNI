package com.fjd.dni.Controllers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataOrder {

    public static int setInfoOrden(Context context, String key){
        int valor = 0;
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            SharedPreferences.Editor edit =  preferences.edit();
            edit.putString("infoorden", key);
            edit.apply();

        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
    public static String getInfoOrden(Context context){
        String valor = "";
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            valor =preferences.getString("infoorden", "0");
        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
    public static int setOrden(Context context, String key){
        int valor = 0;
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            SharedPreferences.Editor edit =  preferences.edit();
            edit.putString("orden", key);
            edit.apply();

        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
    public static int setTipoOrden(Context context, String key){
        int valor = 0;
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            SharedPreferences.Editor edit =  preferences.edit();
            edit.putString("tipo_orden", key);
            edit.apply();

        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
    public static String getOrden(Context context){
        String valor = "";
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            valor =preferences.getString("orden", "");
        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
    public static String getTipoOrden(Context context){
        String valor = "";
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            valor =preferences.getString("tipo_orden", "");
        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
    public static void setRequireCliente(Context context, int key){
        int valor = 0;
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            SharedPreferences.Editor edit =  preferences.edit();
            edit.putInt("cliente", key);
            edit.apply();

        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
    }
    public static int getRequireCliente(Context context){
        int valor = 0;
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            valor =preferences.getInt("cliente", 0);
        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
    public static void setRequireFoto(Context context, int key){
        int valor = 0;
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            SharedPreferences.Editor edit =  preferences.edit();
            edit.putInt("foto", key);
            edit.apply();

        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
    }
    public static int getRequireFoto(Context context){
        int valor = 0;
        try {
            SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
            valor =preferences.getInt("foto", 0);
        }catch (Exception e){
            Log.e("TAG", "DESCRR: "+ e.fillInStackTrace());
        }
        return valor;
    }
}
