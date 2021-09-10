package com.examensupletorio.visorlugares;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.examensupletorio.visorlugares.helpers.RetrofitHelper;
import com.examensupletorio.visorlugares.helpers.SnackbarHelper;
import com.examensupletorio.visorlugares.model.Location;
import com.examensupletorio.visorlugares.model.NearbyPlacesResult;
import com.examensupletorio.visorlugares.service.IGoogleMapsService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISION_REQUEST_CODE = 123456;
    private static final int NEARBY_SEARCH_RADIUS = 100;
    private Spinner spinnerPlaceType;
    private FloatingActionButton fabFind;
    private SupportMapFragment googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation = new Location();
    List<String> placeTypes = new ArrayList() {
        {
            add("restaurant");
            add("airport");
            add("atm");
            add("train_station");
            add("transit_station");
            add("travel_agency");
            add("university");
            add("veterinary_care");
            add("zoo");
        }
    };

    List<String> placeTypesLabels = new ArrayList() {
        {
            add("Restaurante");
            add("Aeropuerto");
            add("Cajero automático");
            add( "Estación de tren");
            add( "Estación de tránsito");
            add( "Agencia de viajes");
            add( "Universidad");
            add( "Veterinaria");
            add("Zoológico");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerPlaceType = findViewById(R.id.spinnerPlaceType);
        fabFind = findViewById(R.id.fabFind);
        fabFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(requirePermissions())
                    {
                        getPlaces();
                    }

                } catch (Exception e) {
                    SnackbarHelper.showInfiniteMessage(MainActivity.this.spinnerPlaceType, "Error al obtener los datos.", "Cerrar");
                }
            }
        });
        googleMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);

        spinnerPlaceType.setAdapter(new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, placeTypes));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requirePermissions();
    }

    public void getPlaces() throws Exception {

        getCurrentLocation();
        Call<NearbyPlacesResult> call = RetrofitHelper.getService(IGoogleMapsService.class)
                .getPlaces(placeTypes.get(spinnerPlaceType.getSelectedItemPosition()),
                        NEARBY_SEARCH_RADIUS, String.format("%f,%f", currentLocation.getLat(), currentLocation.getLng()), this.getString(R.string.google_maps_api_key) );

        call.enqueue(new Callback<NearbyPlacesResult>() {
            @Override
            public void onResponse(Call<NearbyPlacesResult> call, Response<NearbyPlacesResult> response) {
                if (response.isSuccessful()) {

                    NearbyPlacesResult places = response.body();
                    int a=5;
                    //TODO

                }
                else
                {
                    SnackbarHelper.showInfiniteMessage(MainActivity.this.spinnerPlaceType, "Error al obtener los datos.", "Cerrar");
                }
            }

            @Override
            public void onFailure(Call<NearbyPlacesResult> call, Throwable t) {
                SnackbarHelper.showInfiniteMessage(MainActivity.this.spinnerPlaceType, "Error al conectarse al servicio de mapas.", "Cerrar");
            }
        });


    }

    public void getCurrentLocation()
    {
        @SuppressLint("MissingPermission") Task<android.location.Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if(location!= null)
                {
                    currentLocation.setLat(location.getLatitude());
                    currentLocation.setLng(location.getLongitude());
                }
            }
        });
    }

    private boolean requirePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            return true;

        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISION_REQUEST_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISION_REQUEST_CODE)
        {
            if(grantResults.length > 0)
            {
                for(int result: grantResults)
                {
                    if(result == PackageManager.PERMISSION_DENIED)
                    {
                        return;
                    }
                }
                //Get Location
            }

        }
    }
}





//            put("accounting", "");
//            put("amusement_park", "");
//            put("aquarium", "");
//            put("art_gallery", "");
//            put("bakery", "");
//            put("bank", "Banco");
//            put("bar", "");
//            put("beauty_salon", "");
//            put("bicycle_store", "");
//            put("book_store", "");
//            put("bowling_alley", "");
//            put("bus_station", "");
//            put("cafe", "");
//            put("campground", "");
//            put("car_dealer", "");
//            put("car_rental", "");
//            put("car_repair", "");
//            put("car_wash", "");
//            put("casino", "");
//            put("cemetery", "");
//            put("church", "");
//            put("city_hall", "");
//            put("clothing_store", "");
//            put("convenience_store", "");
//            put("courthouse", "");
//            put("dentist", "");
//            put("department_store", "");
//            put("doctor", "");
//            put("drugstore", "");
//            put("electrician", "");
//            put("electronics_store", "");
//            put("embassy", "");
//            put("fire_station", "");
//            put("florist", "");
//            put("funeral_home", "");
//            put("furniture_store", "");
//            put("gas_station", "");
//            put("gym", "");
//            put("hair_care", "");
//            put("hardware_store", "");
//            put("hindu_temple", "");
//            put("home_goods_store", "");
//            put("hospital", "");
//            put("insurance_agency", "");
//            put("jewelry_store", "");
//            put("laundry", "");
//            put("lawyer", "");
//            put("library", "");
//            put("light_rail_station", "");
//            put("liquor_store", "");
//            put("local_government_office", "");
//            put("locksmith", "");
//            put("lodging", "");
//            put("meal_delivery", "");
//            put("meal_takeaway", "");
//            put("mosque", "");
//            put("movie_rental", "");
//            put("movie_theater", "");
//            put("moving_company", "");
//            put("museum", "");
//            put("night_club", "");
//            put("painter", "");
//            put("park", "");
//            put("parking", "");
//            put("pet_store", "");
//            put("pharmacy", "");
//            put("physiotherapist", "");
//            put("plumber", "");
//            put("police", "");
//            put("post_office", "");
//            put("primary_school", "");
//            put("real_estate_agency", "");
//            put("roofing_contractor", "");
//            put("rv_park", "");
//            put("school", "");
//            put("secondary_school", "");
//            put("shoe_store", "");
//            put("shopping_mall", "");
//            put("spa", "");
//            put("stadium", "");
//            put("storage", "");
//            put("store", "");
//            put("subway_station", "");
//            put("supermarket", "");
//            put("synagogue", "");
//            put("taxi_stand", "");
//            put("tourist_attraction", "");