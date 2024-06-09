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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.easy_event_app.model.Categoria;
import com.example.easy_event_app.model.CategoriaRespuesta;
import com.example.easy_event_app.model.ProductoRespuesta;
import com.example.easy_event_app.network.CategoriaAPICliente;
import com.example.easy_event_app.network.CategoriaAPIService;
import com.example.easy_event_app.network.ProductoAPICliente;
import com.example.easy_event_app.network.ProductoAPIService;

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

public class AddProducto extends AppCompatActivity {

    ImageView imgGallery;
    EditText precioPr;
    EditText nombrePr;
    EditText cantidadPr;
    EditText txtDescripcion;
    Spinner categoriaPr;
    Button crearPr;
    private List<Categoria> categoriaLista;
    private Uri selectedImageUri;
    private ProductoAPIService service;
    private CategoriaAPIService servicecat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_producto);


        imgGallery = findViewById(R.id.imgGallery);
        precioPr = findViewById(R.id.txtPrecio);
        nombrePr = findViewById(R.id.nombrePr);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        cantidadPr = findViewById(R.id.cantidadPr);
        categoriaPr = findViewById(R.id.categoriaPr);
        crearPr = findViewById(R.id.crearPr);

        //Button volverButton = findViewById(R.id.volverButton);

        /*volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        service = ProductoAPICliente.getProductoService();
        servicecat = CategoriaAPICliente.getCategoriaService();

        servicecat.categorias(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).enqueue(new Callback<CategoriaRespuesta>() {
            @Override
            public void onResponse(Call<CategoriaRespuesta> call, Response<CategoriaRespuesta> response) {
                if (response.isSuccessful()) {
                    List<Categoria> todaslascategorias = response.body().getCategoria();
                    categoriaLista = todaslascategorias;
                    cargarListaCategorias(categoriaLista);

                    for (Categoria categoria : todaslascategorias) {
                        Log.d("CATEGORIA", "ID: " + categoria.getId() + ", Nombre: " + categoria.getNombre());
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoriaRespuesta> call, Throwable t) {
                Log.e("Error", t.getMessage());
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

        crearPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.formulario);
                if (checkFieldsRequired(linearLayout)) {
                    performRegistration(selectedImageUri);
                }
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

    private void cargarListaCategorias(List<Categoria> data) {
        for (Categoria categoria : data) {
            Log.d("Categoria", "ID: " + categoria.getId() + ", Nombre: " + categoria.getNombre());
        }

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
                                        AddProducto.this.getContentResolver(),
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


        RequestBody precio = RequestBody.create(MediaType.parse("text/plain"),preciopr);
        RequestBody nombre_producto = RequestBody.create(MediaType.parse("text/plain"),nombrepr);
        RequestBody cantidad_disponible = RequestBody.create(MediaType.parse("text/plain"),cantidadpr);
        RequestBody descripcion = RequestBody.create(MediaType.parse("text/plain"),txtdescripcion);
        RequestBody categoria = RequestBody.create(MediaType.parse("text/plain"),categoriapr.toString());


        service.productoAdd(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(),precio, nombre_producto, cantidad_disponible, categoria, descripcion, foto)
                .enqueue(new Callback<ProductoRespuesta>() {
                    @Override
                    public void onResponse(Call<ProductoRespuesta> call, Response<ProductoRespuesta> response) {
                        if (response.isSuccessful()) {
                            //Datainfo.resultLogin = response.body();
                            finish();
                        } else {
                            Toast.makeText(AddProducto.this, "Error en el registro", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductoRespuesta> call, Throwable t) {
                        Toast.makeText(AddProducto.this, "Error en el regitro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });

    }

    public boolean checkFieldsRequired(LinearLayout linearLayout){

        boolean ajuste = true;
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof LinearLayout)
                ajuste=checkFieldsRequired((LinearLayout) view);
            else if (view instanceof EditText) {
                EditText edittext = (EditText) view;
                if (edittext.getText().toString().trim().equals("")) {
                    edittext.setError("Requerido!");
                    ajuste = false;
                    break;
                }
            }
        }



        return ajuste;

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