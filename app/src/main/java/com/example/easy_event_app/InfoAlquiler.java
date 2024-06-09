package com.example.easy_event_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.easy_event_app.adapter.AlquilerAdapter;
import com.example.easy_event_app.adapter.ProductoAdapter;
import com.example.easy_event_app.adapter.SolicitudAdapter;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.AlquilerRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoAlquiler extends AppCompatActivity {

    private AlquilerAPIService servicio;
    private RecyclerView listaProductos;
    private SolicitudAdapter solicitudAdapter;
    private com.example.easy_event_app.model.InfoAlquiler alquiler;
    private List<Producto> productosLista;
    private Long alquilerId;
    private String estado;
    private FloatingActionButton editAlquiler;
    private Button btnRechazar, btnAceptar;
    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_alquiler);
        servicio = AlquilerAPICliente.getAlquilerService();
        editAlquiler = findViewById(R.id.editAlquiler);
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

        estado = getIntent().getStringExtra("ESTADO_PEDIDO");
        Log.d("EstadoPedido", "Estado del pedido:" + estado);


        if ("Entregado".equals(estado) || "Rechazado".equals(estado) || "Finalizado".equals(estado)|| "Recibido".equals(estado) || "Entregado_empresario".equals(estado)|| "Modificado".equals(estado)){
            LinearLayout linearBotones = findViewById(R.id.BotonesOcultar);
            linearBotones.setVisibility(View.GONE);
            FloatingActionButton botonFlotante = findViewById(R.id.editAlquiler);
            botonFlotante.setVisibility(View.GONE);
        }


        editAlquiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoAlquiler.this, AlquilerModificar.class);
                intent.putExtra("alquiler_id", alquilerId);
                llamado.launch(intent);
            }
        });


        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoAlquiler.this);
                builder.setTitle("Rechazar solicitud");
                builder.setMessage(Html.fromHtml("Estas seguro que deseas rechazar la solicitud?, no podras recuperarla"));
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String respuesta = "rechazar";
                        servicio.alquiler_responder(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(),alquilerId, respuesta).enqueue(new Callback<com.example.easy_event_app.model.InfoAlquiler>() {
                            @Override
                            public void onResponse(Call<com.example.easy_event_app.model.InfoAlquiler> call, Response<com.example.easy_event_app.model.InfoAlquiler> response) {
                                if (response.isSuccessful()) {
                                    finish();
                                }

                            }

                            @Override
                            public void onFailure(Call<com.example.easy_event_app.model.InfoAlquiler> call, Throwable t) {

                            }
                        });

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();


            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InfoAlquiler.this);
                builder.setTitle("Aceptar solicitud");
                builder.setMessage(Html.fromHtml("Estas seguro que deseas aceptar la solicitud?, no podras modificarla luego"));
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String respuesta = "aceptar";

                        if(alquiler.getLugar_entrega().equals("Recoger")) {

                            servicio.alquiler_responder(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), alquilerId, respuesta).enqueue(new Callback<com.example.easy_event_app.model.InfoAlquiler>() {
                                @Override
                                public void onResponse(Call<com.example.easy_event_app.model.InfoAlquiler> call, Response<com.example.easy_event_app.model.InfoAlquiler> response) {
                                    if (response.isSuccessful()) {
                                        finish();
                                    }

                                }

                                @Override
                                public void onFailure(Call<com.example.easy_event_app.model.InfoAlquiler> call, Throwable t) {

                                }
                            });

                        }else {

                            showDialog();



                        }

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();







            }
        });

    }
    ActivityResultLauncher<Intent> llamado = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK){
                        finish();
                    }else {

                    }
                }
            }
    );

    private void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        TextView lugar_entrega = dialog.findViewById(R.id.lugar_Entrega);
        EditText precio_envio = dialog.findViewById(R.id.precio_Envio);
        Button cancelar = dialog.findViewById(R.id.cancelar);
        Button aceptar = dialog.findViewById(R.id.aceptar);

        lugar_entrega.setText(alquiler.getLugar_entrega());
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String respuesta = "aceptar";

                String precio = precio_envio.getText().toString();

                servicio.alquiler_responder_envio(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), alquilerId, respuesta, precio).enqueue(new Callback<com.example.easy_event_app.model.InfoAlquiler>() {
                    @Override
                    public void onResponse(Call<com.example.easy_event_app.model.InfoAlquiler> call, Response<com.example.easy_event_app.model.InfoAlquiler> response) {
                        if (response.isSuccessful()) {
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<com.example.easy_event_app.model.InfoAlquiler> call, Throwable t) {

                    }
                });




            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);





    }

    @Override
    protected void onStart() {
        super.onStart();
        alquilerId = (long) getIntent().getLongExtra("alquiler_id", -1);

        servicio.alquiler_id(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), alquilerId).enqueue(new Callback<com.example.easy_event_app.model.InfoAlquiler>() {
            @Override
            public void onResponse(Call<com.example.easy_event_app.model.InfoAlquiler> call, Response<com.example.easy_event_app.model.InfoAlquiler> response) {
                if (response.isSuccessful()) {
                    alquiler = response.body();
                    List<Producto> todoslosproductos = response.body().getProducto_alquiler();
                    ImageView imageView = findViewById(R.id.fotoUser);
                    TextView txtNombre = findViewById(R.id.txtNombre);
                    TextView txtApellido = findViewById(R.id.txtApellido);
                    TextView txtTelefono = findViewById(R.id.txtTelefono);
                    TextView txtEntrega = findViewById(R.id.txtEntrega);
                    TextView txtMetodo = findViewById(R.id.txtMetodo);
                    TextView txtFecha = findViewById(R.id.txtFecha);
                    TextView txtPrecio = findViewById(R.id.txtPrecio);

                    String ruta = "https://easyevent.api.adsocidm.com/storage/"+alquiler.getFoto();
                    Picasso.with(getApplicationContext()).load(ruta).into(imageView);
                    txtNombre.setText(alquiler.getNombre());
                    txtApellido.setText(alquiler.getApellido());
                    txtTelefono.setText(String.valueOf(alquiler.getTelefono()));
                    txtEntrega.setText(alquiler.getLugar_entrega());
                    txtMetodo.setText(alquiler.getMetodo_pago());
                    txtFecha.setText(alquiler.getFecha_alquiler());

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
            public void onFailure(Call<com.example.easy_event_app.model.InfoAlquiler> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void cargarListaProductos(List<Producto> data) {
        solicitudAdapter = new SolicitudAdapter(productosLista, this);
        listaProductos.setAdapter(solicitudAdapter);


        List<Producto> informacion = ((SolicitudAdapter) listaProductos.getAdapter()).getListaProductos();
    }

    private void pedirDomicilio(){



    }
}