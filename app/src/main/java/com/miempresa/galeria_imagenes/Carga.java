package com.miempresa.galeria_imagenes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Carga extends AppCompatActivity {


    TextView appname, desarrollador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        //Cambio de fuente de letra
        String ubicacion = "fuente/MagicMushroom.otf";
        Typeface tf = Typeface.createFromAsset(Carga.this.getAssets(),ubicacion);
        //Cambio de fuente de letra
        appname = findViewById(R.id.app_name);
        desarrollador = findViewById(R.id.desarrollador);

        final int DURACION = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Codigo que se ejecutara al iniciar la app
                Intent intent = new Intent(Carga.this, MainActivityAdministrador.class);
                startActivity(intent);
                finish();
            }
        },DURACION);
        appname.setTypeface(tf);
        desarrollador.setTypeface(tf);

    }
}