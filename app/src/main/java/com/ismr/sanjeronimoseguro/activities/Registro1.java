package com.ismr.sanjeronimoseguro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.client.RegistroDatos;

public class Registro1 extends AppCompatActivity {
    Button mButtonCodigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro1);
        mButtonCodigo = findViewById(R.id.btnCodigo);
        mButtonCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectFormulario();
            }
        });
    }
    private void goToSelectFormulario(){
        Intent intent= new Intent(Registro1.this, RegistroDatos.class);
        startActivity(intent);
    }
}