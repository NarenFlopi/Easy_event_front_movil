package com.example.easy_event_app.network;

import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.AlquilerRespuesta;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.ContadorAlquileres;
import com.example.easy_event_app.model.InfoAlquiler;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.model.RespuestaRegister;

import org.checkerframework.checker.nullness.qual.PolyRaw;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AlquilerAPIService {

    @GET("alquiler")
    Call<AlquilerRespuesta> alquileres (@Header("Authorization")String authorization);

    @FormUrlEncoded
    @POST("alquiler/filtrar")
    Call<ProductoRespuesta> carrito (@Header("Authorization") String authtoken,
                                     @Field("estado") String estado);

    @FormUrlEncoded
    @POST("alquileres/carrito")
    Call<Alquiler> addAlquiler (@Header("Authorization")String authorization,
                                @Field("id") String id,
                                @Field("cantidad") String cantidad);

    @FormUrlEncoded
    @PUT("alquiler/{id}")
    Call<Alquiler> solicitudPedido (@Header("Authorization")String authorization,
                                    @Path("id") long alquilerId,
                                    @Field("metodo_pago") String metodo,
                                    @Field("lugar_entrega") String lugar,
                                    @Field("fecha_alquiler") String fechaAlquiler,
                                    @Field("fecha_devolucion") String fechaDevolucion,
                                    @Field("diferencia_dias") long diferenciaEnDias);

    @FormUrlEncoded
    @PUT("alquiler/{id}")
    Call<InfoAlquiler> alquiler_responder (@Header("Authorization")String authorization,
                                           @Path("id") long id,
                                           @Field("respuesta") String respuesta);


    @GET("alquiler/{id}")
    Call<InfoAlquiler> alquiler_id (@Header("Authorization")String authorization,
                                    @Path("id") long id);

    @DELETE("alquiler/{id}")
    Call<Void> eliminarAlquiler(@Header("Authorization") String authorization,
                                @Path("id") String alquilerId);

    @FormUrlEncoded
    @POST("alquileres/delete_carrito")
    Call<Void> delete (@Header("Authorization")String authorization,
                                    @Field("id") String id,
                                    @Field("producto_id") String idProducto);

    @FormUrlEncoded
    @PUT("alquiler/{id}")
    Call<InfoAlquiler> alquiler_responder_envio (@Header("Authorization")String authorization,
                                                 @Path("id") long id,
                                                 @Field("respuesta") String respuesta,
                                                 @Field("precio_envio") String precio);

    @FormUrlEncoded
    @POST("alquiler/filtrar")
    Call <AlquilerRespuesta> alquiler_filtrado (@Header("Authorization")String authorization,
                                                @Field("estado") String estado);
    @FormUrlEncoded
    @PUT("alquiler/{id}")
    Call <AlquilerRespuesta> alquiler_modificar (@Header("Authorization")String authorization,
                                                 @Path("id") long id,
                                                 @Field ("productos[]") List<Producto> productos,
                                                 @Field ("respuesta") String estado );

    @GET("alquileres/contar")
    Call <ContadorAlquileres> contaralquiler (@Header("Authorization")String authorization);

}
