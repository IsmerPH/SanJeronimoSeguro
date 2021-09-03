package com.ismr.sanjeronimoseguro.includes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ismr.sanjeronimoseguro.R;

public class MyToolbar {
    public static void show(AppCompatActivity activity, String title, boolean upButton){//s le pasa tres parametros la actividad, el titulo que llevara el toolbar y un booleano de si queremos que aparesca el boton de atras
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);


    }
}
