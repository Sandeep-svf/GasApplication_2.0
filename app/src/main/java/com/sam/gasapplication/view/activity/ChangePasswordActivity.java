package com.sam.gasapplication.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.model.CommonSuccessMessageModel;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    AppCompatImageView back_arrow_change_password;
    AppCompatEditText old_Password_edittext,new_password_edittext,confirm_new_password_edittext;
    AppCompatButton update_password_button_layout;
    private String user_id;
    private AppCompatImageView hiden_password_image, c_hiden_password_image, visibale_password_image, c_visibale_password_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        inits();

        SharedPreferences sharedPreferences= ChangePasswordActivity.this.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");


        hiden_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibale_password_image.setVisibility(View.VISIBLE);
                hiden_password_image.setVisibility(View.GONE);
                new_password_edittext.setTransformationMethod(null);
                new_password_edittext.setSelection(new_password_edittext.getText().length());

            }
        });

        visibale_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiden_password_image.setVisibility(View.VISIBLE);
                visibale_password_image.setVisibility(View.GONE);

                new_password_edittext.setTransformationMethod(new PasswordTransformationMethod());
                new_password_edittext.setSelection(new_password_edittext.getText().length());
            }
        });


        c_hiden_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_visibale_password_image.setVisibility(View.VISIBLE);
                c_hiden_password_image.setVisibility(View.GONE);
                confirm_new_password_edittext.setTransformationMethod(null);
                confirm_new_password_edittext.setSelection(confirm_new_password_edittext.getText().length());

            }
        });

        c_visibale_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_hiden_password_image.setVisibility(View.VISIBLE);
                c_visibale_password_image.setVisibility(View.GONE);

                confirm_new_password_edittext.setTransformationMethod(new PasswordTransformationMethod());
                confirm_new_password_edittext.setSelection(confirm_new_password_edittext.getText().length());
            }
        });




        update_password_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation()){
                    chnage_password_api();
                }
            }
        });
        
        


        back_arrow_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void chnage_password_api() {
      
            // show till load api data

            final ProgressDialog pd = new ProgressDialog(ChangePasswordActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();


            Call<CommonSuccessMessageModel> call = API_Client.getClient().CHANGE_PASSWORD_COMMON_SUCCESS_MESSAGE_MODEL_CALL(user_id,
                    old_Password_edittext.getText().toString()
            ,new_password_edittext.getText().toString());

            call.enqueue(new Callback<CommonSuccessMessageModel>() {
                @Override
                public void onResponse(Call<CommonSuccessMessageModel> call, Response<CommonSuccessMessageModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = String.valueOf(response.body().getSuccess());


                            if (success.equals("true") || success.equals("True")) {

                                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                              

                            } else {
                                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
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
                    Log.e("conversion issue", t.getMessage());

                    if (t instanceof IOException) {
                        Toast.makeText(getApplicationContext(), "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
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
        
        if(old_Password_edittext.getText().toString().equals("")){
            Toast.makeText(ChangePasswordActivity.this, "Please enter old password", Toast.LENGTH_SHORT).show();
            return false;
            
        }else if(new_password_edittext.getText().toString().equals("")){
            Toast.makeText(ChangePasswordActivity.this, "Please enter new password", Toast.LENGTH_SHORT).show();
            return false;
        }else if(confirm_new_password_edittext.getText().toString().equals("")){
            Toast.makeText(ChangePasswordActivity.this, "Please confirm new password", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!confirm_new_password_edittext.getText().toString().equals(new_password_edittext.getText().toString())){
            Toast.makeText(ChangePasswordActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    private void inits() {
        hiden_password_image = findViewById(R.id.hiden_password_image);
        c_hiden_password_image = findViewById(R.id.c_hiden_password_image);
        visibale_password_image = findViewById(R.id.visibale_password_image);
        c_visibale_password_image = findViewById(R.id.c_visibale_password_image);
        update_password_button_layout = findViewById(R.id.update_password_button_layout);
        confirm_new_password_edittext = findViewById(R.id.confirm_new_password_edittext);
        new_password_edittext = findViewById(R.id.new_password_edittext);
        old_Password_edittext = findViewById(R.id.old_Password_edittext);
        back_arrow_change_password = findViewById(R.id.back_arrow_change_password);
    }
}