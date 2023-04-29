package com.fjd.dni.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fjd.dni.DataBase.AdminDataBase;


public interface Connections {

    static SQLiteDatabase Database(Context context){
        SQLiteDatabase db;
          final String path = GetSharedPreferences.getSharedData(context).get(0);
            AdminDataBase adminDB = new AdminDataBase(context,path , null, 1);
            db = adminDB.getWritableDatabase();


        return db;
    }
    static boolean ConexionInternet(Context context){
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  conn.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
