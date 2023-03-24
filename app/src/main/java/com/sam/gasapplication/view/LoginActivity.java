package com.sam.gasapplication.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.delivery.activity.DashboardDPActivity;
import com.sam.gasapplication.model.LoginModel;
import com.sam.gasapplication.view.activity.ForgotPasswordActivityActivity;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.app_logo_image_view)
    AppCompatImageView app_logo_image_view;

    @BindView(R.id.username_edittext)
    AppCompatEditText username_edittext;

    @BindView(R.id.password_edittext)
    AppCompatEditText password_edittext;

    @BindView(R.id.login_button)
    AppCompatButton login_button;

    @BindView(R.id.register_layout)
    AppCompatTextView register_layout;

    @BindView(R.id.forgot_passwoprd_text)
    AppCompatTextView forgot_passwoprd_text;

    @BindView((R.id.hiden_password_image))
    AppCompatImageView hiden_password_image;

    @BindView((R.id.visibale_password_image))
    AppCompatImageView visibale_password_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);



        hiden_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibale_password_image.setVisibility(View.VISIBLE);
                hiden_password_image.setVisibility(View.GONE);
                password_edittext.setTransformationMethod(null);
                password_edittext.setSelection(password_edittext.getText().length());

            }
        });

        visibale_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiden_password_image.setVisibility(View.VISIBLE);
                visibale_password_image.setVisibility(View.GONE);

                password_edittext.setTransformationMethod(new PasswordTransformationMethod());
                password_edittext.setSelection(password_edittext.getText().length());
            }
        });


        forgot_passwoprd_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivityActivity.class);
                startActivity(intent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validation()){
                    login_api();
                }

            }
        });

        register_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    private void get_form_data() {


    }

    private Boolean validation(){

        if(username_edittext.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(password_edittext.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login_api() {

        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        Call<LoginModel> call = API_Client.getClient().LOGIN_MODEL_CALL(username_edittext.getText().toString(),
                password_edittext.getText().toString());

        call.enqueue(new Callback<LoginModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                pd.dismiss();


                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();




                        if (success.equals("true") || success.equals("True")) {

                            LoginModel loginModel = response.body();

                            String userId = String.valueOf(loginModel.getUserId());
                            String userType = loginModel.getUserType();

                            SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = getUserIdData.edit();
                            editor.putString("userId", String.valueOf(userId));
                            editor.putString("userType", String.valueOf(userType));
                            editor.apply();

                            // alert_dialog_message("7");

                            Log.e("testdklfjskalf","userId is: "+userId);
                            Log.e("testdklfjskalf","userType is: "+userType);

                            if(userType.equals("0")){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }else if(userType.equals("1")){
                                Intent intent = new Intent(LoginActivity.this, DashboardDPActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "Something went wrong... while login.", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<LoginModel> call, Throwable t) {
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

}