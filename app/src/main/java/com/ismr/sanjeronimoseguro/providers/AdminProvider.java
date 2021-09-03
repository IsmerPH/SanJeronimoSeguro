package com.ismr.sanjeronimoseguro.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismr.sanjeronimoseguro.models.Administrador;
import com.ismr.sanjeronimoseguro.models.Clientes;

import java.util.HashMap;
import java.util.Map;

public class AdminProvider {
    DatabaseReference mDatabase;
    public AdminProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("administrador");
    }
    public Task<Void> create(Administrador admin){
        Map<String, Object> map = new HashMap<>();//para que el id no paraezca en la base de datos junto a el nombre y email
        map.put("name", admin.getName());
        map.put("email", admin.getName());
        map.put("celular", admin.getName());
        return mDatabase.child(admin.getId()).setValue(map);
    }
}
