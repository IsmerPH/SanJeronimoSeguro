package com.ismr.sanjeronimoseguro.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.MainActivity;
import com.ismr.sanjeronimoseguro.activities.client.MapClientActivity;
import com.ismr.sanjeronimoseguro.includes.MyToolbar;
import com.ismr.sanjeronimoseguro.providers.AuthProvider;
import com.ismr.sanjeronimoseguro.providers.GeofireProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapAdminActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private AuthProvider mAutProvider;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;//propiedad
    private GeofireProvider mGeofireProvider;

    private final static int LOCATION_REQUEST_CODE = 1; //Es para saber si debe solicitar los permisos de ubicacion.
    private final static int SETTINGS_REQUEST_CODE = 2;
    private Marker mMarker;//propiedad
    private LatLng mCurrentLatLng;

    private List<Marker> mClientesMarkers = new ArrayList<>(); // se crea una lista para agrecar esos marcadores de los clientes.
    private boolean mIsFirstTime = true; // SE CREA UNA BARIABLE BOOLEANA PARA

   // private LatLng mCurrentLatLng;  para guardar la ubicacion en una variable local linea 67 vid 29 min4
    LocationCallback mLocationCallback = new LocationCallback() { //collbacck que escucha cada vez que el usuario se mueva
        @Override
        public void onLocationResult(LocationResult locationResult) {//metodo
            for (Location location : locationResult.getLocations()) {//un for para recorrer  una propoedad llamada locatin
                if (getApplicationContext() != null) {// hacemos una validacion donde preguntamos si el contexto de la aplicacion es difernete de nulo
                    //obtendremos la localizacion del usuario en tiempo real
       //             mCurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude()); // se agrego para poder guardar la ubicacion en una variable global
                    //------------------------------------------
                    if (mMarker != null){
                        mMarker.remove();
                    }
                    mCurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                    mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(),location.getLongitude())
                            )
                                    .title("Tu Posición")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.sos))
                    );
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))//target()la posicion que se ubica la camara
                                    .zoom(10f)
                                    .build()
                    ));
                    if (mIsFirstTime){
                        mIsFirstTime = false;
                        getActiveClientes();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_admin);
        MyToolbar.show(this, "Mapa Admin", false);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mMapFragment.getMapAsync(this);
        mAutProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider();
    }
    private void getActiveClientes(){//metodo para ver los clientes activos
        mGeofireProvider.getActiveClientes(mCurrentLatLng).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {//metodo donde iremos anadiendo los marcadores de los conductores que se iran conectando
                for (Marker marker: mClientesMarkers) {
                    if (marker.getTag()!=null){
                        if(marker.getTag().equals(key)){
                            return;
                        }
                    }
                }
                LatLng clienteLatLng = new LatLng(location.longitude,location.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(clienteLatLng).title("usuario disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.sos)));
                marker.setTag(key);
                mClientesMarkers.add(marker);
            }

            @Override
            public void onKeyExited(String key) {//metodo para eliminara los clienetes que se iran desconectando de la aplicacion
                for (Marker marker: mClientesMarkers) {
                    if (marker.getTag()!=null){
                        if(marker.getTag().equals(key)){
                            marker.remove();
                            mClientesMarkers.remove(marker);//de esta manera se elimina el marcador de la lista
                            return;
                        }
                    }
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) { //vamos a ir actualzando en tiempo reAL la posicion de los clientes
                for (Marker marker: mClientesMarkers) {
                    if (marker.getTag()!=null){
                        if(marker.getTag().equals(key)){
                            marker.setPosition(new LatLng(location.latitude,location.longitude));
                        }
                    }
                }

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);//los botones para hacer car y alejar el mapa
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //  intervalo de tiempo en que se a estar actualizando la ubicacion del usuario en el mapa
        mLocationRequest.setFastestInterval(1000);
        //la prioridad que va establecer el gps para estar trabajando en la actualizacion de la ubicacion y para que  haga uso del gps con la mayor precicion
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        startLocation();
    }
    // para pedir permiso vamos a sobreescribir un metodo
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //PERMISSION_GRANTED permiso consedido
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()){
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(false);
                    }
                    else{
                        ShowAlertDialogNOGPS();
                    }

                } else {
                    checkLocationPermisssions();
                }
            } else {
                checkLocationPermisssions();
            }
        }
    }
    //sobreescribinos un metodo para realizar la ccion del gps y activarlo


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            //agrego este codigo en nuevas verciones de android vid.25 min 9:51
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(false);
        }
        else{
            ShowAlertDialogNOGPS();
        }
    }

    //metodo donde vamos a crear un aler dialog que le permita al usuario ir a las configueraciones en caso de que no tenga activo el gps
    private void ShowAlertDialogNOGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicación para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),SETTINGS_REQUEST_CODE);//este metodo va estar ejecutandose y va esperar que el usuario ejecute una accion como activar el gps
                    }
                }).create().show();
    }

        //metodo paa hacer que aparezca una notificacion de activar el gps en caso de que lo tengamos desactivado saber si el usuario tiene el gps activado
     private boolean gpsActived(){
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActive=true;
        }
        return isActive;
     }
     //metodo para iniciar el escuchador de nuestra unbicacion vid 25 min 1:40
    private void startLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//si la vercion de android e mayor igual al la verion mashmelow
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {//si estan los permisos concedisos por el usuario
                if (gpsActived()){
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(false);
                }
                else{
                    ShowAlertDialogNOGPS();
                }
            }
            else{
                checkLocationPermisssions();
            }
        }else{// si la vercion no es android masmelow entonces
            if(gpsActived()){
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(false);
            }
            else {
                ShowAlertDialogNOGPS();
            }
        }
    }

     //crear un metodo para ver que pasa si un usuario no acepta los permisos
    private void checkLocationPermisssions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicación para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //esta linea habilita los permisos para utilizar la ubicacion del celular
                                ActivityCompat.requestPermissions(MapAdminActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();//para que se muestre el aler dialog
            }
            else{
                ActivityCompat.requestPermissions(MapAdminActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }
   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.action_logout){
            Logout();
        }
        return super.onOptionsItemSelected(item);
    }
    void Logout(){
        mAutProvider.logout();
        Intent intent = new Intent(MapAdminActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}