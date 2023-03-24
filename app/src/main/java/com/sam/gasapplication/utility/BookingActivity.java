package com.sam.gasapplication.utility;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;



import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intuz.addresspicker.LocationProvider;
import com.intuz.addresspicker.MapRequestResponse;
import com.intuz.addresspicker.MapServices;
import com.intuz.addresspicker.MapUtility;
import com.sam.gasapplication.R;
import com.sam.gasapplication.view.activity.PlaceOrderAddressActivity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingActivity extends AppCompatActivity implements LocationProvider.LocationCallback, OnMapReadyCallback {
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
    String PickupCity = "", PickupZipCode = "",PickupTownName="", PickupBuildingNum = "", dropupCity="",
            state = "", country = "", userAddress = "",dropupBuildingNum="",dropupTownName="",dropupZipCode="";
    private boolean firstTime = true;
    private boolean secondTime = true;
    private AppCompatImageView back_arrow;


    String strDateTimePass="",bookType="",subscriberNo="";
    public static String  place;
    AppCompatTextView search_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        back_arrow = findViewById(R.id.back_arrow);
        pickupPointTxt = findViewById(R.id.pickupPointTxt);
        deliveryPointTxt = findViewById(R.id.deliveryPointTxt);
        back_arrow_button = findViewById(R.id.back_arrow_button);
        search_layout = findViewById(R.id.search_layout);
      /*  confirmPickUpDropBtn = findViewById(R.id.confirmPickUpDropBtn);*/

        back_arrow_button.setOnClickListener(view -> finish());

        place = getIntent().getStringExtra("place");
        strDateTimePass = getIntent().getStringExtra("strDateTimePass");
        bookType = getIntent().getStringExtra("bookType");
        subscriberNo = getIntent().getStringExtra("subscriberNo");


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                indexOfLocation = -2;
                Intent intent = new Intent(BookingActivity.this, SearchMapActivity.class);
                startActivity(intent);
            }
        });

      //  confirmPickUpDropBtn.setVisibility(View.GONE);

        pickupPointTxt.setOnClickListener(view -> {

           /* Intent intent = new Intent(BookingActivity.this, LocationActivity.class);
            startActivityForResult(intent, PLACE_PICKUP_REQUEST_CODE);*/
        });

        deliveryPointTxt.setOnClickListener(view -> {
            indexOfLocation = -3;
          /*  Intent intent = new Intent(BookingActivity.this, LocationActivity.class);
            startActivityForResult(intent, PLACE_DESTINATION_REQUEST_CODE);*/
        });




        getLocationPermission();
        checkAndRequestPermissions();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationProvider = new LocationProvider(BookingActivity.this, BookingActivity.this);
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

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if (NetworkHelper.isNetworkAvailable(BookingActivity.this)) {
                if (currentMap) {
                    currentMap = false;
                    Toast.makeText(BookingActivity.this, "Internet Connected", Toast.LENGTH_LONG).show();
                }

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else {
                if (currentMap == false) {
                    currentMap = true;
                    Toast.makeText(BookingActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private void addMarker(LatLng coordinate) {
        mLatitude = coordinate.latitude;
        mLongitude = coordinate.longitude;
        if (mMap != null) {
            LatLng latLng = new LatLng(mLatitude, mLongitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        getMyLocation();





       /* mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mMap.isIndoorEnabled()) {
            mMap.setIndoorEnabled(false);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);*/

       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mLatitude = latLng.latitude;
                mLongitude = latLng.longitude;
                addMarker(latLng);
                new BookingActivity.getAddressForLocation(mLatitude, mLongitude).execute();
            }
        });*/

        if (checkLocationPermission()) {
            checkAndRequestPermissions();
        } else {
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void handleNewLocation(Location location) {
        if (location != null) {
            Log.e("BookActive", "   firstTime");
            MapUtility.currentLocation = location;

            if (place == null) {
                if (firstTime) {
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();

                    pickupLatitude = String.valueOf(location.getLatitude());
                    pickupLongitude = String.valueOf(location.getLongitude());

                    Log.e("BookActive", "   firstTime");
                    new getAddressForLocation(MapUtility.currentLocation.getLatitude(), MapUtility.currentLocation.getLongitude()).execute();
                    firstTime = false;
                    secondTime = false;
                }
            } else {
                if (secondTime) {
                    Log.e("BookActive", "   secondTime");
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(place, 5);
                        Address locations = addressList.get(0);
                        //  addressLineDrops = String.valueOf(locations.getAddressLine(0));
                        pickupPointTxt.setText("" + userAddress);
                        LatLng latLng = new LatLng(locations.getLatitude(), locations.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(place));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    secondTime = false;
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getAddressForLocation extends AsyncTask<Void, Void, Void> {
        Double latitude, longitude;

        public getAddressForLocation(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(BookingActivity.this, Locale.getDefault());
                StringBuilder sb = new StringBuilder();

                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {

                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null)
                        sb.append(address).append(" ");
                    PickupBuildingNum = addresses.get(0).getFeatureName();
                    PickupTownName = addresses.get(0).getSubLocality();
                    PickupCity = addresses.get(0).getLocality();
                    if (PickupCity != null)
                        sb.append(PickupCity).append(" ");

                    state = addresses.get(0).getAdminArea();
                    if (state != null)
                        sb.append(state).append(" ");
                    country = addresses.get(0).getCountryName();
                    if (country != null)
                        sb.append(country).append(" ");

                    PickupZipCode = addresses.get(0).getPostalCode();
                    if (PickupZipCode != null)
                        sb.append(PickupZipCode).append(" ");
                    userAddress = sb.toString();
                    StaticKeyClass.currentLocation =  userAddress;
                    StaticKeyClass.latitude = String.valueOf(latitude);
                    StaticKeyClass.longitude = String.valueOf(longitude);


                    SharedPreferences latlongobj = getSharedPreferences("LAT_LONG", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = latlongobj.edit();
                    editor.putString("latitude", String.valueOf( StaticKeyClass.latitude));
                    editor.putString("longitude", String.valueOf(StaticKeyClass.longitude));
                    editor.apply();


                    try {
                        search_layout.setText(userAddress);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    Log.e("Books",  " " + PickupCity + " " + PickupBuildingNum + " " + PickupTownName);
                    Log.e("Books",  " " +userAddress);
                    //Log.e("Books",PickupCity+" "+addresses.get(0).getSubLocality()+" "+addresses.get(0).getSubAdminArea());
                }
            } catch (IOException e) {
                e.printStackTrace();
                showLocation((new LatLng(latitude, longitude)));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (userAddress == null || userAddress.equalsIgnoreCase("")) {
                showLocation((new LatLng(latitude, longitude)));
            } else {
                MarkerOptions markerOptions;
                try {
                    mMap.clear();
                    pickupPointTxt.setText("" + userAddress);
                    Log.e("BookActive", "   thirdTime");
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(userAddress));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                } catch (Exception ex) {
                }
            }
        }
    }

    private void showLocation(final LatLng latLng1) {
        if (!MapUtility.isNetworkAvailable(this)) {

            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(BookingActivity.this, "No internet connection available.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MapUtility.showProgress(BookingActivity.this);
                }
            });
            MapServices.MapInterface mapInterface = MapServices.getClient();
            Call<MapRequestResponse> call;
            String s = latLng1.latitude + "," + latLng1.longitude;

            call = mapInterface.MapData(s);
            call.enqueue(new Callback<MapRequestResponse>() {
                @Override
                public void onResponse(Call<MapRequestResponse> call, Response<MapRequestResponse> response) {
                    MapUtility.hideProgress();
                    if (response.isSuccessful()) {
                        MapRequestResponse result = response.body();
                        if (result.getStatus().equalsIgnoreCase("OK")) {
                            if (result.getResults().size() > 0) {
                                final MarkerOptions[] markerOptions = {null};
                                try {
                                    mMap.clear();

                                    userAddress = result.getResults().get(0).getFormatted_address();
                                    pickupPointTxt.setText("" + userAddress);

                                    if (userAddress != null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("BookActive", "   fourthTime");
                                                markerOptions[0] = new MarkerOptions().position(latLng1).title((userAddress)).icon(BitmapDescriptorFactory.fromResource(com.intuz.addresspicker.R.drawable.ic_place_red_800_24dp));
                                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng1, 16.0f);
                                                mMap.moveCamera(cameraUpdate);
                                                mMap.addMarker(markerOptions[0]).showInfoWindow();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace(); // getFromLocation() may sometimes fail
                                }
                            }
                        }
                    } else {
                        MapUtility.hideProgress();
                    }
                }

                @Override
                public void onFailure(Call<MapRequestResponse> call, Throwable t) {
                    t.printStackTrace();
                    MapUtility.hideProgress();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    locationProvider = new LocationProvider(BookingActivity.this, BookingActivity.this);
                    locationProvider.connect();
                }
            }
        }
    }

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

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Books","Calling Resume..."+place);
        if (indexOfLocation == -2) {
            if (place != null) {

                List<Address> addressLists = null;
                Geocoder geocoder;
                geocoder = new Geocoder(BookingActivity.this);
                try {
                    addressLists = geocoder.getFromLocationName(place, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                StringBuilder sb = new StringBuilder();
                String address = addressLists.get(0).getAddressLine(0);
                if (address != null)
                    sb.append(address).append(" ");
                PickupBuildingNum = addressLists.get(0).getFeatureName();
                PickupTownName = addressLists.get(0).getSubLocality();
                PickupCity = addressLists.get(0).getLocality();
                if (PickupCity != null)
                    sb.append(PickupCity).append(" ");

                state = addressLists.get(0).getAdminArea();
                if (state != null)
                    sb.append(state).append(" ");
                country = addressLists.get(0).getCountryName();
                if (country != null)
                    sb.append(country).append(" ");

                PickupZipCode = addressLists.get(0).getPostalCode();
                if (PickupZipCode != null)
                    sb.append(PickupZipCode).append(" ");
                userAddress = sb.toString();

                Address locations = addressLists.get(0);
                mLatitude = locations.getLatitude();
                mLongitude = locations.getLongitude();

                Log.e("Books", "   Address  " + mLatitude + " " + PickupTownName + " " );

                pickupLatitude = String.valueOf(locations.getLatitude());
                pickupLongitude = String.valueOf(locations.getLongitude());

                new getAddressForLocation(mLatitude, mLongitude).execute();
            }

            //getLocationAddress(BookingActivity.this, place);
        } else if (indexOfLocation == -3) {
           // getLocationDestination(BookingActivity.this, place);
        }
    }

   /* public LatLng getLocationAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        LatLng p1 = null;

        if (strAddress == null) {
        } else {
            try {
                addresses = coder.getFromLocationName(strAddress, 5);
                if (addresses == null) {
                    return null;
                }

                Address location = addresses.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());

                pickupLatitude = String.valueOf(location.getLatitude());
                pickupLongitude = String.valueOf(location.getLongitude());
                addressLinePick = String.valueOf(location.getAddressLine(0));

                if (addressLinePick.equals(addressLineDrop)) {
                    Toast.makeText(BookingActivity.this, getString(R.string.pickup_address_destination), Toast.LENGTH_SHORT).show();
                } else if (!pickupLatitude.equals("") || !dropupLongitude.equals("")) {
                    if (!pickupLatitude.equals("") && !dropupLongitude.equals("")) {
                        pickupPointTxt.setText(addressLinePick);
                        // GetDisatance_Api();

                    } else if (!pickupLatitude.equals("")) {
                        pickupPointTxt.setText(addressLinePick);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return p1;
    }*/
/*
    public LatLng getLocationDestination(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        LatLng p1 = null;

        if (strAddress == null) {
        } else {
            try {
                // May throw an IOException
                addresses = coder.getFromLocationName(strAddress, 5);
                if (addresses == null) {
                    return null;
                }

                Address location = addresses.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());

                dropupLatitude = String.valueOf(location.getLatitude());
                dropupLongitude = String.valueOf(location.getLongitude());
                addressLineDrop = String.valueOf(location.getAddressLine(0));

                dropupBuildingNum = addresses.get(0).getFeatureName();
                dropupTownName = addresses.get(0).getSubLocality();
                dropupCity = addresses.get(0).getLocality();
                dropupZipCode = addresses.get(0).getPostalCode();

                if (addressLinePick.equals(addressLineDrop)) {
                   // Toast.makeText(BookingActivity.this, getString(R.string.pickup_address_destination), Toast.LENGTH_SHORT).show();
                } else if (!pickupLatitude.equals("") || !dropupLongitude.equals("")) {
                    if (!pickupLatitude.equals("") && !dropupLongitude.equals("")) {
                        deliveryPointTxt.setText(addressLineDrop);
                        confirmPickUpDropBtn.setVisibility(View.VISIBLE);
                        // GetDisatance_Api();
                    } else if (!dropupLongitude.equals("")) {
                        deliveryPointTxt.setText(addressLineDrop);
                        confirmPickUpDropBtn.setVisibility(View.VISIBLE);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return p1;
    }*/
}