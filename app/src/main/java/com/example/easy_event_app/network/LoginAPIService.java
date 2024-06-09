package com.example.easy_event_app.network;

import com.example.easy_event_app.EditUser;
import com.example.easy_event_app.model.RespuestaLogin;
import com.example.easy_event_app.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface LoginAPIService {

    @FormUrlEncoded
    @POST("auth/login")
    Call<RespuestaLogin> login (@Field("email") String email,
                                @Field("password") String password);

    @GET("logout")
    Call<Void> logout(@Header("Authorization") String authHeader);

    @GET("autologin")
    Call<RespuestaLogin> autologin (@Header("Authorization")String authorization);

    @FormUrlEncoded
    @PUT("user/edit")
    Call<RespuestaLogin> actualizarUsuario(@Header("Authorization") String authtoken,
                                     @Field("nombre") String nuevoNombre,
                                     @Field("apellido") String nuevoApellido,
                                     @Field("email") String nuevoEmail,
                                     @Field("telefono") String nuevoTelefono
                                     );


}
