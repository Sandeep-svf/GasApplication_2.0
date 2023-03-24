package com.sam.gasapplication.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.MyProfileModel;
import com.sam.gasapplication.model.data.MyProfileModelData;
import com.sam.gasapplication.utility.BookingActivity;
import com.sam.gasapplication.utility.MapActivity;
import com.sam.gasapplication.utility.SearchMapActivity;
import com.sam.gasapplication.utility.StaticKeyClass;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderAddressActivity extends AppCompatActivity {

    AppCompatButton submit_address_button;
    AppCompatEditText username_edittext,phone_edittext,landmark_edittext;
    AppCompatEditText current_location_edittext;
    AppCompatButton select_lat_long_layout;
    private String user_id = "";
    private String address= "";
    private Double latitude, longitude;
    private AppCompatImageView place_order_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_address);

        inits();

        SharedPreferences sharedPreferences= PlaceOrderAddressActivity.this.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");

        try {
            SharedPreferences latlongobj= PlaceOrderAddressActivity.this.getSharedPreferences("LAT_LONG", Context.MODE_PRIVATE);
            StaticKeyClass.latitude = latlongobj.getString("latitude","");
            StaticKeyClass.longitude = latlongobj.getString("longitude","");
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(PlaceOrderAddressActivity.this, "Please add address from MAP.", Toast.LENGTH_SHORT).show();
        }

        try {
            address = getIntent().getStringExtra("address");

        } catch (Exception exception) {
            exception.printStackTrace();
        }


        get_user_details_api();

        place_order_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select_lat_long_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceOrderAddressActivity.this, BookingActivity.class);
                startActivity(intent);
            }
        });

        submit_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()){
                    Intent intent = new Intent(PlaceOrderAddressActivity.this, PlaceOrderPaymentActivity.class);
                    intent.putExtra("name",username_edittext.getText().toString());
                    intent.putExtra("address",current_location_edittext.getText().toString());
                    intent.putExtra("landmark",landmark_edittext.getText().toString());
                    intent.putExtra("phoneNumber",phone_edittext.getText().toString());
                    intent.putExtra("lat",StaticKeyClass.latitude);
                    intent.putExtra("long",StaticKeyClass.longitude);
                    startActivity(intent);
                }

            }
        });


    }


    private void get_user_details_api() {

        final ProgressDialog pd = new ProgressDialog(PlaceOrderAddressActivity.this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        Call<MyProfileModel> call = API_Client.getClient().MY_PROFILE_MODEL_CALL(user_id);

        call.enqueue(new Callback<MyProfileModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<MyProfileModel> call, Response<MyProfileModel> response) {
                pd.dismiss();


                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();


                        if (success.equals("true") || success.equals("True")) {

                            MyProfileModel myProfileModel = response.body();
                            MyProfileModelData myProfileModelData = myProfileModel.getData();

                            try {
                                username_edittext.setText(myProfileModelData.getName());
                                current_location_edittext.setText(myProfileModelData.getAddress());
                                phone_edittext.setText(myProfileModelData.getPhone());
                                landmark_edittext.setText(myProfileModelData.getLandmark());

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }


                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();

                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getApplicationContext(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getApplicationContext(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getApplicationContext(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getApplicationContext(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getApplicationContext(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getApplicationContext(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getApplicationContext(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyProfileModel> call, Throwable t) {

                if (t instanceof IOException) {
                    Toast.makeText(getApplicationContext(), "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });


    }


    private boolean validation() {
        if(username_edittext.getText().toString().equals("")){
            Toast.makeText(PlaceOrderAddressActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(phone_edittext.getText().toString().equals("")){
            Toast.makeText(PlaceOrderAddressActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }else if(landmark_edittext.getText().toString().equals("")){
            Toast.makeText(PlaceOrderAddressActivity.this, "Please enter landmark", Toast.LENGTH_SHORT).show();
            return false;
        }else if(current_location_edittext.getText().toString().equals("")){
            Toast.makeText(PlaceOrderAddressActivity.this, "Please select location from map", Toast.LENGTH_SHORT).show();
            return false;
        }else if(StaticKeyClass.latitude.equals("")){
            Toast.makeText(PlaceOrderAddressActivity.this, "Please select address from  map carefully", Toast.LENGTH_SHORT).show();
            return  false;
        }else if(StaticKeyClass.longitude.equals("")){
            Toast.makeText(PlaceOrderAddressActivity.this, "Please select address from  map carefully", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

 /*   @Override
    protected void onStart() {
        super.onStart();
        try {
            current_location_edittext.setText(StaticKeyClass.currentLocation);
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(PlaceOrderAddressActivity.this, "something went wrong... while setting up  current address", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*  @Override
    protected void onRestart() {
        super.onRestart();
        try {
            current_location_edittext.setText(StaticKeyClass.currentLocation);
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(PlaceOrderAddressActivity.this, "something went wrong... while setting up  current address", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(PlaceOrderAddressActivity.this, "Your selected address is: "+StaticKeyClass.currentLocation, Toast.LENGTH_SHORT).show();
        current_location_edittext.setText(StaticKeyClass.currentLocation);
        Log.e("ADDRESS",StaticKeyClass.currentLocation);
        Log.e("ADDRESS",StaticKeyClass.latitude);
        Log.e("ADDRESS",StaticKeyClass.longitude);

    }

    private void inits() {

        place_order_back_arrow = findViewById(R.id.place_order_back_arrow);
        select_lat_long_layout = findViewById(R.id.select_lat_long_layout);
        current_location_edittext = findViewById(R.id.current_location_edittext);
        landmark_edittext = findViewById(R.id.landmark_edittext);
        phone_edittext = findViewById(R.id.phone_edittext);
        username_edittext = findViewById(R.id.username_edittext);
        submit_address_button = findViewById(R.id.submit_address_button);
    }
}