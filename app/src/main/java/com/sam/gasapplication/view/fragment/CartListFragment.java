package com.sam.gasapplication.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.adapter.CartListAdapter;
import com.sam.gasapplication.adapter.OrderHistoryAdapter;
import com.sam.gasapplication.model.CartListModel;
import com.sam.gasapplication.model.TotalAmountModel;
import com.sam.gasapplication.model.TotalAmountModel;
import com.sam.gasapplication.model.TotalAmountModel;
import com.sam.gasapplication.model.data.CartListModelData;
import com.sam.gasapplication.view.activity.PlaceOrderAddressActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartListFragment extends Fragment implements CartListAdapter.Get_Position_Eye_Function{

    RecyclerView rcv_cart_list;
    AppCompatTextView proceed_to_buy_layout,total_ammout_to_pay_text;
    private String user_id;
    private List<CartListModelData> cartListModelDataList = new ArrayList<>();
    private  String itemSize="0";
    private LinearLayoutCompat hide_show_proceed_layout;
    CartListAdapter cartListAdapter ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart_list, container, false);
        intis(view);


        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");


        ((MainActivity)getActivity()).cart_item_number_api();

        proceed_to_buy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cartListModelDataList.isEmpty()){
                    Toast.makeText(getActivity(), "Please add some item in cart before buy it.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getActivity(), PlaceOrderAddressActivity.class);
                    startActivity(intent);
                }

            }
        });

        cart_list_api();
        total_amount_api();



        return view;
    }

    private void total_amount_api() {
        Log.e("test_cart","total_amount_api : CALLING>>>");

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<TotalAmountModel> call = API_Client.getClient().TOTAL_AMOUNT_MODEL_CALL(user_id);

            call.enqueue(new Callback<TotalAmountModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<TotalAmountModel> call, Response<TotalAmountModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();
                            String totalAmount = String.valueOf(response.body().getTotalAmount());

                            if (success.equals("true") || success.equals("True")) {

                                TotalAmountModel totalAmountModel = response.body();

                                total_ammout_to_pay_text.setText(String.valueOf(totalAmountModel.getTotalAmount()));
                                ((MainActivity)getActivity()).cart_item_number_api();
                                CartListFragment.this.notifyAll();
                                pd.dismiss();

                            } else {

                                pd.dismiss();
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();

                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getActivity(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });


        }

    private void cart_list_api() {
 
            final ProgressDialog pd = new ProgressDialog(getActivity());
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

                                total_ammout_to_pay_text.setText(totalAmount);

                                cartListModelDataList = response.body().getCartList();
                                itemSize = String.valueOf(cartListModelDataList.size());

                                if(itemSize.equals("0")){
                                    hide_show_proceed_layout.setVisibility(View.GONE);
                                }else{
                                    hide_show_proceed_layout.setVisibility(View.VISIBLE);
                                    proceed_to_buy_layout.setText("Proceed to buy items");
                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                                rcv_cart_list.setLayoutManager(linearLayoutManager);
                                cartListAdapter = new CartListAdapter(getActivity(),cartListModelDataList,user_id);
                                rcv_cart_list.setAdapter(cartListAdapter);
                                cartListAdapter.setGet_position_itemDrawings(CartListFragment.this);


                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();

                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getActivity(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        }

    private void intis(View view) {
        total_ammout_to_pay_text = view.findViewById(R.id.total_ammout_to_pay_text);
        rcv_cart_list = view.findViewById(R.id.rcv_cart_list);
        proceed_to_buy_layout = view.findViewById(R.id.proceed_to_buy_layout);
        hide_show_proceed_layout = view.findViewById(R.id.hide_show_proceed_layout);

    }

    @Override
    public void page_details(String value) {
        Log.e("test_cart","TEST_F_CALL_FROM_ADAPTER : SUCCESS"+value);
        total_amount_api();


    }
}