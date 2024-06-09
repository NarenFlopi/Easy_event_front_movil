package com.example.easy_event_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.easy_event_app.model.RespuestaRegister;
import com.example.easy_event_app.network.RegisterAPICliente;
import com.example.easy_event_app.network.RegisterAPIService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    EditText ced;
    EditText nom;
    EditText apell;
    EditText corr;
    EditText fech;
    EditText tel;
    EditText cont;

    private RegisterAPIService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ced = findViewById(R.id.ced);
        nom = findViewById(R.id.nom);
        apell = findViewById(R.id.apell);
        corr = findViewById(R.id.corr);
        fech = findViewById(R.id.fech);
        tel = findViewById(R.id.tel);
        cont = findViewById(R.id.cont);


        Button volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario(fech);
            }
        });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.formulario);
                if (checkFieldsRequired(linearLayout)) {
                    register(v);
                }
            }
        });

        service = RegisterAPICliente.getRegisterService();
    }

    public void showPrivacyPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Políticas de Privacidad");
        builder.setMessage(Html.fromHtml(
                "<br><b>PRIVACIDAD Y DATOS PERSONALES:</b><br><br>\n" +
                        "<b>1. Información Básica del Usuario:</b><br><br>\n" +
                        "La aplicación recopila y utiliza información básica del usuario, como nombre, dirección de correo electrónico y ubicación, con el fin de proporcionar servicios personalizados.<br><br>\n" +
                        "\n" +
                        "<b>2. Uso de Datos:</b><br><br>\n" +
                        "Easy Event utiliza la información proporcionada por los usuarios para mejorar la experiencia y ofrecer recomendaciones personalizadas de eventos.<br><br>\n" +
                        "\n" +
                        "<b>3. Confidencialidad:</b><br><br>\n" +
                        "La información del usuario se trata con confidencialidad y no se comparte con terceros sin el consentimiento explícito del usuario, excepto en casos requeridos por la ley.<br><br>\n" +
                        "\n" +
                        "<b>4. Cookies y Tecnologías Similares:</b><br><br>\n" +
                        "La aplicación puede utilizar cookies u otras tecnologías similares para mejorar la navegación y la experiencia del usuario. Al utilizar la aplicación, el usuario acepta el uso de estas tecnologías.<br>\n" +
                        "\n" +
                        "<br><br><b>USO ADECUADO DE LA APLICACION:</b><br><br>\n" +
                        "<b>1. Responsabilidades del Usuario:</b><br><br>\n" +
                        "Los usuarios se comprometen a utilizar la aplicación de manera ética y legal, respetando las leyes locales y los derechos de otros usuarios.<br><br>\n" +
                        "\n" +
                        "<b>2. Contenido Generado por Usuarios:</b><br><br>\n" +
                        "Easy Event no se hace responsable del contenido generado por los usuarios, pero se reserva el derecho de eliminar o modificar contenido que viole los términos de servicio.<br><br>\n" +
                        "\n" +
                        "<b>3. Cuenta del Usuario:</b><br><br>\n" +
                        "Los usuarios son responsables de mantener la confidencialidad de su cuenta y contraseña. Cualquier actividad realizada desde su cuenta será responsabilidad del usuario.\n"
        ));

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performRegistration();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

                Reg();
            }
        });

        builder.show();
    }

    private void mostrarCalendario(final EditText editText) {
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.YEAR, -18); // Resta 18 años a la fecha actual

        int añoMaximo = calendario.get(Calendar.YEAR);
        int mesMaximo = calendario.get(Calendar.MONTH);
        int diaMaximo = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String mesFormateado = String.format("%02d", monthOfYear + 1);
                        String diaFormateado = String.format("%02d", dayOfMonth);
                        String fechaSeleccionada = year + "-" + mesFormateado + "-" + diaFormateado;
                        editText.setText(fechaSeleccionada);
                    }
                }, añoMaximo, mesMaximo, diaMaximo);

        datePickerDialog.getDatePicker().setMaxDate(calendario.getTimeInMillis()); // Establece la fecha máxima permitida
        datePickerDialog.show();
    }

    public void Reg() {
        startActivity(new Intent(Register.this, MainActivity.class));
    }

    public void register(View view) {
        showPrivacyPolicyDialog();
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

    private void performRegistration() {

        String cedula = ced.getText().toString().trim();
        String nombre = nom.getText().toString();
        String apellido = apell.getText().toString();
        String correo = corr.getText().toString();
        String fechaNacimiento = fech.getText().toString();
        String telefono = tel.getText().toString();
        String contraseña = cont.getText().toString();

        service.register(cedula, nombre, apellido, correo, fechaNacimiento, telefono, contraseña)
                .enqueue(new Callback<RespuestaRegister>() {
                    @Override
                    public void onResponse(Call<RespuestaRegister> call, Response<RespuestaRegister> response) {
                        if (response.isSuccessful()) {
                            Datainfo.resultRegister = response.body();
                            Intent register = new Intent(Register.this, MainActivity.class);
                            startActivity(register);
                        } else {
                            Toast.makeText(Register.this, "Error en el registro", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaRegister> call, Throwable t) {
                        Toast.makeText(Register.this, "Error en el registro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }
}