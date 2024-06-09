package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easy_event_app.adapter.ModificarAlquilerAdapter;
import com.example.easy_event_app.adapter.SolicitudAdapter;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.AlquilerRespuesta;
import com.example.easy_event_app.model.InfoAlquiler;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlquilerModificar extends AppCompatActivity {

    private AlquilerAPIService servicio;
    private RecyclerView listaProductos;

    Button modificar;
    private ModificarAlquilerAdapter modificarAlquilerAdapter;
    private com.example.easy_event_app.model.InfoAlquiler alquiler;
    private List<Producto> productosLista;
    private Long alquilerId;
    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alquiler_modificar);

        modificar = findViewById(R.id.modificar);

        ImageButton volverButton = findViewById(R.id.volverButton);
        Button cancelar = findViewById(R.id.cancelarModificar);
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Producto> productos= ((ModificarAlquilerAdapter) listaProductos.getAdapter()).getListaProductos();
                String estado = "aceptar_modificacion";
                //RequestBody estado_field = RequestBody.create(MediaType.parse("text/plain"),estado.toString());
                servicio.alquiler_modificar(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), alquilerId, productos, estado).enqueue(new Callback<AlquilerRespuesta>() {
                    @Override
                    public void onResponse(Call<AlquilerRespuesta> call, Response<AlquilerRespuesta> response) {
                        if (response.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<AlquilerRespuesta> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

            }
        });

        servicio = AlquilerAPICliente.getAlquilerService();
        listaProductos = findViewById(R.id.listaProductos);
        listaProductos.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        alquilerId = (long) getIntent().getLongExtra("alquiler_id", 0);

        servicio.alquiler_id(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), alquilerId).enqueue(new Callback<InfoAlquiler>() {
            @Override
            public void onResponse(Call<InfoAlquiler> call, Response<InfoAlquiler> response) {
                if (response.isSuccessful()) {
                    alquiler = response.body();
                    List<Producto> todoslosproductos = response.body().getProducto_alquiler();

                    productosLista = todoslosproductos;
                    cargarListaProductos(productosLista);
                }
            }

            @Override
            public void onFailure(Call<com.example.easy_event_app.model.InfoAlquiler> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }


    private void cargarListaProductos(List<Producto> data) {
        modificarAlquilerAdapter = new ModificarAlquilerAdapter(productosLista, this);
        listaProductos.setAdapter(modificarAlquilerAdapter);


        List<Producto> informacion = ((ModificarAlquilerAdapter) listaProductos.getAdapter()).getListaProductos();
    }


}