package com.sam.gasapplication.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.adapter.NotificationAdapter;
import com.sam.gasapplication.adapter.OrderHistoryAdapter;
import com.sam.gasapplication.adapter.OrderStatusAdapter;
import com.sam.gasapplication.model.OrderStatusModle;
import com.sam.gasapplication.model.OrderStatusModle;
import com.sam.gasapplication.model.data.OrderStatusModleData;
import com.sam.gasapplication.view.NotificationActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusFragment extends Fragment {

    RecyclerView rcv_order_staus;
    private String user_id;
    private List<OrderStatusModleData> orderStatusModleDataList = new ArrayList<>();

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);

        rcv_order_staus = view.findViewById(R.id.rcv_order_staus);

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        user_id=sharedPreferences.getString("userId","");

        order_status_api();

        return  view;
    }

    private void order_status_api() {
      

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<OrderStatusModle> call = API_Client.getClient().ORDER_STATUS_MODLE_CALL(user_id);

            call.enqueue(new Callback<OrderStatusModle>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<OrderStatusModle> call, Response<OrderStatusModle> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {

                                orderStatusModleDataList = response.body().getData();

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                                rcv_order_staus.setLayoutManager(linearLayoutManager);
                                OrderStatusAdapter orderHistoryAdapter = new OrderStatusAdapter(getActivity(),orderStatusModleDataList,user_id);
                                rcv_order_staus.setAdapter(orderHistoryAdapter);



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
                public void onFailure(Call<OrderStatusModle> call, Throwable t) {

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
}