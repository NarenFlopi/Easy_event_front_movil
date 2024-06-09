package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.easy_event_app.adapter.CategoriaAdapter;
import com.example.easy_event_app.adapter.CategoriasAdapter;
import com.example.easy_event_app.adapter.ProductoAdapter;
import com.example.easy_event_app.model.Categoria;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.network.CategoriaAPICliente;
import com.example.easy_event_app.network.CategoriaAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Categorias extends AppCompatActivity {

    private CategoriaAPIService servicio;

    private CategoriasAdapter categoriaAdapter;

    private List<Categoria> primerosTresItems;

    private RecyclerView categoriaRecyclerView;


    @Override
    public void onStart() {
        super.onStart();

        servicio.categorias(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<CategoriaRespuesta>() {
            @Override
            public void onResponse(Call<CategoriaRespuesta> call, Response<CategoriaRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Categoria> todasLasCategorias = response.body().getCategoria();
                    primerosTresItems = todasLasCategorias;
                    cargarListaCategorias(primerosTresItems);
                }
            }

            @Override
            public void onFailure(Call<CategoriaRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
    private void cargarListaCategorias(List<Categoria> data) {
        categoriaAdapter = new CategoriasAdapter(primerosTresItems, this);
        categoriaRecyclerView.setAdapter(categoriaAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        servicio = CategoriaAPICliente.getCategoriaService();

        categoriaRecyclerView = findViewById(R.id.lista);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Categorias.this, 2);
        categoriaRecyclerView.setLayoutManager(gridLayoutManager);

        categoriaAdapter = new CategoriasAdapter(primerosTresItems, this);
        categoriaRecyclerView.setAdapter(categoriaAdapter);

        ImageButton volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

