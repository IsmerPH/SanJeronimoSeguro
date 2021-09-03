package com.ismr.sanjeronimoseguro.providers;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismr.sanjeronimoseguro.activities.client.RegistroDatos;
import com.ismr.sanjeronimoseguro.models.Clientes;

import java.util.HashMap;
import java.util.Map;

public class ClientProvider {
    DatabaseReference mDatabase;
    public ClientProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("clientes");
    }
    public Task<Void> create(Clientes client){
        Map<String, Object> map = new HashMap<>();//para que el id no paraezca en la base de datos junto a el nombre y email
        map.put("name", client.getName());
        map.put("email", client.getName());
        map.put("celular", client.getName());
        return mDatabase.child(client.getId()).setValue(map);
    }


    public DatabaseReference getClient(String idClient) {
        return mDatabase.child(idClient);
    }

}
