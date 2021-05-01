package com.ismr.sanjeronimoseguro;

//import androidx.appcompat.app.ActionBar;

import android.app.AlertDialog;
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
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("xxxxxx");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                            Toast.makeText(Login.this, "el login se realizo exitosamente",Toast.LENGTH_SHORT).show();
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