package com.ismr.sanjeronimoseguro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.admin.LoginAdminActivity;
import com.ismr.sanjeronimoseguro.activities.admin.MapAdminActivity;
import com.ismr.sanjeronimoseguro.activities.client.MapClientActivity;
import com.ismr.sanjeronimoseguro.activities.client.MenuInicioActivity;

public class MainActivity extends AppCompatActivity {
    Button mButtonAcceder;
    Button mButtonCrearCuenta;
    Button mButtonAccederAdmin;//para hacer pruevas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //para quitar el acionbar y el  titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //-------------------------------
        setContentView(R.layout.activity_main);
        mButtonAcceder= findViewById(R.id.btnAcceder);
        mButtonCrearCuenta= findViewById(R.id.btnCrearCuenta);
        //-------------para hacer pruevas
        mButtonAccederAdmin= findViewById(R.id.btnAccederAdmin);
        mButtonAccederAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aqui iva---> goToSelectAuth();
                goToSelectLoginAdmin();
            }
        });
        //--------------

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
    //---------------- usar este metodo para que puedas mantener la secion iniciada en el intent se pisociona en que ventana se abrio
    @Override
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            //String user =mPref.getString(s: "user", s1: "")  (usar estos metodos esta en vid. 22min.8:13)
            //Intent intent = new Intent(MainActivity.this, MapAdminActivity.class);
            Intent intent = new Intent(MainActivity.this, MenuInicioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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
    //----para hace rpruevas --------------
    private void goToSelectLoginAdmin(){
        Intent intent= new Intent(MainActivity.this, LoginAdminActivity.class);
        startActivity(intent);
    }
}