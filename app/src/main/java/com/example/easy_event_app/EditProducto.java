package com.example.easy_event_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_event_app.model.Categoria;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.Producto;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.CategoriaAPICliente;
import com.example.easy_event_app.network.CategoriaAPIService;
import com.example.easy_event_app.network.ProductoAPICliente;
import com.example.easy_event_app.network.ProductoAPIService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProducto extends AppCompatActivity {

    ImageView imgGallery;
    EditText precioPr;
    EditText nombrePr;
    EditText cantidadPr;
    EditText txtDescripcion;
    long productoId;
    Spinner categoriaPr;
    Button guardarPr;
    private List<Categoria> categoriaLista;
    private Uri selectedImageUri;
    private ProductoAPIService service;
    private CategoriaAPIService servicecat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_producto);

        imgGallery = findViewById(R.id.imgGallery);
        precioPr = findViewById(R.id.txtPrecio);
        nombrePr = findViewById(R.id.nombrePr);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        cantidadPr = findViewById(R.id.cantidadPr);
        categoriaPr = findViewById(R.id.categoriaPr);
        guardarPr = findViewById(R.id.guardarPr);

        service = ProductoAPICliente.getProductoService();
        servicecat = CategoriaAPICliente.getCategoriaService();

        servicecat.categorias(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<CategoriaRespuesta>() {
            @Override
            public void onResponse(Call<CategoriaRespuesta> call, Response<CategoriaRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Categoria> todaslascategorias = response.body().getCategoria();
                    categoriaLista = todaslascategorias;
                    cargarListaCategorias(categoriaLista);
                }
            }

            @Override
            public void onFailure(Call<CategoriaRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });


        productoId =  getIntent().getLongExtra("PRODUCTO_ID", 0);
        Log.d("ProductoID", "ID de Producto: " + productoId);

        service.producto(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), (int) productoId)
                .enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call, Response<Producto> response) {
                        if (response.isSuccessful()) {
                            Producto producto = response.body();
                            String ruta = "http://easyevent.api.adsocidm.com/storage/"+producto.getFoto();
                            Picasso.with(getApplicationContext()).load(ruta).into(imgGallery);
                            nombrePr.setText(producto.getNombre_producto());
                            txtDescripcion.setText(producto.getDescripcion());
                            precioPr.setText(String.valueOf(producto.getPrecio()));
                            cantidadPr.setText(String.valueOf(producto.getCantidad_disponible()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                        // Manejar el fallo de la solicitud
                    }
                });



        imgGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(i);
                //startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        ImageButton volverButton = findViewById(R.id.volverButton);
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        guardarPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration(selectedImageUri);
            }
        });
    }
    private void cargarListaCategorias(List<Categoria> data) {
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                data
        );
        categoriaPr.setAdapter(adapter);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode()
                            == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // do your operation from here....
                        if (data != null
                                && data.getData() != null) {
                            selectedImageUri = data.getData();
                            Bitmap selectedImageBitmap=null;
                            try {
                                selectedImageBitmap
                                        = MediaStore.Images.Media.getBitmap(
                                        EditProducto.this.getContentResolver(),
                                        selectedImageUri);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            imgGallery.setImageBitmap(
                                    selectedImageBitmap);
                        }
                    }
                }
            });

    private void performRegistration(Uri selectedImageUri) {
        String preciopr = precioPr.getText().toString();
        String nombrepr = nombrePr.getText().toString();
        String cantidadpr = cantidadPr.getText().toString();
        String txtdescripcion = txtDescripcion.getText().toString();
        Long categoriapr = categoriaPr.getSelectedItemId()+1;
        // productoid = ""+productoId;

        // Convertir URI a File
        File file = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Crear un archivo temporal para guardar la imagen
            file = createTempFileFromInputStream(inputStream);
        } catch (Exception ex) {

        }

        MultipartBody.Part foto;

        if (file != null){
            //File file = new File(selectedImageUri.getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            // Crear MultipartBody.Part desde el objeto RequestBody
            foto = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);

        }else{
            foto = null;
        }

        // Crear MultipartBody.Part desde el objeto RequestBody


        RequestBody precio = RequestBody.create(MediaType.parse("text/plain"),preciopr);
        //RequestBody Idproducto = RequestBody.create(MediaType.parse("text/plain"),productoid);
        RequestBody nombre_producto = RequestBody.create(MediaType.parse("text/plain"),nombrepr);
        RequestBody cantidad_disponible = RequestBody.create(MediaType.parse("text/plain"),cantidadpr);
        RequestBody descripcion = RequestBody.create(MediaType.parse("text/plain"),txtdescripcion);
        RequestBody categoria = RequestBody.create(MediaType.parse("text/plain"),categoriapr.toString());



        service.productoedit(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(),
                        productoId , precio, nombre_producto, cantidad_disponible, categoria, descripcion, foto)
                .enqueue(new Callback<ProductoRespuesta>() {
                    @Override
                    public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                        if (response.isSuccessful()) {
                            //Datainfo.resultLogin = response.body();
                            finish();
                        } else {
                            Toast.makeText(EditProducto.this, "Error al guardar", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                        Toast.makeText(EditProducto.this, "Error en la edición: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });

    }

    private File createTempFileFromInputStream(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("tempImage", null);
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        out.close();
        return tempFile;
    }


}