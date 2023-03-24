package com.sam.gasapplication.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
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
import com.sam.gasapplication.adapter.PriceListAdapter;
import com.sam.gasapplication.model.ItemListModel;
import com.sam.gasapplication.model.data.CheckPriceListData;
import com.sam.gasapplication.model.data.ItemListModelData;
import com.sam.gasapplication.view.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckPriceListFragment extends Fragment {

    RecyclerView rcv_price_list;
    List<ItemListModelData> checkPriceListDataList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        rcv_price_list = view.findViewById(R.id.rcv_price_list);


        price_list_api();

        
        return view;
    }

    private void price_list_api() {
      

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<ItemListModel> call = API_Client.getClient().ITEM_LIST_MODEL_CALL("7");

            call.enqueue(new Callback<ItemListModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<ItemListModel> call, Response<ItemListModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();




                            if (success.equals("true") || success.equals("True")) {

                                checkPriceListDataList = response.body().getData();

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                                rcv_price_list.setLayoutManager(linearLayoutManager);
                                PriceListAdapter priceListAdapter = new PriceListAdapter(getActivity(),checkPriceListDataList);
                                rcv_price_list.setAdapter(priceListAdapter);

                                
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
                public void onFailure(Call<ItemListModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
                    if (t instanceof IOException) {
                        Toast.makeText(getActivity(), "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion_issue", t.getMessage());
                        Toast.makeText(getActivity(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });


        }
}