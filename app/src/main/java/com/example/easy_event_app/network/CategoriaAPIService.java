package com.example.easy_event_app.network;

import com.example.easy_event_app.model.Categoria;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.RespuestaRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CategoriaAPIService {

    @GET("categoria")
    Call<CategoriaRespuesta> categorias (@Header("Authorization")String authorization);

}
