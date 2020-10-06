package com.example.desafio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.desafio.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirMapa(View view) {

        startActivity(new Intent(this, MapsActivity.class));
    }

    public void abrirLinhas(View view) {

        startActivity(new Intent(this, ListaLinhas.class));
    }
}