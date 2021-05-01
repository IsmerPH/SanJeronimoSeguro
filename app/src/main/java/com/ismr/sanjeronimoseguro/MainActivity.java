package com.ismr.sanjeronimoseguro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button mButtonAcceder;
    Button mButtonCrearCuenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonAcceder= findViewById(R.id.btnAcceder);
        mButtonCrearCuenta= findViewById(R.id.btnCrearCuenta);
        mButtonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectLogin();
            }
        });
        mButtonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectCrearCuenta();
            }
        });
    }
    //aqui iva---> goToSelectAuth();
    private void goToSelectLogin(){
        Intent intent= new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
    private void goToSelectCrearCuenta(){
        Intent intent= new Intent(MainActivity.this, CrearCuenta.class);
        startActivity(intent);
    }
}