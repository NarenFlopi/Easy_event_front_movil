package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.easy_event_app.adapter.FavoritosAdapter;
import com.example.easy_event_app.adapter.ProductosAdapter;
import com.example.easy_event_app.model.Favorito;
import com.example.easy_event_app.model.FavoritoRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.FavoritoAPICliente;
import com.example.easy_event_app.network.FavoritoAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favoritos extends AppCompatActivity {

    private FavoritoAPIService servicio;
    private FavoritosAdapter favoritoAdapter;
    private List<Favorito> favoritosLista;
    private RecyclerView recyclerViewProductos;

    private void cargarListaFavoritos(List<Favorito> data) {
        favoritoAdapter = new FavoritosAdapter(favoritosLista, this);
        recyclerViewProductos.setAdapter(favoritoAdapter);

        TextView textViewEmpty = findViewById(R.id.textViewEmpty);

        if (data.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewProductos.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewProductos.setVisibility(View.VISIBLE);
        }
    }

    private void cargarfavoritos() {
        servicio.favoritos(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token())
                .enqueue(new Callback<FavoritoRespuesta>() {
                    @Override
                    public void onResponse(Call<FavoritoRespuesta> call, Response<FavoritoRespuesta> response) {
                        if (response.isSuccessful()) {
                            List<Favorito> todasLosProductos = response.body().getProducto();
                            favoritosLista = todasLosProductos;
                            cargarListaFavoritos(favoritosLista);
                        } else {
                            Log.e("Error", "Respuesta no exitosa. Código: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<FavoritoRespuesta> call, Throwable t) {
                        Log.e("Error", "Mensaje de error: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        servicio = FavoritoAPICliente.getFavoritoService();

        recyclerViewProductos = findViewById(R.id.lista1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewProductos.setLayoutManager(linearLayoutManager);

        favoritoAdapter = new FavoritosAdapter(favoritosLista, this);
        recyclerViewProductos.setAdapter(favoritoAdapter);

        cargarfavoritos();


        ImageButton volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarfavoritos();
    }
}