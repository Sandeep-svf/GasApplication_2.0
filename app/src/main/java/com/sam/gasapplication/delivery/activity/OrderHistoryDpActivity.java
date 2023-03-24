package com.sam.gasapplication.delivery.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.adapter.OrderHistoryAdapter;
import com.sam.gasapplication.model.OrderHistoryModel;
import com.sam.gasapplication.model.data.OrderHistoryData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDpActivity extends AppCompatActivity {

    RecyclerView rcv_order_history;
    List<OrderHistoryData> orderHistoryDataList = new ArrayList<>();
    private String user_id,userType;
    AppCompatImageView back_arrow;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_dp);

        rcv_order_history = findViewById(R.id.rcv_order_history);
        back_arrow = findViewById(R.id.back_arrow);

        SharedPreferences sharedPreferences= getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");
        userType=sharedPreferences.getString("userType","");


        get_order_history_api();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void get_order_history_api() {

        final ProgressDialog pd = new ProgressDialog(OrderHistoryDpActivity.this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        Call<OrderHistoryModel> call = API_Client.getClient().ORDER_HISTORY_MODEL_CALL(user_id);

        call.enqueue(new Callback<OrderHistoryModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<OrderHistoryModel> call, Response<OrderHistoryModel> response) {
                pd.dismiss();


                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();




                        if (success.equals("true") || success.equals("True")) {

                            orderHistoryDataList = response.body().getData();

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderHistoryDpActivity.this, LinearLayoutManager.VERTICAL,false);
                            rcv_order_history.setLayoutManager(linearLayoutManager);
                            OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryDpActivity.this,orderHistoryDataList,userType);
                            rcv_order_history.setAdapter(orderHistoryAdapter);


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
            public void onFailure(Call<OrderHistoryModel> call, Throwable t) {
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