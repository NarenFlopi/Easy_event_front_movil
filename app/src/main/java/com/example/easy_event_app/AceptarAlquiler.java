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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easy_event_app.adapter.AlquileresAdapter;
import com.example.easy_event_app.adapter.ProductoAdapter;
import com.example.easy_event_app.adapter.SolicitudAdapter;
import com.example.easy_event_app.model.InfoAlquiler;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AceptarAlquiler extends AppCompatActivity {


    private AlquilerAPIService servicio;
    private RecyclerView listaProductos;
    private SolicitudAdapter solicitudAdapter;
    private List<Producto> productosLista;
    private Long alquilerId;
    private String estado;
    private Button btnRechazar, btnAceptar;
    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aceptar_alquiler);

        servicio = AlquilerAPICliente.getAlquilerService();


        listaProductos = findViewById(R.id.listaProductos);
        btnRechazar = findViewById(R.id.btnRechazar);
        btnAceptar = findViewById(R.id.btnAceptar);
        listaProductos.setLayoutManager(new LinearLayoutManager(this));


        ImageButton volverButton = findViewById(R.id.volverButton);
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alquilerId = getIntent().getLongExtra("ALQUILER_ID", -1);
        Log.d("AlquilerID", "ID de Alquiler: " + alquilerId);

        estado = getIntent().getStringExtra("ESTADO_PEDIDO");
        Log.d("EstadoPedido", "Estado del pedido:" + estado);


        if ("Aceptado".equals(estado) || "Solicitud".equals(estado)) {
            LinearLayout linearBotones = findViewById(R.id.linearBotones);
            linearBotones.setVisibility(View.GONE);
        }

        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String respuesta = "rechazar_entrega";
                servicio.alquiler_responder(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(),alquilerId, respuesta).enqueue(new Callback<InfoAlquiler>() {
                    @Override
                    public void onResponse(Call<InfoAlquiler> call, Response<InfoAlquiler> response) {
                        if (response.isSuccessful()) {
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<InfoAlquiler> call, Throwable t) {

                    }
                });

            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String respuesta = "aceptar_entrega";
                servicio.alquiler_responder(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(),alquilerId, respuesta).enqueue(new Callback<InfoAlquiler>() {
                    @Override
                    public void onResponse(Call<InfoAlquiler> call, Response<InfoAlquiler> response) {
                        if (response.isSuccessful()) {
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<InfoAlquiler> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        servicio.alquiler_id(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), alquilerId).enqueue(new Callback<InfoAlquiler>() {
            @Override
            public void onResponse(Call<InfoAlquiler> call, Response<InfoAlquiler> response) {
                if (response.isSuccessful()) {
                    InfoAlquiler alquiler = response.body();
                    List<Producto> todoslosproductos = response.body().getProducto_alquiler();


                    TextView txtEntrega = findViewById(R.id.txtEntrega);
                    TextView txtMetodo = findViewById(R.id.txtMetodo);
                    TextView txtFecha = findViewById(R.id.txtFecha);
                    TextView txtPrecio = findViewById(R.id.txtPrecio);
                    TextView txtTelefonoEmpresa = findViewById(R.id.txtTelefonoEmpresa);
                    TextView txtDireccionEmpresa = findViewById(R.id.txtDireccionEmpresa);



                    txtEntrega.setText(alquiler.getLugar_entrega());
                    txtMetodo.setText(alquiler.getMetodo_pago());
                    txtFecha.setText(alquiler.getFecha_alquiler());
                    txtTelefonoEmpresa.setText(String.valueOf(alquiler.getEmpresa().getTelefono_empresa()));
                    txtDireccionEmpresa.setText(alquiler.getEmpresa().getDireccion_empresa());

                    BigInteger precioProducto = alquiler.getPrecio_alquiler();
                    format.setMaximumFractionDigits(0); // Configurar para no mostrar decimale
                    String precioFormateado = format.format(precioProducto);
                    String precioConMoneda = precioFormateado + " COP";
                    txtPrecio.setText(precioConMoneda);



                    productosLista = todoslosproductos;
                    cargarListaProductos(productosLista);
                }
            }

            @Override
            public void onFailure(Call<InfoAlquiler> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });





    }

    private void cargarListaProductos(List<Producto> data) {
        solicitudAdapter = new SolicitudAdapter(productosLista, this);
        listaProductos.setAdapter(solicitudAdapter);
    }

}