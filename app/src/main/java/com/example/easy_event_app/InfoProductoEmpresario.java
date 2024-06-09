package com.example.easy_event_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.ProductoAPICliente;
import com.example.easy_event_app.network.ProductoAPIService;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoProductoEmpresario extends AppCompatActivity {

    private ProductoAPIService servicio;
    Button eliminar;
    Button editar;
    long productoId;

    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));



    public void cargarpr() {

        productoId = (long) getIntent().getLongExtra("PRODUCTO_ID", -1);
        Log.d("ProductoID", "ID de Producto: " + productoId);


        servicio.producto(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), (int) productoId)
                .enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        if (response.isSuccessful()) {
                            Producto producto = response.body();

                            ImageView imageView = findViewById(R.id.imageView);
                            TextView txtNombreProduc = findViewById(R.id.txtNombreProduc);
                            TextView txtPrecioProduc = findViewById(R.id.txtPrecioProduc);
                            TextView txtCantidad = findViewById(R.id.txtCantidad);
                            TextView txtDescripcion = findViewById(R.id.txtDescripcion);

                            String ruta = "http://easyevent.api.adsocidm.com/storage/"+producto.getFoto();
                            Picasso.with(getApplicationContext()).load(ruta).into(imageView);
                            txtNombreProduc.setText(producto.getNombre_producto());

                            txtDescripcion.setText(producto.getDescripcion());

                            double precioProducto = producto.getPrecio();
                            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
                            format.setMaximumFractionDigits(0);
                            String precioFormateado = format.format(precioProducto);

                            txtPrecioProduc.setText(precioFormateado);
                            txtCantidad.setText(String.valueOf(producto.getCantidad_disponible()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        // Manejar el fallo de la solicitud
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto_empresario);

        servicio = ProductoAPICliente.getProductoService();
        cargarpr();


        /*editTextNumber = findViewById(R.id.editTextNumber);
        Button btnDecrease = findViewById(R.id.btnDecrease);
        Button btnIncrease = findViewById(R.id.btnIncrease);

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseValue();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValue();
            }
        });

        Intent intent = getIntent();
        String nombreProducto = intent.getStringExtra("nombre_producto");
        int cantidadDisponible = intent.getIntExtra("cantidad_disponible", 0);
        long precioProducto = intent.getLongExtra("precio_producto", 0);


        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("COP"));
        String precioSigno = format.format(precioProducto);


        TextView txtNombreProduc = findViewById(R.id.txtNombreProduc);
        TextView txtCantidad = findViewById(R.id.txtCantidad);
        TextView txtPrecioProduc = findViewById(R.id.txtPrecioProduc);

        txtNombreProduc.setText(nombreProducto);
        txtCantidad.setText(String.valueOf(cantidadDisponible));
        txtPrecioProduc.setText(precioSigno);*/


        editar = findViewById(R.id.editar);
        eliminar = findViewById(R.id.eliminar);
        ImageButton volverButton = findViewById(R.id.volverButton);
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editar = new Intent(InfoProductoEmpresario.this, EditProducto.class);
                editar.putExtra("PRODUCTO_ID", productoId);
                startActivity(editar);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoProductoEmpresario.this);
                builder.setTitle("Eliminar producto");
                builder.setMessage(Html.fromHtml("Estas seguro que deseas eliminar el producto?, no podras recuperarlo"));
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        servicio.productodelete(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), productoId)
                                .enqueue(new Callback<ProductoRespuesta>() {
                                    @Override
                                    public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                                        if (response.isSuccessful()) {
                                            //Datainfo.resultLogin = response.body();
                                            finish();
                                        } else {
                                            Toast.makeText(InfoProductoEmpresario.this, "Error al guardar", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                                        Toast.makeText(InfoProductoEmpresario.this, "Error en la edición: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                        t.printStackTrace();
                                    }
                                });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();


            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarpr();

    }

    /*public void decreaseValue() {
        int currentValue = Integer.parseInt(editTextNumber.getText().toString());
        if (currentValue > 0) {
            editTextNumber.setText(String.valueOf(currentValue - 1));
        }
    }

    public void increaseValue() {
        int currentValue = Integer.parseInt(editTextNumber.getText().toString());
        editTextNumber.setText(String.valueOf(currentValue + 1));
    }*/
}