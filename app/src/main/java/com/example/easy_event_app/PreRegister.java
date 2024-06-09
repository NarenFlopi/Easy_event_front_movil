package com.example.easy_event_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PreRegister extends AppCompatActivity {

    public void User(View view) {
        startActivity(new Intent(this, Register.class));
    }

    public void Empre(View v) {
        startActivity(new Intent(this, RegisterEmpresario.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_register);

        Button volverButton = findViewById(R.id.volverButton);

        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}