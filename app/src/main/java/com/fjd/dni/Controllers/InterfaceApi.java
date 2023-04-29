package com.fjd.dni.Controllers;


import com.fjd.dni.Models.DatosDownload;
import com.fjd.dni.Models.DatosUpload;
import com.fjd.dni.Models.LoginAuthReq;
import com.fjd.dni.Models.LoginAuthRes;
import com.fjd.dni.Models.MainJson;
import com.fjd.dni.Models.RespuestaFotos;
import com.fjd.dni.Models.SaveDatosUpload;
import com.fjd.dni.Models.SendDatosFotos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceApi {


    @POST("Token.php")
    Call<LoginAuthRes> getDatosLogin(@Body LoginAuthReq req);


    @POST("Sync.php")
    Call<DatosDownload> getDatosUpload(@Body ArrayList<DatosUpload> request);


    @POST("Sync.php")
    Call<DatosDownload> getSave(@Body ArrayList<SaveDatosUpload> request);
    @POST("Controller.php")
    Call<DatosDownload> getPrueba(@Body ArrayList<MainJson> request);

    @POST("fotos.php")
    Call<RespuestaFotos> getSendFoto(@Body ArrayList<SendDatosFotos> request);


}
