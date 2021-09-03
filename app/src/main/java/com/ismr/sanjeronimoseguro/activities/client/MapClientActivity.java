package com.ismr.sanjeronimoseguro.activities.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.ismr.sanjeronimoseguro.R;
import com.ismr.sanjeronimoseguro.activities.MainActivity;
import com.ismr.sanjeronimoseguro.activities.admin.MapAdminActivity;
import com.ismr.sanjeronimoseguro.includes.MyToolbar;
import com.ismr.sanjeronimoseguro.providers.AuthProvider;
import com.ismr.sanjeronimoseguro.providers.GeofireProvider;

public class MapClientActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Button mButtonLogout;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAutProvider;
    private GeofireProvider mGeofireProvider;//instanciamos el geofire provider
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;//propiedad

    private final static int LOCATION_REQUEST_CODE = 1; //Es para saber si debe solicitar los permisos de ubicacion.
    private final static int SETTINGS_REQUEST_CODE = 2;

    private Marker mMarker;//propiedad
    private LatLng mCurrentLatLng; //para guardar la ubicacion en una variable local linea 67 vid 29 min4  --> localizacion actual

    LocationCallback mLocationCallback = new LocationCallback() { //collbacck que escucha cada vez que el usuario se mueva
        @Override
        public void onLocationResult(LocationResult locationResult) {//metodo
            for (Location location : locationResult.getLocations()) {//un for para recorrer  una propoedad llamada locatin
                if (getApplicationContext() != null) {// hacemos una validacion donde preguntamos si el contexto de la aplicacion es difernete de nulo
                        mCurrentLatLng = new LatLng(location.getLatitude(),location.getLongitude());//localizacion actual del conductor
                    if (mMarker != null){
                        mMarker.remove();
                    }
                    mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(),location.getLongitude())
                            )
                            .title("Tu Posición")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.sos))
                    );
                    //obtendremos la localizacion del usuario en tiempo real
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))//target()la posicion que se ubica la camara
                                    .zoom(18f)
                                    .build()
                    ));
                    updateLocation();//guardar locacion
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client);
        MyToolbar.show(this, "Mapa Usuario", false);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mMapFragment.getMapAsync(this);
        mAutProvider = new AuthProvider();
        mGeofireProvider= new GeofireProvider();//inicializamos el geofire provider
    }
    //creacion de metodo updateLocation para guardar la ubicacion
    private void updateLocation(){
        //if(mAutProvider.existSession() && mCurrentLatLng != null){// si l session existe y ademas es diferente de nulo
            mGeofireProvider.savelocation(mAutProvider.getId(),mCurrentLatLng); //almacenamos la ubicacion en la bas de datod
       // }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {//metodo para ver el mapa
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);//los botones para hacer car y alejar el mapa

        //mMap.setMyLocationEnabled(true);//para ver el icono de la ubicacion exacta en el mapa
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //  intervalo de tiempo en que se a estar actualizando la ubicacion del usuario en el mapa
        mLocationRequest.setFastestInterval(1000);
        //la prioridad que va establecer el gps para estar trabajando en la actualizacion de la ubicacion y para que  haga uso del gps con la mayor precicion
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        //generalmente se activa con el boton de conectarse para que se inicialize la visualizacion de conectarse
        startLocation();//la localizacion se esta ejecutando solo cuando ingresan al mapa si queremos otro lugar utilizar este metodo
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
                        mMap.setMyLocationEnabled(false);//para ver el icono de la ubicacion exacta en el mapa
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
            mMap.setMyLocationEnabled(false);//para ver el icono de la ubicacion exacta en el mapa
        }
        else{
            ShowAlertDialogNOGPS();
        }
    }

    //metodo donde vamos a crear un aler dialog que le permita al usuario ir a las configueraciones en cxaso de que no tenga activo el gps
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
    //metoso para descnectar al conductor
   /* private void disconnect(){
        if(mFusedLocation != null){
            //mButtonConnect.setText("conectarse");
            //mIsConnect =false;
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            if (mAutProvider.existSession()){

            }
            mGeofireProvider.removeLocation(mAutProvider.getId());
        }
        else{
            Toast.makeText(this,"No te puedes desconectar", Toast.LENGTH_SHORT).show();
        }
    }*/


    //metodo para iniciar el escuchador de nuestra unbicacion vid 25 min 1:40
    private void startLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//si la vercion de android e mayor igual al la verion mashmelow
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {//si estan los permisos concedisos por el usuario
                if (gpsActived()){
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(false);//para ver el icono de la ubicacion exacta en el mapa
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
                mMap.setMyLocationEnabled(false);//para ver el icono de la ubicacion exacta en el mapa
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
                                ActivityCompat.requestPermissions(MapClientActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();//para que se muestre el aler dialog
            }
            else{
                ActivityCompat.requestPermissions(MapClientActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
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
    void Logout(){//meodo para cerrar secion
        //deberiamos poner el metodo disconect por que una vez que el usuario ciwerre secion ya no se debe de mostrar la ubicacion
        //disconect();//para eliminarse os datos de su ubicacion en firebase y va detener la actualizacion de ubicacion en tiempo real
        mAutProvider.logout();
        Intent intent = new Intent(MapClientActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    void generateToken() {//generar token para enviar notificaciones

    }
}