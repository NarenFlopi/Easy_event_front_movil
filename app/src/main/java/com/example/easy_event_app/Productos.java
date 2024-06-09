package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_event_app.adapter.CategoriasAdapter;
import com.example.easy_event_app.adapter.ProductosAdapter;
import com.example.easy_event_app.model.Categoria;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.ProductoAPICliente;
import com.example.easy_event_app.network.ProductoAPIService;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Productos extends AppCompatActivity {

    private ProductoAPIService servicio;
    private ProductosAdapter productoAdapter;
    private List<Producto> productosLista;
    private RecyclerView recyclerViewProductos;

    /*@Override
    public void onStart() {
        super.onStart();


        servicio.productos(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<ProductoRespuesta>() {
            @Override
            public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Producto> todasLosProductos = response.body().getProducto();
                    productosLista = todasLosProductos;
                    cargarListaProductos(productosLista);
                }
            }

            @Override
            public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

    }*/

    private void cargarListaProductos(List<Producto> data) {
        productoAdapter = new ProductosAdapter(productosLista, this);
        recyclerViewProductos.setAdapter(productoAdapter);

        TextView textViewEmpty = findViewById(R.id.textViewEmpty);

        if (data.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewProductos.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewProductos.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        int categoryId = (int) getIntent().getLongExtra("CATEGORY_ID", -1);
        Log.d("CategoriaID", "ID de Categoría: " + categoryId);


        servicio = ProductoAPICliente.getProductoService();

        recyclerViewProductos = findViewById(R.id.lista1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewProductos.setLayoutManager(linearLayoutManager);

        productoAdapter = new ProductosAdapter(productosLista, this);
        recyclerViewProductos.setAdapter(productoAdapter);

        servicio.getProductosPorCategoria(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), categoryId)
                .enqueue(new Callback<ProductoRespuesta>() {
                    @Override
                    public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                        if (response.isSuccessful()) {
                            List<Producto> todasLosProductos = response.body().getProducto();

                            Collections.sort(todasLosProductos, new Comparator<Producto>() {
                                @Override
                                public int compare(Producto producto1, Producto producto2) {
                                    return Long.compare(producto2.getId(), producto1.getId());
                                }
                            });

                            productosLista = todasLosProductos;
                            cargarListaProductos(productosLista);
                        } else {
                            Log.e("Error", "Respuesta no exitosa. Código: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                        Log.e("Error", "Mensaje de error: " + t.getMessage());
                        t.printStackTrace();
                    }
                });


        ImageButton volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




}