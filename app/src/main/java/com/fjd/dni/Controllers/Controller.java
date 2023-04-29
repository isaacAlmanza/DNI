package com.fjd.dni.Controllers;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.fjd.dni.BuildConfig;
import com.fjd.dni.Models.DatosDownload;
import com.fjd.dni.Models.DatosSync;
import com.fjd.dni.Models.DatosUpload;
import com.fjd.dni.Models.Datos_Upload_Prueba;
import com.fjd.dni.Models.MainJson;
import com.fjd.dni.Models.ModelSaveEncuesta;
import com.fjd.dni.Models.ProcedureUpload;
import com.fjd.dni.Models.RespuestaFotos;
import com.fjd.dni.Models.SaveDatosUpload;
import com.fjd.dni.Models.SendDatosFotos;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Controller {
    Context context;
    View layout;
    public static final int SYNC=0;
    public static final int SYNC_ORDERS=1;
    public static final int SEND=2;
    public static final int LOGOUT=3;


    public Controller(Context context, View layout) {
        this.context = context;
        this.layout = layout;
    }

    public int Execute(int method, int opcion2){
        final int[] salida = {0};
        try {
            InterfaceApi api;
            ArrayList<SaveDatosUpload> main = new ArrayList<>();
            SaveDatosUpload upload = new SaveDatosUpload();
            ModelSaveEncuesta encuesta  = new ModelSaveEncuesta();
            ProcedureUpload proce = new ProcedureUpload();
            //##################################
            proce.setProcedimiento1("WS_VERSION_APPCOMER");
            //=================================
            String version = BuildConfig.VERSION_NAME;
           // Log.e("TAG", "ValidarVersion: " + version );
            encuesta.setE_1(version);
            //##################################
            upload.setProcedure(proce);
            upload.setToken(GetSharedPreferences.getSharedData(context).get(1));
            upload.setValores(encuesta);
            //##################################
            main.add(upload);
            //##################################
            api = GetApiService.ApiService(GetSharedPreferences.getSharedData(context).get(2));
            api.getSave(main).enqueue(new Callback<DatosDownload>() {
                @Override
                public void onResponse(Call<DatosDownload> call, Response<DatosDownload> response) {
                    try {
                        DatosDownload res =  response.body();
                        if (response.isSuccessful()){
                            if (res.getResultado1().get(0).getCampo1().equalsIgnoreCase("0")){
                                ViewSnackBar.SnackBarDanger(layout,"¡UPS! "+ res.getResultado1().get(0).getCampo0() , context);
                                return;
                            }
                            if (res.getResultado1().get(0).getCampo0().equalsIgnoreCase("1")){
                                switch (method){
                                    case 0:
                                        SincronzarDatos(opcion2);
                                        break;
                                    case 1:
                                        SyncOrdenes();
                                        break;
                                    case 2:
                                        Enviar();
                                        break;
                                    case 3:
                                        CerrarSesion();
                                        break;
                                }
                            }else {
                                ViewSnackBar.SnackBarDanger(layout,res.getResultado1().get(0).getCampo1(), context);
                            }
                        }
                    }catch (Exception e){
                        Log.e("TAG", "onResponse: " + e.fillInStackTrace() );
                    }
                }

                @Override
                public void onFailure(Call<DatosDownload> call, Throwable t) {
                    ViewSnackBar.SnackBarDanger(layout, t.getMessage(), context);
                }
            });
        }catch (Exception e){

            Log.e("TAG", "GuardarDatos: " + e.fillInStackTrace() );
        }
        return salida[0];
    }

    void SincronzarDatos(int pos){
        InterfaceApi api  = GetApiService.ApiService(GetSharedPreferences.getSharedData(context).get(2));
        ArrayList<DatosUpload> main = new ArrayList<>();
        DatosUpload upload = new DatosUpload();
        DatosSync datos = new DatosSync();
        ProcedureUpload proce = new ProcedureUpload();
        proce.setProcedimiento1("WS_PARAMETROS_COLABORADOR");
        datos.setOpcion(valueOf(pos));
        // datos.setE2(GetSharedPreferences.getSharedData(context).get(0));
        upload.setProcedure(proce);
        upload.setValores(datos);
        upload.setToken(GetSharedPreferences.getSharedData(context).get(1));
        main.add(upload);
        api.getDatosUpload(main).enqueue(new Callback<DatosDownload>() {
            @SuppressLint("Range")
            @Override
            public void onResponse(@NonNull Call<DatosDownload> call, @NonNull Response<DatosDownload> response) {
               try {
                   DatosDownload res = response.body();
                   SQLiteDatabase db = Connections.Database(context);
                   if (response.isSuccessful()){
                       String s_1, s_2, s_3, s_4, s_5, s_6, s_7, s_8, s_9, s_10, s_11,s_12, verificacion;
                       switch (pos){
                           case 1:
                               db.delete("BARRIOS", null, null);
                               for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                   ContentValues values = new ContentValues();
                                   s_1 = res.getResultado1().get(i).getCampo0();
                                   s_2 = res.getResultado1().get(i).getCampo1();
                                   s_3 = res.getResultado1().get(i).getCampo2();
                                   s_4 = res.getResultado1().get(i).getCampo3();
                                   s_5 = res.getResultado1().get(i).getCampo4();
                                   values.put("DPTO",s_1);
                                   values.put("MPIO",s_2);
                                   values.put("CORREG",s_3);
                                   values.put("CODIGO",s_4);
                                   values.put("NOMBRE", s_5);
                                   db.insert("BARRIOS", null, values);
                               }
                               break;
                           case 2:
                               db.delete("CORREGIMIENTOS", null, null);
                               for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                                   ContentValues values = new ContentValues();
                                   s_1 = res.getResultado1().get(i).getCampo0();
                                   s_2 = res.getResultado1().get(i).getCampo1();
                                   s_3 = res.getResultado1().get(i).getCampo2();
                                   s_4 = res.getResultado1().get(i).getCampo3();
                                   values.put("DPTO",s_1);
                                   values.put("MPIO",s_2);
                                   values.put("CODIGO",s_3);
                                   values.put("NOMBRE",s_4);

                                   db.insert("CORREGIMIENTOS", null, values);
                               }

                               break;


                       }
                       @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                       SharedPreferences preferences =  context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
                       SharedPreferences.Editor edit =  preferences.edit();
                       edit.putString("fecha_sync", sdf.format(new Date()));
                       edit.apply();
                       if (pos==15){
                           SaveToken("stop");
                       }
                   }else {
                       SaveToken("stop");
                       String error =response.errorBody().string().replace("{\"jwt\":\"\",\"msg\":\"", "").replace("\"}", "");
                       ViewSnackBar.SnackBarDanger(layout, error.trim() , context);
                   }
                   db.close();
               }catch (Exception e){
                   Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();;
               }
            }



            @Override
            public void onFailure(@NonNull Call<DatosDownload> call, @NonNull Throwable t) {
                ViewSnackBar.SnackBarDanger(layout, t.fillInStackTrace().getMessage(),context);
                SaveToken("stop");
            }

        });


    }

    void SyncOrdenes(){
        InterfaceApi api;
        SaveToken("allow");
        api  = GetApiService.ApiService(GetSharedPreferences.getSharedData(context).get(2));
        ArrayList<DatosUpload> main = new ArrayList<>();
        DatosUpload upload = new DatosUpload();
        ProcedureUpload proce = new ProcedureUpload();
        DatosSync datosSync = new DatosSync();
        datosSync.setOpcion(GetSharedPreferences.getSharedData(context).get(0));
        proce.setProcedimiento1("WS_SINCRO_ORDEN_COMER");
        upload.setProcedure(proce);
        upload.setValores(datosSync);
        upload.setToken(GetSharedPreferences.getSharedData(context).get(1));
        main.add(upload);
        Callback<DatosDownload> callback = new Callback<DatosDownload>() {
            @Override
            public void onResponse(@NonNull Call<DatosDownload> call, @NonNull Response<DatosDownload> response) {

                if (response.isSuccessful()){
                    String s_1, s_2, s_3, s_4, s_5, s_6,s_7, s_8, s_9, s_10, s_11, s_12, s_13, s_14, s_15,
                            s_16, s_17, s_18, s_19, s_20,s_21, s_22, s_23, s_24, s_25, s_26, s_27, s_28, s_29,
                            s_30, s_31, s_32,  s_33, s_34, s_35,s_36,s_37, s_38, s_39, s_40, s_41, s_42, s_43, s_44, s_45,
                            s_46, s_47, s_48, s_49, s_50, s_51,s_52, s_53, s_54, s_55, s_56, s_57, s_58, s_59, s_60;
                    DatosDownload res = response.body();
                    SQLiteDatabase db = Connections.Database(context);
                    db.delete("ORDENES", null, null);
                    SaveToken("allow");
                    if (res.getResultado1().size()==0){
                        ViewSnackBar.SnackBarDanger(layout,"NO EXISTEN DATOS" , context);
                        SaveToken("stop");
                        return;
                    }
                    for (int i = 0; i < Objects.requireNonNull(res).getResultado1().size(); i++) {
                        ContentValues values = new ContentValues();
                        s_1 = res.getResultado1().get(i).getCampo0();
                        s_2 = res.getResultado1().get(i).getCampo1();
                        s_3 = res.getResultado1().get(i).getCampo2();
                        s_4 = res.getResultado1().get(i).getCampo3();
                        s_5 = res.getResultado1().get(i).getCampo4();
                        s_6 = res.getResultado1().get(i).getCampo5();
                        s_35 = res.getResultado1().get(i).getCampo34();
                        s_8 = res.getResultado1().get(i).getCampo7();
                        s_9 = res.getResultado1().get(i).getCampo8();
                        s_10 = res.getResultado1().get(i).getCampo9();

                        values.put("ORDEN", s_1);
                        values.put("INFO", s_35);
                        values.put("TIPO_ORDEN", s_3);
                        values.put("DESCRIPCION", s_4);
                        values.put("ENCUESTA", s_5);
                        values.put("NOMBRE_CLIENTE", "null");
                        values.put("CEDULA", "null");
                        values.put("TELEFONO", "null");
                        values.put("EMAIL", "null");
                        values.put("TIPO_HABITANTE", "null");
                        values.put("FECHA_VISITA", "null");
                        values.put("VALOR", "null");
                        values.put("PUNTO_PAGO", "null");
                        values.put("OBSERVACION", "null");
                        values.put("LONGITUD", "0.0");
                        values.put("LATITUD", "0.0");
                        values.put("USER", "null");
                        values.put("CUMPLIDA", "N");
                        values.put("ESTADO", "N");



                        db.insert("ORDENES", null, values);
                        if (i== res.getResultado1().size()-1){
                            Log.e("TAG", "onResponse: final  " + i + " - " + (res.getResultado1().size()-1) );
                            SaveToken("stop");
                        }
                    }
                    db.close();

                }else {
                    try {
                        String error =response.errorBody().string().replace("{\"jwt\":\"\",\"msg\":\"", "").replace("\"}", "");
                        ViewSnackBar.SnackBarDanger(layout, error.trim() , context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DatosDownload> call, @NonNull Throwable t) {

            }
        };
        api.getDatosUpload(main).enqueue(callback);

    }
    @SuppressLint("Range")
    void SendResponseOffline(ArrayList<MainJson> arrayListjson){
        String TAG ="ertertert";
        SaveToken("allow");
        InterfaceApi api;
        api = GetApiService.ApiService(GetSharedPreferences.getSharedData(context).get(2));
        api.getPrueba(arrayListjson).enqueue(new Callback<DatosDownload>() {
            @Override
            public void onResponse(Call<DatosDownload> call, Response<DatosDownload> response) {
                if (response.isSuccessful()){
                    DatosDownload  res = response.body();
                    SQLiteDatabase db  = Connections.Database(context);
                    ContentValues  values =  new ContentValues();
                    values.put("ESTADO", "S");
                    Log.e(TAG, "onResponse: "+ arrayListjson.get(0).getDatos().getValores().get(0).getE_2() );
                    if (res.getResultado1().get(0).getCampo1().equalsIgnoreCase("1")){
                        ViewSnackBar.SnackBarSuccess(layout, res.getResultado1().get(0).getCampo0(), context);
                        long ordenes=  db.update("PERSONAS", values, "CEDULA=?", new String[]{arrayListjson.get(0).getDatos().getValores().get(0).getE_1()});
                        if (ordenes>0){
                            Log.e(TAG, "onResponse: Actualiza Orden"  );
                        }
                        Enviar();
                        db.close();
                    }else {
                        ContentValues  values1 =  new ContentValues();
                        values1.put("ESTADO", res.getResultado1().get(0).getCampo0());
                        long ordenes=  db.update("PERSONAS", values1, "CEDULA=?", new String[]{arrayListjson.get(0).getDatos().getValores().get(0).getE_1()});
                        if (ordenes>0){
                            Log.e(TAG, "onResponse: Actualiza Orden"  );
                            Enviar();
                        }
                        ViewSnackBar.SnackBarDanger(layout, res.getResultado1().get(0).getCampo0(), context);
                    }

                }
            }

            @Override
            public void onFailure(Call<DatosDownload> call, Throwable t) {
                SaveToken("stop");
            }
        });
    }
    @SuppressLint("Range")
    void SendResponseOnline(ArrayList<MainJson> arrayListjson){
        String TAG ="ertertert";
        SaveToken("allow");
        InterfaceApi api;
        api = GetApiService.ApiService(GetSharedPreferences.getSharedData(context).get(2));
        api.getPrueba(arrayListjson).enqueue(new Callback<DatosDownload>() {
            @Override
            public void onResponse(Call<DatosDownload> call, Response<DatosDownload> response) {
                if (response.isSuccessful()){
                    DatosDownload  res = response.body();
                    SQLiteDatabase db  = Connections.Database(context);
                    ContentValues  values =  new ContentValues();
                    values.put("ESTADO", "S");
                    Log.e(TAG, "onResponse: "+ arrayListjson.get(0).getDatos().getValores().get(0).getE_2() );
                    if (res.getResultado1().get(0).getCampo1().equalsIgnoreCase("1")){
                        ViewSnackBar.SnackBarSuccess(layout, res.getResultado1().get(0).getCampo0(), context);
                        long ordenes=  db.update("PERSONAS", values, "CEDULA=?", new String[]{arrayListjson.get(0).getDatos().getValores().get(0).getE_1()});
                        if (ordenes>0){
                            Log.e(TAG, "onResponse: Actualiza Orden"  );
                        }
                        Enviar();
                        db.close();
                    }else {
                        ContentValues  values1 =  new ContentValues();
                        values1.put("ESTADO", res.getResultado1().get(0).getCampo0());
                        long ordenes=  db.update("PERSONAS", values1, "CEDULA=?", new String[]{arrayListjson.get(0).getDatos().getValores().get(0).getE_1()});
                        if (ordenes>0){
                            Log.e(TAG, "onResponse: Actualiza Orden"  );
                            Enviar();
                        }
                        ViewSnackBar.SnackBarDanger(layout, res.getResultado1().get(0).getCampo0(), context);
                    }

                }
            }

            @Override
            public void onFailure(Call<DatosDownload> call, Throwable t) {
                SaveToken("stop");
            }
        });
    }
     void SendFotos(@NonNull SendDatosFotos upload , String tabla) {
        InterfaceApi api;
        ArrayList<SendDatosFotos> main = new ArrayList<>();
        SaveToken("allow");
        upload.setToken(GetSharedPreferences.getSharedData(context.getApplicationContext()).get(1));
        //##################################
        main.add(upload);
        //##################################
        api = GetApiService.ApiService(GetSharedPreferences.getSharedData(context.getApplicationContext()).get(2));
        api.getSendFoto(main).enqueue(new Callback<RespuestaFotos>() {
            @Override
            public void onResponse(Call<RespuestaFotos> call, Response<RespuestaFotos> response) {
                try {
                    RespuestaFotos res =  response.body();
                    if (response.isSuccessful()){
                        if (res.getS_2().equalsIgnoreCase("1")){
                            ContentValues cv;
                            SQLiteDatabase db = Connections.Database(context);
                            cv = new ContentValues();
                            cv.put("ESTADO", "S");
                            long resp = db.update(tabla, cv,"ID_FOTO=? ",
                                    new String[]{upload.getId()} );
                            if (resp>0){
                                Log.e("TAG", "EnviasrFotos: Actualiza estado");
                            }
                            db.close();
                            ViewSnackBar.SnackBarSuccess(layout, res.getS_1(), context);
                            EnviarFotos();
                        }else {

                            if (res.getS_1().equalsIgnoreCase("Registro ya existe!")){
                                ContentValues cv;
                                SQLiteDatabase db = Connections.Database(context);
                                cv = new ContentValues();
                                cv.put("ESTADO",res.getS_1() );
                                long resp = db.update(tabla, cv,"ID_FOTO=?",
                                        new String[]{upload.getId()} );
                                if (resp>0){
                                    Log.e("TAG", "EnviasrFotos: Actualiza estado");
                                }
                                db.close();
                                EnviarFotos();
                            }
                            ViewSnackBar.SnackBarDanger(layout, res.getS_1(), context);

                        }
                    }else {
                        SaveToken("stop");
                        String error =response.errorBody().string().replace("{\"jwt\":\"\",\"msg\":\"", "").replace("\"}", "");
                        ViewSnackBar.SnackBarDanger(layout, error.trim() , context);
                    }
                }catch (Exception e){
                    Log.e("TAG", "onResponse: "+ e.fillInStackTrace() );
                }
            }

            @Override
            public void onFailure(Call<RespuestaFotos> call, Throwable t) {
                ViewSnackBar.SnackBarDanger(layout, t.getMessage() , context);
                SaveToken("stop");
            }
        });
    }

    void CerrarSesion() {
        ModelSaveEncuesta encuesta  = new ModelSaveEncuesta();
        encuesta.setE_1(GetSharedPreferences.getSharedData(context).get(0));
        encuesta.setE_2(GetSharedPreferences.getSharedData(context).get(4));
        final InterfaceApi[] api = new InterfaceApi[1];
        ArrayList<SaveDatosUpload> main = new ArrayList<>();
        SaveDatosUpload upload = new SaveDatosUpload();
        ProcedureUpload proce = new ProcedureUpload();
        //##################################
        proce.setProcedimiento1("WS_CERRAR_SESION_APK");
        //=================================
        //##################################
        upload.setProcedure(proce);
        upload.setValores(encuesta);
        upload.setToken(GetSharedPreferences.getSharedData(context).get(1));
        //##################################
        main.add(upload);
        //##################################
        api[0] = GetApiService.ApiService(GetSharedPreferences.getSharedData(context.getApplicationContext()).get(2));
        api[0].getSave(main).enqueue(new Callback<DatosDownload>()  {
            @Override
            public void onResponse(Call<DatosDownload> call, Response<DatosDownload> response) {
                DatosDownload res = response.body();;
                if (response.isSuccessful()){
                    if (res.getResultado1().get(0).getCampo1().equalsIgnoreCase("1")){
                        boolean delete =  context.deleteDatabase(GetSharedPreferences.getSharedData(context).get(0)+".db");
                        if (delete){
                            Log.e("TAG", "onResponse: eliminar" + delete );
                        }else {
                            Log.e("TAG", "onResponse: Error" + delete );
                        }
                        ViewSnackBar.SnackBarSuccess(layout,res.getResultado1().get(0).getCampo0(), context);
                        SharedPreferences preferences = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.clear();
                        edit.apply();
                        edit.commit();

                        ((Activity)context).finish();
                    }else {
                        ViewSnackBar.SnackBarDanger(layout,res.getResultado1().get(0).getCampo0(), context);
                    }
                }
            }

            @Override
            public void onFailure(Call<DatosDownload> call, Throwable t) {
                ViewSnackBar.SnackBarDanger(layout,t.getMessage(), context);
            }
        });



    }
    @SuppressLint("Range")
    void Enviar(){

        try {
            SQLiteDatabase db = Connections.Database(context);
            ModelSaveEncuesta encuesta;
            MainJson json;
            ArrayList<MainJson> arrayListjson =  new ArrayList<>();
            Datos_Upload_Prueba upload;
            ArrayList<ModelSaveEncuesta> lista_data;
            Cursor ordenes =  db.rawQuery("SELECT * FROM PERSONAS WHERE ESTADO ='N' ",null);
            if (ordenes.getCount()>0){
                ordenes.moveToPosition(0);
                lista_data = new ArrayList<>();
                encuesta = new ModelSaveEncuesta();
                upload = new Datos_Upload_Prueba();
                encuesta.setE_1(ordenes.getString(ordenes.getColumnIndex("CEDULA"))==null?"null":ordenes.getString(ordenes.getColumnIndex("CEDULA")));
                encuesta.setE_2(ordenes.getString(ordenes.getColumnIndex("NOMBRES"))==null?"null":ordenes.getString(ordenes.getColumnIndex("NOMBRES")));
                encuesta.setE_3(ordenes.getString(ordenes.getColumnIndex("APELLIDOS"))==null?"null":ordenes.getString(ordenes.getColumnIndex("APELLIDOS")));
                encuesta.setE_4(ordenes.getString(ordenes.getColumnIndex("FECHA_NAC"))==null?"null":ordenes.getString(ordenes.getColumnIndex("FECHA_NAC")));
                encuesta.setE_5(ordenes.getString(ordenes.getColumnIndex("FECHA_EXP"))==null?"null":ordenes.getString(ordenes.getColumnIndex("FECHA_EXP")));
                encuesta.setE_6(ordenes.getString(ordenes.getColumnIndex("LUGAR_NAC"))==null?"null":ordenes.getString(ordenes.getColumnIndex("LUGAR_NAC")));
                encuesta.setE_7(ordenes.getString(ordenes.getColumnIndex("SEXO"))==null?"null":ordenes.getString(ordenes.getColumnIndex("SEXO")));
                encuesta.setE_8(ordenes.getString(ordenes.getColumnIndex("TELEFONO"))==null?"null":ordenes.getString(ordenes.getColumnIndex("TELEFONO")));
                encuesta.setE_9(ordenes.getString(ordenes.getColumnIndex("EMAIL"))==null?"null":ordenes.getString(ordenes.getColumnIndex("EMAIL")));
                encuesta.setE_10(ordenes.getString(ordenes.getColumnIndex("LATITUD"))==null?"null":ordenes.getString(ordenes.getColumnIndex("LATITUD")));
                encuesta.setE_11(ordenes.getString(ordenes.getColumnIndex("LONGITUD"))==null?"null":ordenes.getString(ordenes.getColumnIndex("LONGITUD")));
                encuesta.setE_12(ordenes.getString(ordenes.getColumnIndex("USER"))==null?"null":ordenes.getString(ordenes.getColumnIndex("USER")));
                encuesta.setE_13(ordenes.getString(ordenes.getColumnIndex("FECHA_EJE"))==null?"null":ordenes.getString(ordenes.getColumnIndex("FECHA_EJE")));
                encuesta.setE_14(ordenes.getString(ordenes.getColumnIndex("HORA_EJE"))==null?"null":ordenes.getString(ordenes.getColumnIndex("HORA_EJE")));
                encuesta.setE_15(ordenes.getString(ordenes.getColumnIndex("CORREG"))==null?"null":ordenes.getString(ordenes.getColumnIndex("CORREG")));
                encuesta.setE_16(ordenes.getString(ordenes.getColumnIndex("BARRIO"))==null?"null":ordenes.getString(ordenes.getColumnIndex("BARRIO")));


                lista_data.add(encuesta);
                upload.setProcedure("WS_DATOS_COLABORADOR");
                upload.setValores(lista_data);
                json = new MainJson();
                json.setDatos(upload);
                json.setToken(GetSharedPreferences.getSharedData(context).get(1));
                arrayListjson.add(json);
                SendResponseOffline( arrayListjson);

            }else {
                db.close();
                ordenes.close();
                EnviarFotos();
            }

        }catch (Exception e){
            Log.e("TAG", "Enviar: " + e.fillInStackTrace() );
        }


    }
    @SuppressLint("Range")
    void EnviarFotos() {
        try{
        Log.e("TAG", "EnviarFotos: Entra" );
        SendDatosFotos fotos;

        SQLiteDatabase db = Connections.Database(context);
        Cursor cursor = db.rawQuery("SELECT * FROM FOTOS WHERE ESTADO = 'N'", null);
        Log.e("TAG", "EnviarFotos: "  +cursor.getCount() );
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            fotos = new SendDatosFotos();
            fotos.setId(cursor.getString(cursor.getColumnIndex("ID_FOTO")));
            fotos.setCuenta(cursor.getString(cursor.getColumnIndex("ID_FOTO")));
            fotos.setNumero(cursor.getString(cursor.getColumnIndex("N_FOTOS")));
            fotos.setAnio(cursor.getString(cursor.getColumnIndex("ANO")));
            fotos.setMes(cursor.getString(cursor.getColumnIndex("MES")));
            fotos.setFotoB64(cursor.getString(cursor.getColumnIndex("FOTO")));
            fotos.setProcedimiento("FOTO_VISITAS_CREA");
            fotos.setToken(GetSharedPreferences.getSharedData(context).get(1));
            SendFotos( fotos,  "FOTOS");
        }else {
            SaveToken("stop");
        }
        cursor.close();
        db.close();

       }catch (Exception e){
           Log.e("TAG", "EnviarFotos: " + e.fillInStackTrace() );
       }
    }

    public void SaveToken(String token){
        SharedPreferences preferences = context.getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor edit =  preferences.edit();
        edit.putString("progress", token);
        edit.apply();
        edit.commit();
    }


}
