package com.sam.gasapplication.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.adapter.NotificationAdapter;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.model.NotificationModel;
import com.sam.gasapplication.model.NotificationModel;
import com.sam.gasapplication.model.data.MyProfileModelData;
import com.sam.gasapplication.model.data.NotificationModelData;
import com.sam.gasapplication.view.activity.MyProfileActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    AppCompatImageView back_layout;
    RecyclerView rcv_notification;
    private String user_id="";
    List<NotificationModelData> notificationModelDataList = new ArrayList<>();
    AppCompatButton delete_all_notification_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        intis();

        SharedPreferences sharedPreferences= NotificationActivity.this.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delete_all_notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add pop up...
                final Dialog dialog = new Dialog(NotificationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.logout_dialog_notification_clear_all);
                LinearLayout noDialogLogout = dialog.findViewById(R.id.noDialogLogout);
                LinearLayout yesDialogLogout = dialog.findViewById(R.id.yesDialogLogout);


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                yesDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clear_all_notification_api();
                        dialog.dismiss();
                    }
                });


                noDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        get_notification_api();

    }

    private void clear_all_notification_api() {


            final ProgressDialog pd = new ProgressDialog(NotificationActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommonSuccessMessageModel> call = API_Client.getClient().DELETE_ALL_NOTIFICATION_COMMON_SUCCESS_MESSAGE_MODEL_CALL(user_id);

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
                                Toast.makeText(NotificationActivity.this, message, Toast.LENGTH_SHORT).show();
                                get_notification_api();
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

    private void get_notification_api() {

            final ProgressDialog pd = new ProgressDialog(NotificationActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<NotificationModel> call = API_Client.getClient().NOTIFICATION_MODEL_CALL(user_id);

            call.enqueue(new Callback<NotificationModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {

                                notificationModelDataList = response.body().getData();

                                if(notificationModelDataList.isEmpty()){
                                    delete_all_notification_button.setVisibility(View.GONE);
                                    Toast.makeText(NotificationActivity.this, "Notification list is empty...", Toast.LENGTH_SHORT).show();
                                }else{
                                    delete_all_notification_button.setVisibility(View.VISIBLE);
                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this,RecyclerView.VERTICAL,false);
                                rcv_notification.setLayoutManager(linearLayoutManager);
                                NotificationAdapter notificationAdapter = new NotificationAdapter(NotificationActivity.this,notificationModelDataList,user_id);
                                rcv_notification.setAdapter(notificationAdapter);


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
                public void onFailure(Call<NotificationModel> call, Throwable t) {

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
        delete_all_notification_button = findViewById(R.id.delete_all_notification_button);
        back_layout = findViewById(R.id.back_layout);
        rcv_notification = findViewById(R.id.rcv_notification);
    }
}