package com.sam.gasapplication.delivery.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
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

import com.google.android.libraries.places.internal.gx;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.adapter.NotificationAdapter;
import com.sam.gasapplication.delivery.adapter.OrderDetailsAdapter;
import com.sam.gasapplication.delivery.adapter.OrderListAdapterDP;
import com.sam.gasapplication.delivery.model.OrderDetailsModel;
import com.sam.gasapplication.delivery.model.OrderDetailsModelData;
import com.sam.gasapplication.delivery.model.OrderListModelDP;
import com.sam.gasapplication.delivery.model.data.OrderDetailsModelDataList;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.view.NotificationActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {

    private String orderNumber,user_id,userType="0";
    private List<OrderDetailsModelDataList> orderDetailsModelDataListList = new ArrayList<>();
    private LinearLayoutCompat all_button_layout;
    private AppCompatImageView back_arrow;
    private AppCompatTextView order_number,name,phone_number,order_status,order_date,total_ammount,address;
    private RecyclerView rcv_item_list;
    private AppCompatButton otw_button,c_button,d_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        otw_button = findViewById(R.id.otw_button);
        c_button = findViewById(R.id.c_button);
        d_button = findViewById(R.id.d_button);
        rcv_item_list = findViewById(R.id.rcv_item_list);
        address = findViewById(R.id.address);
        total_ammount = findViewById(R.id.total_ammount);
        order_date = findViewById(R.id.order_date);
        order_status = findViewById(R.id.order_status);
        phone_number = findViewById(R.id.phone_number);
        order_number = findViewById(R.id.order_number);
        all_button_layout = findViewById(R.id.all_button_layout);
        name = findViewById(R.id.name);
        back_arrow = findViewById(R.id.back_arrow);

        SharedPreferences sharedPreferences= getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");




        try {
            orderNumber = getIntent().getStringExtra("orderNumnber");
            userType = getIntent().getStringExtra("userType");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if(userType.equals("0")){
            all_button_layout.setVisibility(View.GONE);
        }else if(userType.equals("1")){
            all_button_layout.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(OrderDetailsActivity.this, "something went wrong.. while filtering user type.", Toast.LENGTH_SHORT).show();
        }

        if(orderNumber.isEmpty()) Toast.makeText(OrderDetailsActivity.this, "Failed to load order number...", Toast.LENGTH_SHORT).show();


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        order_details_api();


        otw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                on_the_way_api();
            }
        });

        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                final Dialog dialog = new Dialog(OrderDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.cancel_item);
                LinearLayout noDialogLogout = dialog.findViewById(R.id.noDialogLogout);
                LinearLayout yesDialogLogout = dialog.findViewById(R.id.yesDialogLogout);
                AppCompatEditText reason_layout = dialog.findViewById(R.id.reason_layout);


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                yesDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel_order_api(reason_layout.getText().toString());
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

        d_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(OrderDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.deliverd_item);
                LinearLayout noDialogLogout = dialog.findViewById(R.id.noDialogLogout);
                LinearLayout yesDialogLogout = dialog.findViewById(R.id.yesDialogLogout);
                AppCompatEditText reason_layout = dialog.findViewById(R.id.reason_layout);


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                yesDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deliverd_api();
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


    }

    private void deliverd_api() {





            final ProgressDialog pd = new ProgressDialog(OrderDetailsActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommonSuccessMessageModel> call = API_Client.getClient().DELIVERD_COMMON_SUCCESS_MESSAGE_MODEL_CALL(orderNumber);

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
                                Intent intent = new Intent(OrderDetailsActivity.this, OrderListDpActivity.class);
                                startActivity(intent);
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

    private void cancel_order_api(String cancelReason) {
      



            final ProgressDialog pd = new ProgressDialog(OrderDetailsActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();


            Call<CommonSuccessMessageModel> call = API_Client.getClient().CANCEL_ORDER_COMMON_SUCCESS_MESSAGE_MODEL_CALL(user_id,orderNumber,cancelReason);

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

                                Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(OrderDetailsActivity.this, OrderListDpActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(OrderDetailsActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(OrderDetailsActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(OrderDetailsActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(OrderDetailsActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(OrderDetailsActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(OrderDetailsActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(OrderDetailsActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(OrderDetailsActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(OrderDetailsActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(OrderDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(OrderDetailsActivity.this, "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(OrderDetailsActivity.this, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });
        }

    private void on_the_way_api() {


            Log.e("user_id","user_id :"+user_id);

            final ProgressDialog pd = new ProgressDialog(OrderDetailsActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommonSuccessMessageModel> call = API_Client.getClient().ON_THE_WAY_COMMON_SUCCESS_MESSAGE_MODEL_CALL(orderNumber);

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
                                Intent intent = new Intent(OrderDetailsActivity.this, OrderListDpActivity.class);
                                startActivity(intent);
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

    private void order_details_api() {

        Log.e("user_id","user_id :"+user_id);

            final ProgressDialog pd = new ProgressDialog(OrderDetailsActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<OrderDetailsModel> call = API_Client.getClient().ORDER_DETAILS_MODEL_CALL(user_id,orderNumber);

            call.enqueue(new Callback<OrderDetailsModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<OrderDetailsModel> call, Response<OrderDetailsModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {

                                OrderDetailsModel orderDetailsModel = response.body();
                                OrderDetailsModelData orderDetailsModelData = orderDetailsModel.getData();

                                order_number.setText(orderDetailsModelData.getOrderNumber());
                                name.setText(orderDetailsModelData.getName());
                                address.setText(orderDetailsModelData.getAddress());
                                phone_number.setText(orderDetailsModelData.getPhone());
                                total_ammount.setText(String.valueOf(orderDetailsModelData.getTotalAmount()));
                                String oStatus = orderDetailsModelData.getOrderStatus();
                                if(oStatus.equals("0")){
                                    order_status.setText("Processing your order");
                                }else if(oStatus.equals("1")){
                                    order_status.setText("On the way");
                                }else if(oStatus.equals("2")){
                                    order_status.setText("Not Delivered");
                                }else if(oStatus.equals("3")){
                                    order_status.setText("Delivered");
                                }else{
                                    Toast.makeText(OrderDetailsActivity.this, "something went wrong.. while fetching order status value from APIs.", Toast.LENGTH_SHORT).show();
                                }
                              
                                order_date.setText(orderDetailsModelData.getOrderedDate());


                                orderDetailsModelDataListList = orderDetailsModelData.getProductData();

                                Log.e("list_size","list_size :"+orderDetailsModelDataListList.size());

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this, RecyclerView.HORIZONTAL,false);
                                rcv_item_list.setLayoutManager(linearLayoutManager);
                                OrderDetailsAdapter notificationAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this,orderDetailsModelDataListList);
                                rcv_item_list.setAdapter(notificationAdapter);


                                /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                                rcv_order_list_dp.setLayoutManager(linearLayoutManager);
                                OrderListAdapterDP orderHistoryAdapter = new OrderListAdapterDP(user_id,modelDataDPList, OrderListDpActivity.this);
                                rcv_order_list_dp.setAdapter(orderHistoryAdapter);*/

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
                public void onFailure(Call<OrderDetailsModel> call, Throwable t) {

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