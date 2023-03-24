package com.sam.gasapplication.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.AddRemoveitemtoCartModel;
import com.sam.gasapplication.model.AddRemoveitemtoCartModel;
import com.sam.gasapplication.model.data.ItemListModelData;
import com.sam.gasapplication.utility.GridSpacingItemDecoration;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListViewHolder> {


    private Context context;
    private List<ItemListModelData> checkPriceListDataList = new ArrayList<>();
    private String user_id;


    public interface  Get_Position_Eye_Function
    {
        void page_details( String id);


    }
    private  Get_Position_Eye_Function get_position_eye_function;

    public void setGet_position_itemDrawings(ItemListAdapter.Get_Position_Eye_Function get_position_eye_function) {
        this.get_position_eye_function = get_position_eye_function;
    }

    public ItemListAdapter(Context context, List<ItemListModelData> checkPriceListDataList,String user_id) {
        this.context = context;
        this.checkPriceListDataList = checkPriceListDataList;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_layout, parent, false);
        return  new ItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String product_cart_status = String.valueOf(checkPriceListDataList.get(position).getAdded());


        if(product_cart_status.equals("1")){
            holder.add_product_to_cart_button.setText("Remove");
        }else if(product_cart_status.equals("0")){
            holder.add_product_to_cart_button.setText("Add");
        }else{
            Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }

        Glide.with(context).load(checkPriceListDataList.get(position).getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.product_image);
        holder.product_price.setText(String.valueOf(checkPriceListDataList.get(position).getPrice()));
        holder.product_weight.setText(String.valueOf(checkPriceListDataList.get(position).getWeight()));

        holder.add_product_to_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String product_id = String.valueOf(checkPriceListDataList.get(position).getId());
                String product_cart_current_status = String.valueOf(checkPriceListDataList.get(position).getAdded());

                Log.e("STATUS","SESSION 0: "+ product_cart_current_status);

                add_item_in_to_cart_api(product_id,holder,product_cart_current_status,position);
                get_position_eye_function.page_details(String.valueOf(position));



            }
        });


    }



    private void add_item_in_to_cart_api(String product_id,ItemListViewHolder holder,String product_cart_status_2,Integer position) {


            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<AddRemoveitemtoCartModel> call = API_Client.getClient().ADD_REMOVEITEMTO_CART_MODEL_CALL(user_id,product_id);

            call.enqueue(new Callback<AddRemoveitemtoCartModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<AddRemoveitemtoCartModel> call, Response<AddRemoveitemtoCartModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();

                            if (success.equals("true") || success.equals("True")) {


                                Log.e("STATUS","SESSION 1: ");

                                if(product_cart_status_2.equals("1")){

                                    checkPriceListDataList.get(position).setAdded(0);
                                    notifyItemChanged(Integer.parseInt(product_cart_status_2));
                                    notifyDataSetChanged();
                                    Log.e("STATUS","SESSION 2: "+product_cart_status_2+" : "+checkPriceListDataList.get(position).getAdded());
                                }else if(product_cart_status_2.equals("0")){
                                    checkPriceListDataList.get(position).setAdded(1);
                                    notifyItemChanged(Integer.parseInt(product_cart_status_2));
                                    notifyDataSetChanged();
                                    Log.e("STATUS","SESSION 3: "+product_cart_status_2+" : "+checkPriceListDataList.get(position).getAdded());


                                }else{
                                    Toast.makeText(context, "Something went wrong while setting data in added model....", Toast.LENGTH_SHORT).show();
                                }


                                String newStatus = String.valueOf(checkPriceListDataList.get(position).getAdded());

                                if(newStatus.equals("1")){
                                    holder.add_product_to_cart_button.setText("Remove");


                                    Log.e("STATUS","SESSION FINAL: Remove POS"+position);

                                }else if(newStatus.equals("0")){
                                    holder.add_product_to_cart_button.setText("Add");

                                    Log.e("STATUS","SESSION FINAL: Add POS"+position);

                                }else{
                                    Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
                                }
                               // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                pd.dismiss();



                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }


                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();

                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(context, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(context, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(context, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(context, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(context, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(context, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(context, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AddRemoveitemtoCartModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
                    if (t instanceof IOException) {
                        Toast.makeText(context, "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion_issue", t.getMessage());
                        Toast.makeText(context, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });


        }

    @Override
    public int getItemCount() {
        return checkPriceListDataList.size();
    }
}

class ItemListViewHolder extends RecyclerView.ViewHolder{

    ShapeableImageView product_image;
    AppCompatTextView product_price,product_weight;
    AppCompatButton add_product_to_cart_button;

    public ItemListViewHolder(@NonNull View itemView) {
        super(itemView);

        product_image = itemView.findViewById(R.id.product_image);
        product_weight = itemView.findViewById(R.id.product_weight);
        product_price = itemView.findViewById(R.id.product_price);
        add_product_to_cart_button = itemView.findViewById(R.id.add_product_to_cart_button);
    }
}
