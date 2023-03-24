package com.sam.gasapplication.delivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.gasapplication.R;

import com.sam.gasapplication.delivery.model.data.OrderDetailsModelDataList;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderItemViewHolder> {
    private Context context;
    private List<OrderDetailsModelDataList> orderDetailsModelDataListList = new ArrayList<>();

    public OrderDetailsAdapter(Context context, List<OrderDetailsModelDataList> orderDetailsModelDataListList) {
        this.context = context;
        this.orderDetailsModelDataListList = orderDetailsModelDataListList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_list_design, parent, false);
        OrderItemViewHolder myViewHolder = new OrderItemViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {

        holder.weight.setText(orderDetailsModelDataListList.get(position).getWeight());
        holder.ammount.setText(orderDetailsModelDataListList.get(position).getTotalAmount());
        holder.quantity.setText(String.valueOf(orderDetailsModelDataListList.get(position).getQuantity()));

    }

    @Override
    public int getItemCount() {
        return orderDetailsModelDataListList.size();
    }
}
class OrderItemViewHolder extends RecyclerView.ViewHolder{

    AppCompatTextView weight,quantity,ammount;

    public OrderItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ammount = itemView.findViewById(R.id.ammount);
        quantity = itemView.findViewById(R.id.quantity);
        weight = itemView.findViewById(R.id.weight);
    }
}