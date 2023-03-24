package com.sam.gasapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sam.gasapplication.R;
import com.sam.gasapplication.model.data.CheckPriceListData;
import com.sam.gasapplication.model.data.ItemListModelData;

import java.util.ArrayList;
import java.util.List;

public class PriceListAdapter extends RecyclerView.Adapter<PriceListViewHOlder> {

    private Context context;
    private List<ItemListModelData> checkPriceListDataList = new ArrayList<>();


    public PriceListAdapter(Context context, List<ItemListModelData> checkPriceListDataList) {
        this.context = context;
        this.checkPriceListDataList = checkPriceListDataList;
    }

    @NonNull
    @Override
    public PriceListViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.price_list_layout, parent, false);
        return  new PriceListViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceListViewHOlder holder, int position) {

        try {
            holder.weight_text.setText(String.valueOf(checkPriceListDataList.get(position).getName())+"");
            holder.price_text.setText(String.valueOf(checkPriceListDataList.get(position).getPrice())+"");
        } catch (NumberFormatException n){
            n.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return checkPriceListDataList.size();
    }
}

class PriceListViewHOlder extends RecyclerView.ViewHolder{
    AppCompatTextView weight_text,price_text;

    public PriceListViewHOlder(@NonNull View itemView) {
        super(itemView);
        weight_text = itemView.findViewById(R.id.weight_text);
        price_text = itemView.findViewById(R.id.price_text);
    }
}