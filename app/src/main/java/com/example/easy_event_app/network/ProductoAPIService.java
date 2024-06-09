package com.example.easy_event_app.network;

import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.Favorito;
import com.example.easy_event_app.model.FavoritoRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductoAPIService {

    @GET("producto")
    Call<ProductoRespuesta> productos (@Header("Authorization")String authorization);

    @GET("getProductos/{id}")
    Call<ProductoRespuesta> getProductosPorCategoria(
            @Header("Authorization") String authorization,
            @Path("id") int idCategoria
    );

    @Multipart
    @POST("producto")
    Call<ProductoRespuesta> productoAdd (@Header("Authorization")String authorization,
                                         @Part("precio") RequestBody precio,
                                         @Part("nombre_producto") RequestBody nombre_producto,
                                         @Part("cantidad_disponible") RequestBody cantidad_disponible,
                                         @Part("categoria") RequestBody categoria,
                                         @Part("descripcion") RequestBody descripcion,
                                         @Part MultipartBody.Part foto
    );


    @Multipart
    @POST("producto/actualizar/{id}")
    Call<ProductoRespuesta> productoedit (@Header("Authorization")String authorization,
                                          @Path("id") long productoId,
                                          @Part("precio") RequestBody precio,
                                          @Part("nombre_producto") RequestBody nombre_producto,
                                          @Part("cantidad_disponible") RequestBody cantidad_disponible,
                                          @Part("categoria") RequestBody categoria,
                                          @Part("descripcion") RequestBody descripcion,
                                          @Part MultipartBody.Part foto
    );

    @DELETE("producto/{id}")
    Call <ProductoRespuesta> productodelete (@Header("Authorization")String authorization,
                                             @Path("id") long productoId);



    @GET("producto/{id}")
    Call<Producto> producto (
            @Header("Authorization")String authorization,
            @Path("id") int idProducto
    );
    @POST("producto/search")
    Call<ProductoRespuesta> searchProductos(@Header("Authorization") String authorization, @Query("busqueda") String busqueda);





}
