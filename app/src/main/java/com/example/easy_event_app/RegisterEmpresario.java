package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.easy_event_app.model.RespuestaRegister;
import com.example.easy_event_app.network.RegisterEmpAPICliente;
import com.example.easy_event_app.network.RegisterEmpAPIService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEmpresario extends AppCompatActivity {

    public void Next(View view) {
        startActivity(new Intent(this, RegisterEmpresa.class));
    }

    EditText ced;
    EditText nom;
    EditText apell;
    EditText corr;
    EditText fech;
    EditText tel;
    EditText cont;
    private RegisterEmpAPIService service;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empresario);

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

        Button register = findViewById(R.id.registerEmp);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.formulario);
                if (checkFieldsRequired(linearLayout)) {
                    registerEmpresario(v);
                }
            }
        });

        service = RegisterEmpAPICliente.getRegisterEmpService();

    }

    private void mostrarCalendario(final EditText editText) {
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.YEAR, -18); // Resta 18 años a la fecha actual

        int añoMaximo = calendario.get(Calendar.YEAR);
        int mesMaximo = calendario.get(Calendar.MONTH);
        int diaMaximo = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterEmpresario.this,
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


    public void registerEmpresario (View view) {

        String cedula = ced.getText().toString().trim();
        String nombre = nom.getText().toString();
        String apellido = apell.getText().toString();
        String correo = corr.getText().toString();
        String fechaNacimiento = fech.getText().toString();
        String telefono = tel.getText().toString();
        String contraseña = cont.getText().toString();

        Intent pasar = new Intent(this, RegisterEmpresa.class);
        pasar.putExtra("cedula", cedula);
        pasar.putExtra("nombre", nombre);
        pasar.putExtra("apellido", apellido);
        pasar.putExtra("correo", correo);
        pasar.putExtra("fechaNacimiento", fechaNacimiento);
        pasar.putExtra("telefono", telefono);
        pasar.putExtra("contraseña", contraseña);
        startActivity(pasar);

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

}