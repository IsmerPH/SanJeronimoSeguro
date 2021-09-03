package com.ismr.sanjeronimoseguro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.admin.RegistroDatosAdminActivity;

public class CrearCuenta extends AppCompatActivity {
    Button mButtonRecibirCodigo;
    Button mButtonAdministrador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        mButtonRecibirCodigo = findViewById(R.id.btnRecibirCodigo);
        mButtonAdministrador = findViewById(R.id.btnAdministrador);
        mButtonRecibirCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectRegistro1();
            }
        });
        mButtonAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectAdministrador();
            }
        });
    }
    private void goToSelectRegistro1(){
        Intent intent= new Intent(CrearCuenta.this, Registro1.class);
        startActivity(intent);
    }
    private void goToSelectAdministrador(){
        Intent intent= new Intent( CrearCuenta.this, RegistroDatosAdminActivity.class);
        startActivity(intent);
    }
}