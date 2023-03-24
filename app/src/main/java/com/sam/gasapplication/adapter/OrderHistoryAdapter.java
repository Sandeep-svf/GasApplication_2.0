package com.sam.gasapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.gasapplication.R;
import com.sam.gasapplication.delivery.activity.OrderDetailsActivity;
import com.sam.gasapplication.model.data.OrderHistoryData;

import java.util.ArrayList;
import java.util.List;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryViewHolder> {

    private Context context;
    List<OrderHistoryData> orderHistoryDataList = new ArrayList<>();
    private String userType;


    public OrderHistoryAdapter(Context context, List<OrderHistoryData> orderHistoryDataList, String userType) {
        this.context = context;
        this.orderHistoryDataList = orderHistoryDataList;
        this.userType = userType;

    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history, parent, false);
        OrderHistoryViewHolder myViewHolder = new OrderHistoryViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.order_id.setText(orderHistoryDataList.get(position).getOrderNumber());
        holder.order_date.setText(orderHistoryDataList.get(position).getDate());
        holder.status.setText(orderHistoryDataList.get(position).getOrderStatus());

        holder.product_detials_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String orderNumnber = orderHistoryDataList.get(position).getOrderNumber();

                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderNumnber",orderNumnber);
                intent.putExtra("userType","0");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderHistoryDataList.size();
    }
}
class OrderHistoryViewHolder extends RecyclerView.ViewHolder{

    AppCompatTextView order_id,name,mobile_number,email,address,weight,Price,status,order_date;
    AppCompatButton product_detials_button;

    public OrderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id = itemView.findViewById(R.id.order_id);
        name = itemView.findViewById(R.id.name);
      //  mobile_number = itemView.findViewById(R.id.mobile_number);
      //  email = itemView.findViewById(R.id.email);
       // address = itemView.findViewById(R.id.address);
      //  weight = itemView.findViewById(R.id.weight);
        order_date = itemView.findViewById(R.id.order_date);
        status = itemView.findViewById(R.id.status);
        product_detials_button = itemView.findViewById(R.id.product_detials_button);
    }
}