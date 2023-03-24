package com.sam.gasapplication.view.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.adapter.CartListAdapter;
import com.sam.gasapplication.model.CartListModel;
import com.sam.gasapplication.model.IdQuntityListModel;
import com.sam.gasapplication.model.PlaceOrderModel;
import com.sam.gasapplication.model.TotalAmountModel;
import com.sam.gasapplication.model.data.CartListModelData;
import com.sam.gasapplication.utility.StaticKeyClass;
import com.sam.gasapplication.view.fragment.CartListFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderPaymentActivity extends AppCompatActivity {


    AppCompatButton place_order_button;
    AppCompatTextView payment_mode_text,total_amount_text;
    AppCompatImageView place_order_back_arrow;
    private String user_id;
    private String custmerName="", custmerPhoneNumber="", custmerAddress="", custmerLandmark="", latitude="", longitude="";

    private List<CartListModelData> cartListModelDataList = new ArrayList<>();
    private List<IdQuntityListModel> idQuntityListModelList = new ArrayList<>();


    IdQuntityListModel idQuntityListModel = new IdQuntityListModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_payment);

        try {
            custmerName = getIntent().getStringExtra("name");
            custmerAddress = getIntent().getStringExtra("address");
            custmerLandmark = getIntent().getStringExtra("landmark");
            custmerPhoneNumber = getIntent().getStringExtra("phoneNumber");
            latitude = getIntent().getStringExtra("lat");
            longitude = getIntent().getStringExtra("long");
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(PlaceOrderPaymentActivity.this, "Something went wrong while loading custmer data...", Toast.LENGTH_SHORT).show();
        }


        total_amount_text = findViewById(R.id.total_amount_text);
        place_order_back_arrow = findViewById(R.id.place_order_back_arrow);
        place_order_button = findViewById(R.id.place_order_button);
        payment_mode_text = findViewById(R.id.payment_mode_text);


        SharedPreferences sharedPreferences= getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");

        
        total_amount_api();
        cart_list_api();

        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                place_order_api();


            }
        });

        place_order_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });






    }

    private void total_amount_api() {
     




            Call<TotalAmountModel> call = API_Client.getClient().TOTAL_AMOUNT_MODEL_CALL(user_id);

            call.enqueue(new Callback<TotalAmountModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<TotalAmountModel> call, Response<TotalAmountModel> response) {



                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();
                            String totalAmount = String.valueOf(response.body().getTotalAmount());

                            if (success.equals("true") || success.equals("True")) {

                                TotalAmountModel totalAmountModel = response.body();

                                total_amount_text.setText(String.valueOf(totalAmountModel.getTotalAmount())+"INR");



                            } else {


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
                public void onFailure(Call<TotalAmountModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
                    if (t instanceof IOException) {
                        Toast.makeText(getApplicationContext(), "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }

    private void place_order_api() {


        try {

            JSONObject paramObject = new JSONObject();
            paramObject.put("user_id", user_id);
            paramObject.put("name", custmerName);
            paramObject.put("mobileNo", custmerPhoneNumber);
            paramObject.put("delivery_charge", "50");
            paramObject.put("current_location", custmerAddress);
            paramObject.put("total_amount", "1250");
            paramObject.put("latitude", latitude);
            paramObject.put("longitude", longitude);


            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObjectDestination = new JSONObject();

            for(int i=0; i< idQuntityListModelList.size() ; i++ ){
                jsonObjectDestination.put("id", idQuntityListModelList.get(i).getId());
                jsonObjectDestination.put("qty",idQuntityListModelList.get(i).getQuantity() );
                jsonArray.put(jsonObjectDestination);

            }

            paramObject.put("id", jsonArray);

            Log.e("orderNumber","paramObject is :"+paramObject.toString());



        final ProgressDialog pd = new ProgressDialog(PlaceOrderPaymentActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<PlaceOrderModel> call = API_Client.getClient().PLACE_ORDER_MODEL_CALL(paramObject.toString());

            call.enqueue(new Callback<PlaceOrderModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();
                            String orderNumber = String.valueOf(response.body().getOrderNumber());
                            String totalAmount = String.valueOf(response.body().getTotalAmount());
                            String userName = String.valueOf(response.body().getUserName());
                            String email = String.valueOf(response.body().getUserEmail());
                            String address = String.valueOf(response.body().getUserAddress());
                            String cartCount = String.valueOf(response.body().getCartCount());
                            String userId = String.valueOf(response.body().getUserId());

                            if (success.equals("true") || success.equals("True")) {
                             //   Toast.makeText(PlaceOrderPaymentActivity.this, message, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                                Log.e("orderNumber","orderNumber is :"+orderNumber);
                                Intent intent = new Intent(PlaceOrderPaymentActivity.this, WebActivity.class);
                                intent.putExtra("orderNumber",orderNumber);
                                intent.putExtra("totalAmount",totalAmount);
                                intent.putExtra("userName",userName);
                                intent.putExtra("email",email);
                                intent.putExtra("address",address);
                                intent.putExtra("cartCount",cartCount);
                                intent.putExtra("userId",userId);
                                startActivity(intent);
                            } else {
                                Toast.makeText(PlaceOrderPaymentActivity.this, message, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(PlaceOrderPaymentActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();

                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(PlaceOrderPaymentActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(PlaceOrderPaymentActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
                    if (t instanceof IOException) {
                        Toast.makeText(PlaceOrderPaymentActivity.this, "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(PlaceOrderPaymentActivity.this, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        }

    private void cart_list_api() {

        final ProgressDialog pd = new ProgressDialog(PlaceOrderPaymentActivity.this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        Call<CartListModel> call = API_Client.getClient().CART_LIST_MODEL_CALL(user_id);

        call.enqueue(new Callback<CartListModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {
                pd.dismiss();


                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();
                        String totalAmount = String.valueOf(response.body().getTotalAmount());

                        if (success.equals("true") || success.equals("True")) {

                            cartListModelDataList = response.body().getCartList();

                            try {
                                if(cartListModelDataList.isEmpty()){
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "You don't have any item in your cart yet.", Toast.LENGTH_SHORT).show();
                                }else{
                                    for(int i = 0; i < cartListModelDataList.size(); i++){

                                        idQuntityListModel.setId(String.valueOf(cartListModelDataList.get(i).getId()));
                                        idQuntityListModel.setQuantity(String.valueOf(cartListModelDataList.get(i).getQuantity()));
                                        idQuntityListModelList.add(idQuntityListModel);
                                    }
                                }

                            } catch (Exception exception) {
                                exception.printStackTrace();
                                Toast.makeText(PlaceOrderPaymentActivity.this, "Something went wring while loading quantity and id from cart list.....", Toast.LENGTH_SHORT).show();
                                Log.e("size","exception :"+String.valueOf(exception));
                            }

                            Log.e("size","cartListModelDataList :"+cartListModelDataList.size());
                            Log.e("size","idQuntityListModelList :"+idQuntityListModelList.size());

                        } else {
                            Toast.makeText(PlaceOrderPaymentActivity.this, message, Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }


                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(PlaceOrderPaymentActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();

                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(PlaceOrderPaymentActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } catch (Exception e) {
                            Toast.makeText(PlaceOrderPaymentActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CartListModel> call, Throwable t) {
                Log.e("bhgyrrrthbh", String.valueOf(t));
                if (t instanceof IOException) {
                    Toast.makeText(PlaceOrderPaymentActivity.this, "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(PlaceOrderPaymentActivity.this, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });


    }
    
}