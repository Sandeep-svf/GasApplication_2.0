package com.sam.gasapplication.delivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.gasapplication.R;

import com.sam.gasapplication.delivery.activity.OrderDetailsActivity;
import com.sam.gasapplication.delivery.model.data.OrderListModelDataDP;
import com.sam.gasapplication.utility.BookingActivity;

import java.util.ArrayList;
import java.util.List;


public class OrderListAdapterDP extends RecyclerView.Adapter<OrderListViewHolder> {


    private String user_id;
    private List<OrderListModelDataDP> modelDataDPList = new ArrayList<>();
    private Context context;


    public OrderListAdapterDP(String user_id, List<OrderListModelDataDP> modelDataDPList, Context context) {
        this.user_id = user_id;
        this.modelDataDPList = modelDataDPList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_dp, parent, false);
        OrderListViewHolder myViewHolder = new OrderListViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            holder.order_number.setText(modelDataDPList.get(position).getOrderNumber());
            holder.name.setText(modelDataDPList.get(position).getName());
            holder.phone_number.setText(modelDataDPList.get(position).getPhone());
            holder.order_status.setText(modelDataDPList.get(position).getOrderStatus());
            holder.total_amount.setText(String.valueOf(modelDataDPList.get(position).getTotalAmount()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        holder.draw_map_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookingActivity.class);
                context.startActivity(intent);
            }
        });

        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String orderNumnber = modelDataDPList.get(position).getOrderNumber();

                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderNumnber",orderNumnber);
                intent.putExtra("userType","1");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelDataDPList.size();
    }
}
class OrderListViewHolder extends RecyclerView.ViewHolder{

    AppCompatTextView order_number,name,phone_number,order_status,total_amount;
    AppCompatButton view_details;
    AppCompatImageView draw_map_route;

    public OrderListViewHolder(@NonNull View itemView) {
        super(itemView);
        order_number = itemView.findViewById(R.id.order_number);
        name = itemView.findViewById(R.id.name);
        phone_number = itemView.findViewById(R.id.phone_number);
        order_status = itemView.findViewById(R.id.order_status);
        total_amount = itemView.findViewById(R.id.total_amount);
        view_details = itemView.findViewById(R.id.view_details);
        draw_map_route = itemView.findViewById(R.id.draw_map_route);
    }
}