package com.ismr.sanjeronimoseguro.activities.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.Login;
import com.ismr.sanjeronimoseguro.activities.MainActivity;
import com.ismr.sanjeronimoseguro.activities.admin.MapAdminActivity;
import com.ismr.sanjeronimoseguro.databinding.ActivityLoginBuscarCelularBinding;

public class MenuInicioActivity extends AppCompatActivity {
    Button mButtonAlertar;
    Button mButtonAlertar2;
    Button mButtonUbicarCelular;
    Button mButtonColectividad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        mButtonAlertar= findViewById(R.id.btnAlertar);
        mButtonAlertar2= findViewById(R.id.btnAlerta2);
        mButtonColectividad= findViewById(R.id.btnServicioCole);
        mButtonUbicarCelular= findViewById(R.id.btnUbicarCel);
        mButtonAlertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectAlertar();
            }
        });

        mButtonAlertar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectAlertar();
            }
        });
        mButtonColectividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectColectividad();
            }
        });
        mButtonUbicarCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectUbicarCel();
            }
        });

    }
    private void goToSelectAlertar(){
        Intent intent= new Intent(MenuInicioActivity.this,MapClientActivity.class);
        startActivity(intent);
    }
    private void goToSelectUbicarCel(){
        Intent intent= new Intent(MenuInicioActivity.this,loginBuscarCelularActivity.class);
        startActivity(intent);
    }private void goToSelectColectividad(){
        Intent intent= new Intent(MenuInicioActivity.this, ServicioColectividadActivity.class);
        startActivity(intent);
    }
}