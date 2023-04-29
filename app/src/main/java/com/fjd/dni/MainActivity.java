package com.fjd.dni;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.biometrics.BiometricPrompt;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fjd.dni.Controllers.Connections;
import com.fjd.dni.Controllers.Controller;
import com.fjd.dni.Controllers.GetSharedPreferences;
import com.fjd.dni.Controllers.SelectComponent;
import com.fjd.dni.Controllers.ViewSnackBar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.microblink.blinkid.MicroblinkSDK;
import com.microblink.blinkid.activity.result.ResultStatus;
import com.microblink.blinkid.activity.result.contract.TwoSideDocumentScan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST = 123187;
    Button scan, save;
    EditText et_nombres, et_documento, et_email, et_telefono;
    TextView tv_correg, tv_barrio;
    String nombres, apellidos, email, tel, edad, sexo, fecha_nac, lugar_nac, documento, fecha_exp, USER, barrio, correg;
    SimpleDateFormat dateFormat, horaformat;
    FrameLayout layout;
    double latitud, longitud;
    private String TAG  = "Error_scan";
    Controller controller;

    private FusedLocationProviderClient fusedLocationClient;
    private int contador =1;
    private String last="";
    KeyGenerator keyGenerator;
    SecretKey secretKey;

    @SuppressLint({"WrongThread", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Views();
        getTexts();
        controller =  new Controller(this, layout);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        MicroblinkSDK.setLicenseKey(BuildConfig.API_KEY,this);
        EventsButtons();


    }
    void Views(){
        scan = findViewById(R.id.scan_dni);
        layout = findViewById(R.id.layout_inicio);
        et_nombres = findViewById(R.id.et_nombre);
        tv_barrio = findViewById(R.id.tv_barrio);
        tv_correg = findViewById(R.id.tv_correg);
        et_documento = findViewById(R.id.et_documento);
        et_email = findViewById(R.id.et_email);
        save =  findViewById(R.id.btn_guardar);
        et_telefono = findViewById(R.id.et_telefono);
    }
    void EventsButtons(){
        scan.setOnClickListener(v->startScanning());
        save.setOnClickListener(v->CapturarHuella());
        tv_barrio.setOnClickListener(v->{
            SelectComponent.Select(this, tv_barrio, "BARRIOS", "CODIGO", "NOMBRE");
        });
        tv_correg.setOnClickListener(v->{
            setCoordenadasInit();
            SelectComponent.Select(this, tv_correg, "CORREGIMIENTOS", "CODIGO", "NOMBRE");
        });
    }
    void Clean(){
        et_telefono.setText(null);
        et_email.setText(null);
        et_nombres.setText(null);
        et_documento.setText(null);
        tv_correg.setText(getText(R.string.seleccione_departamento));
        tv_correg.setTag(null);
        tv_barrio.setText(getText(R.string.seleccione_barrio));
        tv_barrio.setTag(null);
        nombres = "";
        apellidos = "";
        email = "";
        tel  = "";
        edad  = "";
        sexo  = "";
        fecha_exp = "";;
        fecha_nac  = "";
        lugar_nac  = "";
        documento  = "";
    }
    // method within MyActivity from previous step
    void getTexts(){
        dateFormat = new SimpleDateFormat("MM/dd/yy");
        horaformat = new SimpleDateFormat("HH:mm:ss");
        USER = GetSharedPreferences.getSharedData(this).get(0);
        tel =  et_telefono.getText().toString().trim();
        tel = tel.isEmpty()?"0":tel;
        email =  et_email.getText().toString().trim();
        email =  email.isEmpty()?"null":email;
        barrio = String.valueOf(tv_barrio.getTag());
        correg = String.valueOf(tv_correg.getTag());

    }
    public void startScanning() {
        // Start scanning
        resultLauncher.launch(null);
    }


    @SuppressLint("SetTextI18n")
    ActivityResultLauncher<Void> resultLauncher = registerForActivityResult(new TwoSideDocumentScan(), twoSideScanResult -> {
                try {
                    ResultStatus resultScanStatus = twoSideScanResult.getResultStatus();
                    if (resultScanStatus == ResultStatus.FINISHED) {
                        // code after a successful scan
                        // use result.getResult() for fetching results, for example:
                        nombres = twoSideScanResult.getResult().getFirstName().value();
                        apellidos  = twoSideScanResult.getResult().getLastName().value();
                        documento = twoSideScanResult.getResult().getDocumentNumber().value();
                        edad = String.valueOf(twoSideScanResult.getResult().getAge());
                        sexo = twoSideScanResult.getResult().getSex().value();
                        fecha_nac =twoSideScanResult.getResult().getDateOfBirth().getDate().getMonth()+"/"+
                                twoSideScanResult.getResult().getDateOfBirth().getDate().getDay()+"/"+
                                twoSideScanResult.getResult().getDateOfBirth().getDate().getYear();
                        lugar_nac =  twoSideScanResult.getResult().getPlaceOfBirth().value();
                        lugar_nac.replace("\n", "\t");
                        fecha_exp =twoSideScanResult.getResult().getDateOfIssue().getDate().getMonth()+"/"+
                                twoSideScanResult.getResult().getDateOfIssue().getDate().getDay()+"/"+
                                twoSideScanResult.getResult().getDateOfIssue().getDate().getYear();
                        et_nombres.setText(nombres+"\t"+apellidos);
                        et_documento.setText(documento);
                        et_documento.setEnabled(false);
                        et_nombres.setEnabled(false);
                    } else if (resultScanStatus == ResultStatus.CANCELLED) {
                        // code after a cancelled scan
                    } else if (resultScanStatus == ResultStatus.EXCEPTION) {
                        // code after a failed scan
                        Toast.makeText(this, ResultStatus.EXCEPTION.toString(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.e("TAG", "Error: " +e.fillInStackTrace() );
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return super.onCreateOptionsMenu(menu);
    }

       @SuppressLint({"NonConstantResourceId", "Range"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pendientes:
                startActivity( new Intent(this, PendientesActivity.class));
                break;
            case R.id.send:
                ExecuteEnviar();
                break;
            case R.id.sync:
                if (!Connections.ConexionInternet(this)){
                    ViewSnackBar.SnackBarDanger(layout, getString(R.string.offline), this);
                }else {
                    new BackGroundParams().execute();
                }
                break;
            case R.id.logout:
                DialogLogOut();
                break;
            case R.id.action_camera:
                //CapturarFoto();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    void ExecuteEnviar(){
        SQLiteDatabase db  =Connections.Database(this);
        Cursor personas =  db.rawQuery("SELECT * FROM PERSONAS WHERE ESTADO = 'N'", null);
        Cursor fotos =  db.rawQuery("SELECT * FROM FOTOS WHERE ESTADO = 'N'", null);
        if ( personas.getCount()==0 && fotos.getCount()==0){
            ViewSnackBar.SnackBarDanger(layout, getString(R.string.EMPTY_TABLES) , this);
            return;
        }
        if (!Connections.ConexionInternet(this)){
            ViewSnackBar.SnackBarDanger(layout, getString(R.string.offline) , this);
            return;
        }
        personas.close();
        fotos.close();
        db.close();
        new BackGroundEnviar().execute();
    }

    void ExecuteSave(){
        setCoordenadasInit();
        String message ="";
        getTexts();
        if (nombres==null || documento==null|| tv_barrio.getTag() ==null || tv_correg.getTag() ==null || fecha_nac==null || edad==null ){
            ViewSnackBar.SnackBarDanger(layout, "Por favor agregue toda la información para continuar",this);
            return;
        }
        if (longitud==0 || latitud ==0 ){
            ViewSnackBar.SnackBarDanger(layout, "Detectando su ubicación. Por favor espere...",this);
            return;
        }
        if (tel.equalsIgnoreCase("0") && email.equalsIgnoreCase("null")){
            message ="¿Deseas continuar sin la información del telefono celular y correo electronico?";
            DialogVerficarTelEmail(message);
        }else if (email.equalsIgnoreCase("null")){
            message = "¿Deseas continuar sin la información del correo electronico?";
            DialogVerficarTelEmail(message);
        }else if (tel.equalsIgnoreCase("0")){
            message ="¿Deseas continuar sin la información del telefono celular?";
            DialogVerficarTelEmail(message);
        }else {
            GuardarDatos();
        }
    }

    void GuardarDatos(){
      try {
          getTexts();
          SQLiteDatabase db = Connections.Database(this);
          ContentValues values =  new ContentValues();

          values.put("NOMBRES", nombres);
          values.put("APELLIDOS", apellidos);
          values.put("CEDULA", documento);
          values.put("EDAD", edad);
          values.put("SEXO", sexo);
          values.put("FECHA_NAC", fecha_nac);
          values.put("FECHA_EXP", fecha_exp);
          values.put("LUGAR_NAC", lugar_nac);
          values.put("TELEFONO", tel);
          values.put("EMAIL", email);
          values.put("USER", USER);
          values.put("LATITUD", latitud);
          values.put("LONGITUD", longitud);
          values.put("CORREG", correg);
          values.put("BARRIO", barrio);
          values.put("FECHA_EJE", dateFormat.format( new Date()));
          values.put("HORA_EJE", horaformat.format(new Date()));
          Cursor cursor =  db.rawQuery("SELECT * FROM PERSONAS WHERE CEDULA ='"+documento+"'", null);
          if (cursor.getCount()==0){
              long res = db.insert("PERSONAS", null, values);
              if (res>0){
                  ContentValues foto =  new ContentValues();
                  foto.put("ESTADO", "N");
                  db.update("FOTOS",foto , "ID_FOTO=?", new String[]{documento});
                  Clean();
                  ViewSnackBar.SnackBarSuccess(layout, getString(R.string.save_success),this);
              }else {
                  ViewSnackBar.SnackBarDanger(layout, getString(R.string.save_error),this);
              }
          }else {
              ViewSnackBar.SnackBarDanger(layout, getString(R.string.registro_ya_existe),this);
          }

          db.close();
          cursor.close();
      }catch (Exception e){
          ViewSnackBar.SnackBarDanger(layout, e.getMessage(),this);
      }
    }

    void setCoordenadasInit(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        try {
                            longitud = location.getLongitude();
                            latitud = location.getLatitude();
                            Log.e(TAG, "getCoordenadas:  entra " + location.getLatitude() +location.getLongitude());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    // CERRAR SESION
    @SuppressLint("UseCompatLoadingForDrawables")
    void DialogLogOut(){
        String message = "";
        SQLiteDatabase db = Connections.Database(this);
        Cursor ordenes =  db.rawQuery("SELECT * FROM PERSONAS WHERE ESTADO ='N'",null);
        if (ordenes.getCount()>0){
            message ="Tienes información pendiente por enviar\n\n¿Deseas continuar?";
        }else {
            message = "¿Estás seguro que deseas cerrar sesión?";
        }
        ordenes.close();
        db.close();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getDrawable(R.drawable.baseline_logout_24));
        builder.setTitle("¡Cerrar Sesión!")
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> controller.Execute(Controller.LOGOUT,0))
                .setNegativeButton("Cancelar", ((dialog, which) -> dialog.dismiss()));
        AlertDialog dialog =  builder.create();
        dialog.show();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    void DialogVerficarTelEmail(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getDrawable(R.drawable.ic_baseline_warning_amber_24));
        builder.setTitle("¡Advertencia!")
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) ->GuardarDatos())
                .setNegativeButton("Cancelar", ((dialog, which) -> dialog.dismiss()));
        AlertDialog dialog =  builder.create();
        dialog.show();
    }
    // METODO DE CERRAR SESION LOCAL Y EN EL SERVIDOR

    @SuppressLint("StaticFieldLeak")
    class BackGroundEnviar extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Enviando Datos...");
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Esto puede tardar varios minutos...");
            progressDialog.setProgress(0);
            progressDialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            CountDownLatch latch;
            String tag;
            try {
                latch = new CountDownLatch(1);
                tag =  "allow";
                //SendData.Enviar(Gestion.this, layout);
                controller.Execute(Controller.SEND,0);
                while (!tag.equalsIgnoreCase("stop")){
                    tag = getStop();
                    if (tag.equalsIgnoreCase("stop")){
                        latch.countDown();
                    }
                }

                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @SuppressLint("Range")
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }
    class BackGroundParams extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Sincronizando parametros...");
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Verficando Información...\nPor favor espere unos minutos");
            progressDialog.setProgress(0);
            progressDialog.show();
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... strings) {

            CountDownLatch latch;
            String tag;
            try {
                latch = new CountDownLatch(1);
                tag =  "allow";
                for (int i = 1; i <= 2; i++) {
                    controller.Execute(Controller.SYNC, i );
                }
                while (!tag.equalsIgnoreCase("stop")){
                    tag = getStop();
                    if (tag.equalsIgnoreCase("stop")){
                        latch.countDown();
                    }
                }

                latch.await();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @SuppressLint("Range")
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    String getStop(){
        SharedPreferences preferences = getSharedPreferences("DATA", MODE_PRIVATE);
        return preferences.getString("progress", "allow");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST && resultCode==RESULT_OK && data!=null){
           // GuardarFoto(data);
        }
    }

    private void CapturarHuella(){
         try {
             Log.e(TAG, "CapturarHuella: entra");
             BiometricPrompt biometricPrompt = null;
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                 biometricPrompt = new BiometricPrompt.Builder(this)
                         .setTitle("Autenticación de huella digital")
                         .setSubtitle("Coloque su dedo en el sensor de huellas digitales")
                         .setDescription("Se requiere autenticación para continuar")
                         .setNegativeButton("Cancelar", this.getMainExecutor(), (dialogInterface, i) -> {
                             // La autenticación fue cancelada por el usuario
                         })
                         .build();

                 biometricPrompt.authenticate(new CancellationSignal(),
                         this.getMainExecutor(),
                         new BiometricPrompt.AuthenticationCallback() {

                     @Override
                     public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                         // La autenticación fue exitosa
                         Log.e(TAG, "CapturarHuella: entra result" + result.getCryptoObject());
                     }

                     @Override
                     public void onAuthenticationFailed() {
                         // La autenticación falló
                         Log.e(TAG, "CapturarHuella: entra fail ");
                     }

                     @Override
                     public void onAuthenticationError(int errorCode, CharSequence errString) {
                         // Se produjo un error durante la autenticación
                         Log.e(TAG, "CapturarHuella: entra err" + errString);
                     }
                 });
             }


         }catch (Exception e){
             e.printStackTrace();
         }
    }
}