package com.example.easy_event_app.network;

import com.example.easy_event_app.model.RespuestaRegister;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterAPIService {

    @FormUrlEncoded
    @POST("auth/signup")
    Call<RespuestaRegister> register (@Field("cedula") String cedula,
                                      @Field("nombre") String nombre,
                                      @Field("apellido") String apellido,
                                      @Field("email") String email,
                                      @Field("fecha_nacimiento") String fechaNacimiento,
                                      @Field("telefono") String telefono,
                                      @Field("password") String password);



}
