package com.sam.gasapplication.view.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.view.activity.MyProfileActivity;
import com.sam.gasapplication.view.activity.SupportActivity;


public class DashboardFragment extends Fragment {

    CardView Order_history_layout_CV,order_status_layout_CV,check_price_layout_CV,place_order_layout_CV
            ,support_layout_CV,manage_profile_layout_CV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Order_history_layout_CV = view.findViewById(R.id.Order_history_layout_CV);
        order_status_layout_CV = view.findViewById(R.id.order_status_layout_CV);
        check_price_layout_CV = view.findViewById(R.id.check_price_layout_CV);
        place_order_layout_CV = view.findViewById(R.id.place_order_layout_CV);
        support_layout_CV = view.findViewById(R.id.support_layout_CV);
        manage_profile_layout_CV = view.findViewById(R.id.manage_profile_layout_CV);


        support_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SupportActivity.class);
                startActivity(intent);
            }
        });

        manage_profile_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        place_order_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ItemListPlaceOrderFragment orderHistoryFragment = new ItemListPlaceOrderFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                MainActivity.load_home_fragment(orderHistoryFragment, fragmentManager);

                ItemListPlaceOrderFragment fragment2 = new ItemListPlaceOrderFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.commit();

                ((MainActivity)getActivity()).set_head("2");

            }
        });

        check_price_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* CheckPriceListFragment orderHistoryFragment = new CheckPriceListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(orderHistoryFragment, fragmentManager);*/

                CheckPriceListFragment fragment2 = new CheckPriceListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.commit();

                ((MainActivity)getActivity()).set_head("1");

            }
        });

        Order_history_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(orderHistoryFragment, fragmentManager);*/

                OrderHistoryFragment fragment2 = new OrderHistoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.commit();

                ((MainActivity)getActivity()).set_head("4");

            }
        });

        order_status_layout_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*OrderStatusFragment orderStatusFragment = new OrderStatusFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(orderStatusFragment, fragmentManager);*/

                OrderStatusFragment fragment2 = new OrderStatusFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.commit();
                ((MainActivity)getActivity()).set_head("3");

            }
        });


        return view;
    }
}