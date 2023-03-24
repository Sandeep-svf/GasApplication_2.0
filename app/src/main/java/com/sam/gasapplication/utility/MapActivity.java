package com.sam.gasapplication.utility;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.sam.gasapplication.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 125460;
    double longitudes = 0.00, latitudes = 0.00;
    String currentlatitude = "0.00", currentlongtide = "0.00";
    GPSTracker gps;
    GPSTracker gpsTracker;
    private GoogleMap mMap;
    RelativeLayout relatice_back_change_password;
    private String key, locationStatus, user_id;
    private double mLatitude, mLongitude;
    public static String string;
    public static String lat_long;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView search_address_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        search_address_layout = findViewById(R.id.search_address_layout);

        search_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setInitialQuery("Noida").build(MapActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this);

       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                string = String.valueOf(latLng);
                Log.e("latLng_Data", string);
                // Setting the position for the marker
                markerOptions.position(latLng);
                Log.e("Lat_Long", String.valueOf(latLng));
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0F));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });*/






    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        my_marker();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String title = marker.getTitle();

                Toast.makeText(getApplicationContext(), title + "", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void my_marker() {

            try {
                /*latitudes = Double.parseDouble(getIntent().getStringExtra("lattitude"));
                longitudes = Double.parseDouble(getIntent().getStringExtra("longitude"));*/

                String city = getIntent().getStringExtra("city");
                createMarker(latitudes, longitudes, city);


            } catch (Exception e) {

            }

    }

    public void createMarker(double latitude, double longitude, String comppanyname) {
        LatLng latLng = new LatLng(latitude, longitude);
        lat_long = latitude + "," + longitude;


            string = String.valueOf(latLng);
            Log.e("latLng_Data", string);
            MarkerOptions options = new MarkerOptions().position(latLng).title(comppanyname).icon(BitmapFromVector(getApplicationContext(), R.drawable.location));
            mMap.addMarker(options);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 13.0F);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(yourLocation);

              /*  Log.e("latLng" , String.valueOf(latLng));
           addMarker(latLng);*/

            if (locationStatus.equals("0")) {

            }else{

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        string = String.valueOf(latLng);
                        Log.e("latLng_Data", string);
                        // Setting the position for the marker
                        markerOptions.position(latLng);
                        Log.e("Lat_Long", String.valueOf(latLng));
                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Clears the previously touched position
                        mMap.clear();

                        // Animating to the touched position
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0F));

                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);
                    }
                });
            }

        }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        //vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        //Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        // below line is use to add bitmap in our canvas.
        //Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        //vectorDrawable.draw(canvas);

        Bitmap sccale = Bitmap.createScaledBitmap(((BitmapDrawable)vectorDrawable).getBitmap(), 96, 96, false);


        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(sccale);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Place place = Autocomplete.getPlaceFromIntent(data);

                    LatLng latLng = place.getLatLng();
                  //  locationDailyWeather(latLng.latitude, latLng.longitude);

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 5);
                    Address address = addresses.get(0);
                    //search_location.setText(place.getName());

                } catch (IOException e) {

                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
               /* Logger.analyser(context, Logger.LoggerMessage.UPDATE_PROFILE_ACTIVITY, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                        "An error occurred: " + status.getStatusMessage()
                );*/
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
    }

}