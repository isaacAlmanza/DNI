package com.fjd.dni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fjd.dni.Controllers.GetApiService;
import com.fjd.dni.Controllers.GetSharedPreferences;
import com.fjd.dni.Controllers.InterfaceApi;
import com.fjd.dni.Controllers.ViewSnackBar;
import com.fjd.dni.Models.LoginAuthReq;
import com.fjd.dni.Models.LoginAuthRes;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    String TAG = "eRROr";
    FrameLayout layout;
    InterfaceApi api;
    String imei;
    TextView tv_title, tv_title_mode;
    String url, TOKEN,BASE;
    // EditText et_server, et_dominio, et_cc, et_cod;
    TextInputEditText et_server, et_dominio, et_cc, et_cod;

    Button btnAuth;
    ProgressBar progressBar;
    private ProgressDialog progressDialog;
    static final Integer PHONESTATS = 0x1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            consultarPermiso();
            BASE = "https://";
            et_server = findViewById(R.id.servidor);
            et_dominio = findViewById(R.id.dominio);
            et_cc = findViewById(R.id.cc_auth);
            progressBar =  findViewById(R.id.progress_bar);
            et_cod = findViewById(R.id.cod_auth);
            tv_title =  findViewById(R.id.title_login);
            tv_title_mode = findViewById(R.id.title_mode);
            TextView tv_instagram = findViewById(R.id.instagram);
            btnAuth = findViewById(R.id.btnAuth);
            layout = findViewById(R.id.layout_auth);
            TOKEN = GetSharedPreferences.getSharedData(this).get(1);



            if (!TOKEN.isEmpty()){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            tv_instagram.setOnClickListener(v->irAInstagram());
            btnAuth.setOnClickListener(view -> new BackgroundLogin().execute());
            tv_title.setOnLongClickListener(v->{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(getDrawable(R.drawable.ic_baseline_warning_amber_24));
                builder.setTitle("CENSOS CONELEC")
                        .setCancelable(true)
                        .setMessage("¿Cúal modo deseas utilizar?")
                        .setPositiveButton("Prueba", (dialog, id) -> {
                            BASE = "http://";
                            tv_title_mode.setText("Modo Prueba");
                            dialog.dismiss();
                        })
                        .setNegativeButton("Producción", ((dialog, which) ->{
                            BASE = "https://";
                            tv_title_mode.setText(null);
                            dialog.dismiss();
                        }));
                AlertDialog dialog =  builder.create();
                dialog.show();
                return false;
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }
    // OBTENEMOS EL ID QUE GENERA AL APP EN EL DISPOSITIVO
    @SuppressLint("HardwareIds")
    public static String getIMEIDeviceId(Context context) {
        String deviceId;
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }
    // CONSULTAMOS LOS PERMISOS DE LA APP
    private void consultarPermiso() {
        String[] perms = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET,
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,  android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE};

        int per =checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) ;
        int accessFinePermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarsePermission = checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int cameraPermission = checkSelfPermission(android.Manifest.permission.CAMERA);
        int internet =checkSelfPermission(android.Manifest.permission.INTERNET);
        int write = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (per == PackageManager.PERMISSION_GRANTED  && cameraPermission == PackageManager.PERMISSION_GRANTED &&
                accessFinePermission == PackageManager.PERMISSION_GRANTED && accessCoarsePermission == PackageManager.PERMISSION_GRANTED &&
                internet == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED) {
            imei = getIMEIDeviceId(getApplicationContext());
        } else {
            requestPermissions(perms,PHONESTATS);
        }
    }
    // METODO QUE NOS DA EL RESULTADO DE LOS PERMISOS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // Validamos si el usuario acepta el permiso para que la aplicación acceda a los datos internos del equipo, si no denegamos el acceso
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(this, "Has negado el permiso a la aplicación", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    // GUARDAMOS LAS CREDENCIALES EN LOS PREFERENCES DE LA APP
    private void SaveToken(String token, String url, String imei, String cedula, String d_user){
        String user =  et_cod.getText().toString().trim();
        SharedPreferences preferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor edit =  preferences.edit();
        edit.putString("TOKEN", token);
        edit.putString("user", user);
        edit.putString("url", url);
        edit.putString("cedula", cedula);
        edit.putString("imei", imei);
        edit.putString("d_user", d_user);
        edit.apply();
    }
    // VALIDAMOS LOS EL FORMULARIO
    boolean ValidarLogin(){
        String codigo = et_cod.getText().toString().trim();
        String cedula = et_cc.getText().toString().trim();
        String servidor = et_server.getText().toString().trim();
        String dominio = et_dominio.getText().toString().trim();
        if (codigo.isEmpty() || cedula.isEmpty() || servidor.isEmpty() || dominio.isEmpty()){
            ViewSnackBar.SnackBarDanger(layout, "Ingrese los datos para continuar", this);
            return true;
        }
        return false;
    }
    // METODO QUE NOS LLEVA A INSTAGRAM
    public void irAInstagram() {
        Uri uri = Uri.parse("http://www.instagram.com/fjddesarrollodesoftware/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    //PREPARAMOS LA PETION LOGIN CON LOS DATOS OBTENIDOS EN LA VISTA
    private void LoginData() throws NoSuchFieldException {
        try {
            String codigo = et_cod.getText().toString().trim();
            String cedula = et_cc.getText().toString().trim();
            String servidor = et_server.getText().toString().trim();
            String dominio = et_dominio.getText().toString().trim();
            LoginAuthReq req = new LoginAuthReq();
            req.setCodigo(codigo);
            req.setCedula(cedula);
            imei = getIMEIDeviceId(getApplicationContext());
            req.setImei(imei);
            url = BASE+servidor+"/"+dominio+"/pages/Api_Token/";
            api = GetApiService.ApiService(url);
            api.getDatosLogin(req).enqueue(new Callback<LoginAuthRes>() {
                @Override
                public void onResponse(Call<LoginAuthRes> call, Response<LoginAuthRes> response) {
                    LoginAuthRes res = response.body();
                    if (response.isSuccessful()) {
                        if (!res.getToken().isEmpty()) {
                            SaveToken(res.getToken(), url, imei, cedula, res.getUsu_movil());
                            ViewSnackBar.SnackBarSuccess(layout,res.getMsgError(),LoginActivity.this );
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            ViewSnackBar.SnackBarDanger(layout, res.getMsgError(), LoginActivity.this);
                        }
                    }else {
                        try {
                            String error =response.errorBody().string().replace("{\"jwt\":\"\",\"msg\":\"", "").replace("\"}", "");
                            ViewSnackBar.SnackBarDanger(layout, error.trim() , LoginActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<LoginAuthRes> call, Throwable t) {
                    ViewSnackBar.SnackBarDanger(layout, t.getMessage(), LoginActivity.this);

                }
            });
        }catch (Exception e){
            ViewSnackBar.SnackBarDanger(layout, e.getMessage(), LoginActivity.this);
        }

    }
    //CLASE QUE SE EJCUTE EN SEGUNDO PLANO QUE EJECUTA LA PETICION LOGIN AL SERVIDOR
    public class BackgroundLogin extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.setClickable(false);
            progressBar.setTooltipText("Cargando información por favor espere..");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (!ValidarLogin()) {
                try {
                    LoginData();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(unused);
        }
    }
}