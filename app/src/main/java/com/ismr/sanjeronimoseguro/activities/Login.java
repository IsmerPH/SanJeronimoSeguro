package com.ismr.sanjeronimoseguro.activities;

//import androidx.appcompat.app.ActionBar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.client.MapClientActivity;
import com.ismr.sanjeronimoseguro.activities.client.MenuInicioActivity;
import com.ismr.sanjeronimoseguro.activities.client.RegistroDatos;
import com.ismr.sanjeronimoseguro.includes.MyToolbar;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {
   Toolbar mToolbar;
   TextInputEditText mTextImputEmail;
   TextInputEditText mTextImputPassword;
   Button mButtonLogin;
   FirebaseAuth mAuth;
   DatabaseReference mDatabase;
   AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyToolbar.show(this,"login de usuario", true);

        mTextImputEmail = findViewById(R.id.textImputEmail);
        mTextImputPassword = findViewById(R.id.textImputPassword);
        mButtonLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDialog = new SpotsDialog.Builder().setContext(Login.this).setMessage("Espere un momento").build();
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }
    private void login(){
        String email = mTextImputEmail.getText().toString();
        String password = mTextImputPassword.getText().toString();
        if (!email.isEmpty()&& !password.isEmpty()){
            if (password.length()>= 6){
                mDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            /*String user = mPref.getString(s:"user", s1:"");
                            if (user.equals("client")){   aqui entra el intent para que accesa den caso de que sea usuario cliente}
                            else{}si no es usuaruo es coductor aqui entra si el usuario es conductor*/
                            //Intent intent = new Intent(Login.this, MapClientActivity.class);
                            Intent intent = new Intent(Login.this, MenuInicioActivity.class);
                            //para que el usuario una vez que entre a la actividad de mapa ya no pueda volver atras con el boton
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                           // Toast.makeText(Login.this, "el login se realizo exitosamente",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Login.this, "contrasena incorrecta",Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
            }
            else {
                Toast.makeText(this, "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "La contraseña y el email son obligatorios", Toast.LENGTH_SHORT).show();
        }
    }
}