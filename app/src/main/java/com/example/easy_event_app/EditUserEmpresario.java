package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.easy_event_app.model.RespuestaLogin;
import com.example.easy_event_app.network.LoginAPICliente;
import com.example.easy_event_app.network.LoginAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserEmpresario extends AppCompatActivity {

    EditText nom;
    EditText apell;
    EditText email;
    EditText tel;
    private LoginAPIService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ImageButton volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nom = findViewById(R.id.nom);
        apell = findViewById(R.id.apell);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);

        service = LoginAPICliente.getLoginService();

        Button guardarButtom = findViewById(R.id.guardarButtom);
        guardarButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = nom.getText().toString();
                String nuevoApellido = apell.getText().toString();
                String nuevoEmail = email.getText().toString();
                String nuevoTelefono = tel.getText().toString();

                // Hacer la solicitud al servidor
                service.actualizarUsuario(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), nuevoNombre, nuevoApellido, nuevoEmail, nuevoTelefono)
                        .enqueue(new Callback<RespuestaLogin>() {
                            @Override
                            public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
                                int responseCode = response.code(); //Obtener codigo de respuesta
                                Log.i("Logout", "codigo de respuesta" + responseCode);

                                if (response.isSuccessful()) {
                                    Toast.makeText(EditUserEmpresario.this, "Datos actualizados con exito", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(EditUserEmpresario.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RespuestaLogin> call, Throwable t) {
                                //manejar la falla de la llamada al servidor
                                Toast.makeText(EditUserEmpresario.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                            }
                        });


                //Toast.makeText(EditUser.this, "Datos guardados con exito", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        nom = findViewById(R.id.nom);
        apell = findViewById(R.id.apell);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);

        nom.setText(Datainfo.resultLogin.getUser().getNombre());
        apell.setText(Datainfo.resultLogin.getUser().getApellido());
        email.setText(Datainfo.resultLogin.getUser().getEmail());
        tel.setText(Datainfo.resultLogin.getUser().getTelefono().toString());



    }
}