
package com.ismr.sanjeronimoseguro.providers;

//import com.firebase.geofire.GeoFireUtils;
import android.provider.MediaStore;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.sql.DatabaseMetaData;
public class GeofireProvider {
    private DatabaseReference mDatabase;
    private GeoFire mGeofire;

    public GeofireProvider (){
       // mDatabase= FirebaseDatabase.getInstance().getReference().child("Active_Admin");
      mDatabase= FirebaseDatabase.getInstance().getReference().child("Active_client");
       mGeofire = new GeoFire (mDatabase);
    }

   /* public void savelocation(String idAdmin, LatLng latLng){
        mGeofire.setLocation(idAdmin,new GeoLocation(latLng.latitude, latLng.longitude));//con esta linea se guarda la localizacon del usuario en database
    }*/
   public void savelocation(String idClient, LatLng latLng){
       mGeofire.setLocation(idClient,new GeoLocation(latLng.latitude, latLng.longitude));//con esta linea se guarda la localizacon del usuario en database
   }
    public void removeLocation(String idClient) {//metodo para eliminar la localizacion

       mGeofire.removeLocation(idClient);
    }
   /* public void removeLocation(String idAdmin){//metodo para eliminar la localizacion
        mGeofire.removeLocation(idAdmin);*/

    public GeoQuery getActiveClientes (LatLng latLng){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude), 20); //ver los clientes disponibles en un area de 20 km
        geoQuery.removeAllListeners();
        return geoQuery;
    }

}