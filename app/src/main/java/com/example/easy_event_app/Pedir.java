package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy_event_app.model.Alquiler;
import com.example.easy_event_app.network.AlquilerAPIService;
import com.example.easy_event_app.network.AlquilerAPICliente;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pedir extends AppCompatActivity {

    Spinner spinnerMetodoPago;
    Spinner spinnerLugarEntrega;
    EditText lugarEntrega;
    EditText fechaAl;
    EditText fechaDev;
    private String lugar;
    private Long alquilerId;
    private AlquilerAPIService service;
    long diferenciaEnDias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir);

        AndroidThreeTen.init(this);

        alquilerId = getIntent().getLongExtra("ALQUILER_ID", -1);
        Log.d("AlquilerID", "ID de Alquiler: " + alquilerId);

        long totalAlquiler = getIntent().getLongExtra("TOTAL_ALQUILER", 0);
        Log.d("TotalAlquiler", "Total de alquiler recibido: " + totalAlquiler);


        Button volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago);
        spinnerLugarEntrega = findViewById(R.id.spinnerLugarEntrega);
        lugarEntrega = findViewById(R.id.lugarEntrega);
        fechaAl = findViewById(R.id.fechaAl);
        fechaDev = findViewById(R.id.fechaDev);

        fechaAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario(fechaAl, Calendar.getInstance()); // Fecha mínima es la fecha actual
            }
        });

        fechaDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fechaAl.getText().toString().isEmpty()) {
                    Calendar fechaMinima = Calendar.getInstance();
                    String[] partesFecha = fechaAl.getText().toString().split("-");
                    fechaMinima.set(Integer.parseInt(partesFecha[0]), Integer.parseInt(partesFecha[1]) - 1, Integer.parseInt(partesFecha[2]));
                    mostrarCalendario(fechaDev, fechaMinima);
                } else {
                    Toast.makeText(Pedir.this, "Seleccione primero la fecha de alquiler", Toast.LENGTH_SHORT).show();

                }
            }
        });

        service = AlquilerAPICliente.getAlquilerService();

        ArrayAdapter<CharSequence> lugarEntregaAdapter = ArrayAdapter.createFromResource(this, R.array.metodos_entrega, android.R.layout.simple_spinner_item);
        lugarEntregaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLugarEntrega.setAdapter(lugarEntregaAdapter);

        spinnerLugarEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Verificar si se seleccionó "Personalizado"
                if (position == 1) { // El índice 1 corresponde a "Personalizado" en el array
                    // Mostrar el EditText
                    lugarEntrega.setVisibility(View.VISIBLE);

                } else {
                    // Ocultar el EditText
                    lugarEntrega.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se necesita implementación en este caso
            }
        });

        Button guardarButtom = findViewById(R.id.pedirButtom);
        guardarButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(camposLlenos()){
                String metodo = spinnerMetodoPago.getSelectedItem().toString();
                String entrega = spinnerLugarEntrega.getSelectedItem().toString();

                if (entrega.equals("Personalizado")){

                    lugar =  lugarEntrega.getText().toString();

                }else {
                    lugar =  "Recoger";
                }

                String fechaAlquilerStr = fechaAl.getText().toString();
                String fechaDevolucionStr = fechaDev.getText().toString();



                // Hacer la solicitud al servidor
                service.solicitudPedido(Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token(), alquilerId, metodo, lugar, fechaAlquilerStr, fechaDevolucionStr, diferenciaEnDias)
                        .enqueue(new Callback<Alquiler>() {
                            @Override
                            public void onResponse(Call<Alquiler> call, Response<Alquiler> response) {
                                int responseCode = response.code(); //Obtener codigo de respuesta
                                Log.i("Logout", "codigo de respuesta" + responseCode);

                                if (response.isSuccessful()) {
                                    Toast.makeText(Pedir.this, "Pedido realizado con exito", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(Pedir.this, "No has ingresado los datos", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Alquiler> call, Throwable t) {
                                //manejar la falla de la llamada al servidor
                                Toast.makeText(Pedir.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                    Toast.makeText(Pedir.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mostrarCalendario(final EditText editText, final Calendar fechaMinima) {
        Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Pedir.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar fechaSeleccionada = Calendar.getInstance();
                        fechaSeleccionada.set(year, monthOfYear, dayOfMonth);
                        if (!fechaSeleccionada.before(fechaMinima)) {
                            String mesFormateado = String.format("%02d", monthOfYear + 1);
                            String diaFormateado = String.format("%02d", dayOfMonth);
                            String fechaSeleccionadaStr = year + "-" + mesFormateado + "-" + diaFormateado;
                            editText.setText(fechaSeleccionadaStr);

                            // Calcular la diferencia en días
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate fechaAlquiler = LocalDate.parse(fechaAl.getText().toString(), formatter);
                            LocalDate fechaDevolucion = LocalDate.parse(fechaSeleccionadaStr, formatter);
                            diferenciaEnDias = ChronoUnit.DAYS.between(fechaAlquiler, fechaDevolucion);

                            long totalAlquiler = getIntent().getLongExtra("TOTAL_ALQUILER", 0);

                            // Calcular y mostrar el precio total en el Logcat
                            long precioTotal;
                            if (diferenciaEnDias <= 0) {
                                precioTotal = totalAlquiler;
                            } else {
                                precioTotal = totalAlquiler * diferenciaEnDias;
                            }

                            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
                            format.setMaximumFractionDigits(0);
                            String precioTotalFormateado = format.format(precioTotal);

                            // Mostrar el precio total formateado en el TextView
                            TextView precioTotalTextView = findViewById(R.id.precioTotal);
                            precioTotalTextView.setText("" + precioTotalFormateado);

                            Intent intent = new Intent(Pedir.this, ThirdFragment.class);
                            intent.putExtra("PRECIO_TOTAL", precioTotal);

                        } else {
                            Toast.makeText(Pedir.this, "La fecha seleccionada no puede ser anterior a la fecha mínima", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, año, mes, dia);

        // Establece la fecha mínima permitida como la fecha de mañana
        datePickerDialog.getDatePicker().setMinDate(fechaMinima.getTimeInMillis());

        datePickerDialog.show();
    }

    private boolean camposLlenos() {
        boolean camposLlenos = !fechaAl.getText().toString().isEmpty() &&
                !fechaDev.getText().toString().isEmpty();

        if(spinnerLugarEntrega.getSelectedItemPosition() == 1) {
            camposLlenos &= !lugarEntrega.getText().toString().isEmpty();
        }

        return camposLlenos;
    }

}