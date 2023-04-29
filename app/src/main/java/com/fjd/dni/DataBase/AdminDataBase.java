package com.fjd.dni.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AdminDataBase extends SQLiteOpenHelper {

    Context context;
    public AdminDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,  name+".db", factory, version);
        this.context =context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      try {
          db.execSQL(Tables.PERSONAS);
          db.execSQL(Tables.FOTOS);
          db.execSQL(Tables.BARRIOS);
          db.execSQL(Tables.CORREG);
          db.execSQL(Tables.ZONA);
      }catch (Exception e){
          Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
