package com.ismr.sanjeronimoseguro.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.client.RegistroDatos;
import com.ismr.sanjeronimoseguro.models.Administrador;
import com.ismr.sanjeronimoseguro.models.Clientes;
import com.ismr.sanjeronimoseguro.providers.AdminProvider;
import com.ismr.sanjeronimoseguro.providers.AuthProvider;
import com.ismr.sanjeronimoseguro.providers.ClientProvider;

import dmax.dialog.SpotsDialog;

public class    RegistroDatosAdminActivity extends AppCompatActivity {
    //Toolbar mToolbar;
    //falta el mPref el cual se usa para cambiar de tipo de usuario
    //FirebaseAuth mAuth;
    //DatabaseReference mDatabase;
    AuthProvider mAuthProvider;
    AdminProvider mAdminProvider;


    //views
    Button mButtonRegister;
    TextInputEditText mTextImputNombre;
    TextInputEditText mTextImputCelular;
    TextInputEditText mTextImputEmail;
    TextInputEditText mTextImputPassword;

    AlertDialog mDialog;//para que aparezca e dialogo de carga


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_datos_admin);

        //MyToolbar.show(this, "registro de administrador", true);
        mAuthProvider = new AuthProvider();
        mAdminProvider = new AdminProvider();

        //mAuth = FirebaseAuth.getInstance();
        //mDatabase= FirebaseDatabase.getInstance().getReference();
        mButtonRegister = findViewById(R.id.btnFinalizar);
        mTextImputEmail = findViewById(R.id.textImputRegistroCorreo);
        mTextImputNombre = findViewById(R.id.textImpuRegistroNombre);
        mTextImputCelular = findViewById(R.id.textImpuRegistroCelular);
        mTextImputPassword = findViewById(R.id.textImputRegistroPassword);
        //falta el mPref=getApplicationContext().......    el cual se usa para escoger el tipo de usuario cponductor o usuario

        mDialog = new SpotsDialog.Builder().setContext(RegistroDatosAdminActivity.this).setMessage("Registrando Usuario").build();//para dialogo de carga

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegister();
                ;
            }
        });
    }

    void clickRegister() {
        final String name = mTextImputNombre.getText().toString();
        final String celular = mTextImputCelular.getText().toString();
        final String email = mTextImputEmail.getText().toString();
        final String password = mTextImputPassword.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !celular.isEmpty() && !password.isEmpty()) {
            if (password.length() >= 6) {
                mDialog.show();
                register(name, email, password, celular);
                /*mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mDialog.hide();
                        if(task.isSuccessful()){
                            //se llama al mettodo save user
                            String id = mAuth.getCurrentUser().getUid();
                           saveUser(id,name, email, celular);
                        }
                        else {
                            Toast.makeText(RegistroDatos.this, "No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            } else {
                Toast.makeText(this, "la contrasena debe tener almenos 6 caracterres", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, final String email, String password, String celular) {
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()) {
                    //se llama al mettodo save user
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Administrador admin = new Administrador(id, name, email, celular);
                    create(admin);
                } else {
                    Toast.makeText(RegistroDatosAdminActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Administrador admin) {
        mAdminProvider.create(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(RegistroDatosAdminActivity.this, "se registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroDatosAdminActivity.this,MapAdminActivity.class);
                    //para que el usuario una vez que entre a la actividad de mapa ya no pueda volver atras con el boton
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegistroDatosAdminActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
//creacion de metodo save User Guardar usuario se pasa usuario y ocntrasena
    /*void saveUser(String id, String name, String email, String celular){
        //String selectdUser = mPref.getString(s:"user", s1:"") usado para escoger el tipo de usuario a registrar
        User user= new User();
        user.setEmail(email);
        user.setName(name);
        user.setCelular(celular);
        //para usar el tipo de usuario se usa un if() y dentro del if el siguiente comando para registrar
        mDatabase.child("Users").child("clientes").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistroDatos.this, "el Registro se realizo exitosamente",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegistroDatos.this, "fallo el registro",Toast.LENGTH_SHORT).show();
                }
            }
        });// esto quiere decir que dentro de nuestro nodo principal en la base de datos creamos otro nodo llamado Users


    }*/