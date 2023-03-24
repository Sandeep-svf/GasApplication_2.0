package com.sam.gasapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sam.gasapplication.R;
import com.sam.gasapplication.model.GooglePlaceModel;

import java.util.ArrayList;

public class GooglePlaceAdapter extends RecyclerView.Adapter<GooglePlaceAdapter.ViewMyBookingHolder> {
    Context context;
    ArrayList<GooglePlaceModel> arrayList;

    public GooglePlaceAdapter(Context context, ArrayList<GooglePlaceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewMyBookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list, parent, false);
        return new ViewMyBookingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMyBookingHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getPlaceName());
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewMyBookingHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewMyBookingHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}