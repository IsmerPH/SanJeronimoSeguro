package com.ismr.sanjeronimoseguro.services;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingClient extends FirebaseMessagingService{        //se ereda una clase de firebase messages


    @Override
    public void onNewToken(@NonNull String s) { // este metodo nos servira para generar un token de usuario con el fin de enviar notificaciones de dispositivo a dispositivo
        super.onNewToken(s);
    }

    @Override     //overide sobnreescribir un metodo
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
