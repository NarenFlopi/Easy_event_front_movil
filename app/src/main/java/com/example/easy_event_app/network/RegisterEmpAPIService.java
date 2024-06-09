package com.example.easy_event_app.network;

import com.example.easy_event_app.model.RespuestaRegister;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RegisterEmpAPIService {

    @Multipart
    @POST("auth/signup_empresario")
    Call<RespuestaRegister> registerEmpresario (@Part("cedula") RequestBody cedula,
                                                @Part("nombre") RequestBody nombre,
                                                @Part("apellido") RequestBody apellido,
                                                @Part("email") RequestBody email,
                                                @Part("fecha_nacimiento") RequestBody fechaNacimiento,
                                                @Part("telefono") RequestBody telefono,
                                                @Part("password") RequestBody password,
                                                @Part("nit_empresa") RequestBody nit_empresa,
                                                @Part("direccion_empresa") RequestBody direccion_empresa,
                                                @Part("nombre_empresa") RequestBody nombre_empresa,
                                                @Part("telefono_empresa") RequestBody telefono_empresa,
                                                @Part("email_empresa") RequestBody email_empresa,
                                                @Part MultipartBody.Part foto);
}
