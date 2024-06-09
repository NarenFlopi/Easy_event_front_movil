package com.example.easy_event_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_event_app.adapter.ProductosAdapter;
import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.model.Favorito;
import com.example.easy_event_app.model.FavoritoRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.example.easy_event_app.network.CategoriaAPICliente;
import com.example.easy_event_app.network.FavoritoAPICliente;
import com.example.easy_event_app.network.FavoritoAPIService;
import com.example.easy_event_app.network.ProductoAPICliente;
import com.example.easy_event_app.network.ProductoAPIService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoProducto extends AppCompatActivity {

    private ProductoAPIService servicio;
    private FavoritoAPIService service;
    private AlquilerAPIService servicios;

    private RecyclerView recyclerViewProductos;
    EditText editTextNumber;
    private int productoId;

    private int cantidadPro;
    private TextView txtPrecioProduc;

    ImageView cormorado;

    ImageView corblanco;

    int cantidadDisponible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);

        servicio = ProductoAPICliente.getProductoService();
        service = FavoritoAPICliente.getFavoritoService();
        servicios = AlquilerAPICliente.getAlquilerService();
        ImageView cormorado = findViewById(R.id.cormorado);
        ImageView corblanco = findViewById(R.id.corblanco);

        productoId = (int) getIntent().getLongExtra("PRODUCTO_ID", -1);
        Log.d("ProductoID", "ID de Producto: " + productoId);

        cantidadPro = (int) getIntent().getLongExtra("CANTIDAD", -1);
        Log.d("CantidadProducto", "Cantidad de producto:" + cantidadPro);

        editTextNumber = findViewById(R.id.editTextNumber);

        if (cantidadPro > 0) {

            editTextNumber.setText(String.valueOf(cantidadPro));
        } else {

            editTextNumber.setFilters(new InputFilter[]{new InputFilterMinMax(1, cantidadDisponible)});
        }



        cormorado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service.favoritodelete(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), productoId)
                        .enqueue(new Callback<Favorito>() {
                            @Override
                            public void onResponse(Call<Favorito> call, Response<Favorito> response) {
                                if (response.isSuccessful()) {
                                    cormorado.setVisibility(View.GONE);
                                    corblanco.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<Favorito> call, Throwable t) {
                                // Manejar el fallo de la solicitud
                            }
                        });

            }
        });
        corblanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service.favoritoadd(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), productoId)
                        .enqueue(new Callback<FavoritoRespuesta>() {
                            @Override
                            public void onResponse(Call<FavoritoRespuesta> call, Response<FavoritoRespuesta> response) {
                                if (response.isSuccessful()) {
                                        cormorado.setVisibility(View.VISIBLE);
                                        corblanco.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<FavoritoRespuesta> call, Throwable t) {
                                // Manejar el fallo de la solicitud
                            }
                        });

            }
        });


        servicio.producto(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), productoId)
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


                            if(producto.getFavorito() == "false"){
                                cormorado.setVisibility(View.GONE);
                            }else {
                                corblanco.setVisibility(View.GONE);
                            }


                            String ruta = "http://easyevent.api.adsocidm.com/storage/"+producto.getFoto();
                            Picasso.with(getApplicationContext()).load(ruta).into(imageView);
                            txtNombreProduc.setText(producto.getNombre_producto());

                            double precioProducto = producto.getPrecio();
                            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
                            format.setMaximumFractionDigits(0);
                            String precioFormateado = format.format(precioProducto);

                            txtPrecioProduc.setText(precioFormateado);
                            txtCantidad.setText(String.valueOf(producto.getCantidad_disponible()));
                            txtDescripcion.setText(producto.getDescripcion());

                            cantidadDisponible = producto.getCantidad_disponible();


                            // Configura el valor máximo de editTextNumber
                            editTextNumber.setFilters(new InputFilter[]{new InputFilterMinMax(1, cantidadDisponible)});
                        }
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        // Manejar el fallo de la solicitud
                    }
                });

        editTextNumber = findViewById(R.id.editTextNumber);
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

        /*Intent intent = getIntent();
        String nombreProducto = intent.getStringExtra("nombre_producto");
        String Descripcion = intent.getStringExtra("descripcion");
        int cantidadDisponible = intent.getIntExtra("cantidad_disponible", 0);
        long precioProducto = intent.getLongExtra("precio_producto", 0);


        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("COP"));
        String precioSigno = format.format(precioProducto);


        TextView txtNombreProduc = findViewById(R.id.txtNombreProduc);
        TextView txtCantidad = findViewById(R.id.txtCantidad);
        TextView txtPrecioProduc = findViewById(R.id.txtPrecioProduc);
        TextView txtDescripcion = findViewById(R.id.txtDescripcion);

        txtNombreProduc.setText(nombreProducto);
        txtCantidad.setText(String.valueOf(cantidadDisponible));
        txtPrecioProduc.setText(precioSigno);
        txtDescripcion.setText(Descripcion);*/



        ImageButton volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnAgregar = findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearAlquiler();
            }
        });
    }



    public void crearAlquiler () {

        String id = ""+productoId;
        String cantidad = editTextNumber.getText().toString();

        servicios.addAlquiler(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), id, cantidad)
                .enqueue(new Callback<Alquiler>() {
                    @Override
                    public void onResponse(Call<Alquiler> call, Response<Alquiler> response) {
                        if (response.isSuccessful()) {
                            mostrarMensaje("Producto agregado al carrito");
                            finish();
                        } else {
                            Log.e("Error", "Respuesta no exitosa. Código: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Alquiler> call, Throwable t) {
                        Log.e("Error", "Mensaje de error: " + t.getMessage());
                        t.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InfoProducto.this, "Solo puedes agregar productos de la misma empresa", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void mostrarMensaje(String mensaje) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(InfoProducto.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });





        /*AlertDialog.Builder builder = new AlertDialog.Builder(InfoProducto.this);
        builder.setMessage(mensaje)
                .setCancelable(false);

        AlertDialog alert = builder.create();
        alert.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        alert.dismiss();
                    }
                },
                2000);*/
    }


    public void decreaseValue() {
        String text = editTextNumber.getText().toString();
        if (!text.isEmpty()) {
            int currentValue = Integer.parseInt(text);
            if (currentValue > 1) {
                editTextNumber.setText(String.valueOf(currentValue - 1));
            } else {
                editTextNumber.setText("1");
            }
        } else {
            editTextNumber.setText("1");
        }
    }

    public void increaseValue() {
        String text = editTextNumber.getText().toString();
        if (!text.isEmpty()) {
            int currentValue = Integer.parseInt(text);
            if (currentValue < cantidadDisponible) {
                editTextNumber.setText(String.valueOf(currentValue + 1));
            } else {
                editTextNumber.setText(String.valueOf(cantidadDisponible));
            }
        } else {
            editTextNumber.setText("1");
        }
    }

    public class InputFilterMinMax implements InputFilter {
        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

}