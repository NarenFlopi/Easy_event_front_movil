package com.example.easy_event_app.network;

import com.example.easy_event_app.model.Favorito;
import com.example.easy_event_app.model.FavoritoRespuesta;
import com.example.easy_event_app.model.ProductoRespuesta;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoritoAPIService {

    @GET("favoritos/user")
    Call<FavoritoRespuesta> favoritos(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("favorito")
    Call<FavoritoRespuesta> favoritoadd(@Header("Authorization") String authorization,
                                        @Field("producto_id") Integer productoId
    );

    @DELETE("favorito/{id}")
    Call<Favorito> favoritodelete(@Header("Authorization") String authorization,
                                  @Path("id") long productoId
    );


}
