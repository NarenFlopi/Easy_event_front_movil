package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easy_event_app.adapter.AlquilerAdapter;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.AlquilerRespuesta;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudAlquileres extends AppCompatActivity {

    private AlquilerAPIService servicio;
    private AlquilerAdapter alquilerAdapter;
    private List<Alquiler> alquilereslista;
    private RecyclerView alquilerRecyclerView;
    private TextView nada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_alquileres);

        servicio = AlquilerAPICliente.getAlquilerService();
        alquilerRecyclerView = findViewById(R.id.listaAlquiler);
        alquilerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        nada = findViewById(R.id.nada);

        String estado = "solicitud";
        servicio.alquiler_filtrado(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), estado).enqueue(new Callback<AlquilerRespuesta>() {
            @Override
            public void onResponse(Call<AlquilerRespuesta> call, Response<AlquilerRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Alquiler> todosLosAlquileres = response.body().getAlquileres();
                    Log.i("alquilerestodos", response.body().toString());

                    Collections.sort(todosLosAlquileres, new Comparator<Alquiler>() {
                        @Override
                        public int compare(Alquiler alquiler1, Alquiler alquiler2) {
                            return Long.compare(alquiler2.getId(), alquiler1.getId());
                        }
                    });

                    alquilereslista = todosLosAlquileres;
                    cargarListaAlquileres(alquilereslista);
                }
            }

            @Override
            public void onFailure(Call<AlquilerRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });


        super.onStart();
    }

    private void cargarListaAlquileres(List<Alquiler> data) {

        if (data.size() <= 0 ){
            TextView nada = findViewById(R.id.nada);
            nada.setVisibility(View.VISIBLE);
        } else {
            TextView nada = findViewById(R.id.nada);
            nada.setVisibility(View.GONE);
        }

        alquilerAdapter = new AlquilerAdapter(data, this    );
        alquilerRecyclerView.setAdapter(alquilerAdapter);
    }
}