package com.sam.gasapplication.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.model.CommonSuccessMessageModel;


import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends AppCompatActivity {

    AppCompatImageView back_arrow_support;
    AppCompatEditText user_name_CU,user_email_CF,phone_number_CF,user_messages_CF;
    AppCompatButton submit_contact_us_form_button;
    private String regexEmail = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[A-Za-z0-9]:(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        intis();
        back_arrow_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit_contact_us_form_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()){
                    support_api();
                }
            }
        });





    }

    private boolean validation() {

        if(user_name_CU.getText().toString().equals("")){
            Toast.makeText(SupportActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            return false;
        } else   if(user_email_CF.getText().toString().equals("")){
            Toast.makeText(SupportActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!user_email_CF.getText().toString().matches(regexEmail)){
            Toast.makeText(SupportActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else  if(phone_number_CF.getText().toString().equals("")){
            Toast.makeText(SupportActivity.this, "Please enter phone numner", Toast.LENGTH_SHORT).show();
            return false;
        } else   if(user_messages_CF.getText().toString().equals("")){
            Toast.makeText(SupportActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void support_api() {
  

            final ProgressDialog pd = new ProgressDialog(SupportActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommonSuccessMessageModel> call = API_Client.getClient().SUPPORT_COMMON_SUCCESS_MESSAGE_MODEL_CALL("",user_name_CU.getText().toString()
            ,user_email_CF.getText().toString(),phone_number_CF.getText().toString(),user_messages_CF.getText().toString());

            call.enqueue(new Callback<CommonSuccessMessageModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<CommonSuccessMessageModel> call, Response<CommonSuccessMessageModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();

                            if (success.equals("true") || success.equals("True")) {

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                pd.dismiss();

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
                public void onFailure(Call<CommonSuccessMessageModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
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

    private void intis() {

        submit_contact_us_form_button = findViewById(R.id.submit_contact_us_form_button);
        user_messages_CF = findViewById(R.id.user_messages_CF);
        phone_number_CF = findViewById(R.id.phone_number_CF);
        user_email_CF = findViewById(R.id.user_email_CF);
        user_name_CU = findViewById(R.id.user_name_CU);
        back_arrow_support = findViewById(R.id.back_arrow_support);
    }
}