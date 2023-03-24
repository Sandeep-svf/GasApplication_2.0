package com.sam.gasapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sam.gasapplication.R;
import com.sam.gasapplication.view.activity.PlaceOrderAddressActivity;


public class PlaceOrderFragment extends Fragment {

    AppCompatImageView add,remove;
    AppCompatTextView price_text,quantity_text,total_text;
    AppCompatButton place_order_button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_price, container, false);
        inits(view);


        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlaceOrderAddressActivity.class);
                startActivity(intent);

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(validation()){
                        int quantity = Integer.parseInt(quantity_text.getText().toString());
                        quantity = --quantity;
                        quantity_text.setText(String.valueOf(quantity));

                        int price = Integer.parseInt(price_text.getText().toString());
                        double total = price * quantity;
                        total_text.setText(String.valueOf(total));

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int quantity = Integer.parseInt(quantity_text.getText().toString());
                    quantity = ++quantity;
                    quantity_text.setText(String.valueOf(quantity));

                    int price = Integer.parseInt(price_text.getText().toString());
                    double total = price * quantity;
                    total_text.setText(String.valueOf(total));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });



    return view;
    }

    private boolean validation() {
        if(quantity_text.getText().toString().equals("0")){
            Toast.makeText(getActivity(), "Please select item first.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void inits(View view) {
        place_order_button = view.findViewById(R.id.place_order_button);
        add = view.findViewById(R.id.add);
        remove = view.findViewById(R.id.remove);
        price_text = view.findViewById(R.id.price_text);
        quantity_text = view.findViewById(R.id.quantity_text);
        total_text = view.findViewById(R.id.total_text);
    }
}