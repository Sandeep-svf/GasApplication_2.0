package com.sam.gasapplication.utility;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.intuz.addresspicker.LocationProvider;
import com.sam.gasapplication.R;

import java.util.ArrayList;
import java.util.List;

public class RouteMapActivity extends AppCompatActivity implements LocationProvider.LocationCallback, OnMapReadyCallback {


    private String TAG = "so47492459";

    int indexOfLocation = -1;
    private static final int PLACE_PICKUP_REQUEST_CODE = 1001;
    private static final int PLACE_DESTINATION_REQUEST_CODE = 1002;
    TextView pickupPointTxt, deliveryPointTxt;
    LinearLayout back_arrow_button;
    List<Address> addresses;
    String pickupLatitude = "", pickupLongitude = "", addressLinePick = "", addressLineDrop = "",
            dropupLongitude = "", dropupLatitude = "";

    private LocationProvider locationProvider;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private boolean currentMap = false;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 8088;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;
    private double mLatitude, mLongitude;
    List<Address> addressList;
    String PickupCity = "", PickupZipCode = "", PickupTownName = "", PickupBuildingNum = "", dropupCity = "",
            state = "", country = "", userAddress = "", dropupBuildingNum = "", dropupTownName = "", dropupZipCode = "";
    private boolean firstTime = true;
    private boolean secondTime = true;
    private AppCompatImageView back_arrow;


    String strDateTimePass = "", bookType = "", subscriberNo = "";
    public static String place;
    AppCompatTextView search_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        getLocationPermission();
        checkAndRequestPermissions();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationProvider = new LocationProvider(RouteMapActivity.this, RouteMapActivity.this);
        locationProvider.connect();


    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void getMyLocation() {
        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                mMap.setMyLocationEnabled(true);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
                mMap.setOnMyLocationChangeListener(myLocationChangeListener);

            } else {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            }
        }
    }


    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (NetworkHelper.isNetworkAvailable(RouteMapActivity.this)) {
                if (currentMap) {
                    currentMap = false;
                    Toast.makeText(RouteMapActivity.this, "Internet Connected", Toast.LENGTH_LONG).show();
                }

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else {
                if (currentMap == false) {
                    currentMap = true;
                    Toast.makeText(RouteMapActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

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
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        getMyLocation();

        LatLng barcelona = new LatLng(41.385064,2.173403);
        mMap.addMarker(new MarkerOptions().position(barcelona).title("Marker in Barcelona"));

        LatLng madrid = new LatLng(40.416775,-3.70379);
        mMap.addMarker(new MarkerOptions().position(madrid).title("Marker in Madrid"));

        LatLng zaragoza = new LatLng(41.648823,-0.889085);

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBr4fC60qcaJzTdfJJLWdIRDjXy9IohH4s")
                .build();








        if (checkLocationPermission()) {
            checkAndRequestPermissions();
        } else {
            return;
        }


    }

    @Override
    public void handleNewLocation(Location location) {

    }
}