package com.example.easy_event_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.easy_event_app.model.RespuestaLogin;
import com.example.easy_event_app.network.LoginAPICliente;
import com.example.easy_event_app.network.LoginAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    CardView firstCardView;
    private LoginAPIService service;

    private boolean isValidate=true;


    public void login (View view) {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        String email = username.getText().toString();
        String passwd = password.getText().toString();

        /*service.login(email, passwd).enqueue(new Callback<RespuestaLogin>() {
            @Override
            public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
                if (response.isSuccessful()) {
                    Datainfo.resultLogin= response.body();
                    //Toast.makeText(MainActivity.this, "r"+response ,Toast.LENGTH_LONG).show();
                    if(Datainfo.resultLogin.getUser().getRol_id() == 2){
                        Intent logeo = new Intent(MainActivity.this, HomeEmpresario.class);
                        startActivity(logeo);
                        return;
                    }

                    if(Datainfo.resultLogin.getUser().getRol_id() == 3){
                        Intent logeo = new Intent(MainActivity.this, Home.class);
                        startActivity(logeo);
                        return;
                    }
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "Error email/password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaLogin> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/

        service.login(email, passwd).enqueue(new Callback<RespuestaLogin>() {
            @Override
            public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
                if (response.isSuccessful()) {
                    Datainfo.resultLogin= response.body();
                    //Toast.makeText(MainActivity.this, "r"+response ,Toast.LENGTH_LONG).show();
                    if(Datainfo.resultLogin.getUser().getRol_id() == 2){
                        Intent logeo = new Intent(MainActivity.this, HomeEmpresario.class);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        prefs.edit().putString("authorization",Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).commit();
                        startActivity(logeo);
                        return;
                    }

                    if(Datainfo.resultLogin.getUser().getRol_id() == 3){
                        Intent logeo = new Intent(MainActivity.this, Home.class);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        prefs.edit().putString("authorization",Datainfo.resultLogin.getToken_type() + " " + Datainfo.resultLogin.getAccess_token()).commit();
                        startActivity(logeo);
                        return;
                    }
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "Error email/password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaLogin> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void Reg (View view) {
        startActivity(new Intent(this, PreRegister.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = LoginAPICliente.getLoginService();



        if (Environment.isExternalStorageManager()) {

        } else {

            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        service = LoginAPICliente.getLoginService();
        firstCardView = findViewById(R.id.firstCardView);

        Animation slideUpAnimation = new TranslateAnimation(0, 0, 500, 0);
        slideUpAnimation.setDuration(2000);

        firstCardView.setVisibility(View.VISIBLE);
        firstCardView.startAnimation(slideUpAnimation);


    }

    @Override
    protected void onStart() {
        super.onStart();
        RelativeLayout rl = findViewById(R.id.ventanaInicial);
        rl.setVisibility(View.GONE);

        String authorization;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        authorization = prefs.getString("authorization", null);
        if (authorization != null){
            service.autologin(authorization).enqueue(new Callback<RespuestaLogin>() {
                @Override
                public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
                    RelativeLayout rl = findViewById(R.id.ventanaInicial);
                    if (response.isSuccessful()){
                        String [] values = authorization.split(" ");
                        Datainfo.resultLogin=response.body();
                        Datainfo.resultLogin.setToken_type(values[0]);
                        Datainfo.resultLogin.setAccess_token(values[1]);

                        if(Datainfo.resultLogin.getUser().getRol_id() == 2){
                            Intent logeo = new Intent(MainActivity.this, HomeEmpresario.class);
                            startActivity(logeo);
                            return;
                        } else if(Datainfo.resultLogin.getUser().getRol_id() == 3){
                            Intent logeo = new Intent(MainActivity.this, Home.class);
                            startActivity(logeo);
                            return;
                        }

                    } else {
                        Log.i("paso", "if");
                        rl.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<RespuestaLogin> call, Throwable t) {
                    Log.i("paso", "if2");
                    RelativeLayout rl = findViewById(R.id.ventanaInicial);
                    rl.setVisibility(View.VISIBLE);
                }
            });



        } else {
            rl.setVisibility(View.VISIBLE);
        }

    }
}