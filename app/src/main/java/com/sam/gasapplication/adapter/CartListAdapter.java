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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.ChangeQuantityModel;
import com.sam.gasapplication.model.ChangeQuantityModel;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.model.data.CartListModelData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListAdapter extends RecyclerView.Adapter<CartListViewHolder> {

    private Context context;
    private List<CartListModelData> cartListModelDataList = new ArrayList<>();
    private String userId;
    private Boolean apiFlag = false;
    private String success = "false";




    public CartListAdapter(Context context, List<CartListModelData> cartListModelDataList, String userId) {
        this.context = context;
        this.cartListModelDataList = cartListModelDataList;
        this.userId = userId;
    }



    public interface  Get_Position_Eye_Function
    {
        void page_details(String value);
       // void page_details2(int position, String id);

    }
    private  Get_Position_Eye_Function get_position_eye_function;

    public void setGet_position_itemDrawings(CartListAdapter.Get_Position_Eye_Function get_position_eye_function) {
        this.get_position_eye_function = get_position_eye_function;
    }


    @NonNull
    @Override
    public CartListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_list_layout, parent, false);
        return  new CartListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(cartListModelDataList.get(position).getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.item_image_cart);

        Log.e("image","IMAGE :"+cartListModelDataList.get(position).getImage());

        holder.priduct_id_cart.setText(String.valueOf(cartListModelDataList.get(position).getId()));
        holder.product_price_cart.setText(String.valueOf(cartListModelDataList.get(position).getTotalAmount()));
        holder.product_weight_cart.setText(String.valueOf(cartListModelDataList.get(position).getWeight()));
        holder.quantity_text.setText(String.valueOf(cartListModelDataList.get(position).getQuantity()));
        holder.product_price_cart_ind.setText(String.valueOf(cartListModelDataList.get(position).getPrice()));



        holder.delete_cart_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = String.valueOf(cartListModelDataList.get(position).getId());
                delete_cart_item_api(productId,position);
                get_position_eye_function.page_details(String.valueOf(position));

            }
        });


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(validation(holder,context)){
                        int quantity = Integer.parseInt(holder.quantity_text.getText().toString());
                        quantity = --quantity;
                        // holder.quantity_text.setText(String.valueOf(quantity));

                        String productId = String.valueOf(cartListModelDataList.get(position).getId());
                        String productPrice = String.valueOf(cartListModelDataList.get(position).getPrice());

                        change_quantity_api(position,productId, String.valueOf(quantity),userId,holder,productPrice);
                        get_position_eye_function.page_details(String.valueOf(position));


                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something went wrong... Please try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int quantity = Integer.parseInt(holder.quantity_text.getText().toString());
                    quantity = ++quantity;
                    //holder.quantity_text.setText(String.valueOf(quantity));

                    String productId = String.valueOf(cartListModelDataList.get(position).getId());
                    String productPrice = String.valueOf(cartListModelDataList.get(position).getPrice());

                    change_quantity_api(position,productId, String.valueOf(quantity),userId,holder,productPrice);
                    get_position_eye_function.page_details(String.valueOf(position));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void delete_cart_item_api(String productId, int position) {
       

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();


            Call<CommonSuccessMessageModel> call = API_Client.getClient().DELETE_CART_ITEM_COMMON_SUCCESS_MESSAGE_MODEL_CALL(userId,productId);

            call.enqueue(new Callback<CommonSuccessMessageModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<CommonSuccessMessageModel> call, Response<CommonSuccessMessageModel> response) {
                    pd.dismiss();


                    try {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {

                               // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                removeItem(position);
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
                public void onFailure(Call<CommonSuccessMessageModel> call, Throwable t) {
                    Log.e("bhgyrrrthbh", String.valueOf(t));
                    if (t instanceof IOException) {
                        Toast.makeText(context, "This is an actual network failure :( inform the user and possibly retry)" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(context, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });
        }

    private void change_quantity_api(Integer position,String productId, String productQuantity, String userId, CartListViewHolder holder, String productPrice) {
        
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<ChangeQuantityModel> call = API_Client.getClient().CHANGE_QUANTITY_MODEL_CALL(userId,productId,productQuantity);

            call.enqueue(new Callback<ChangeQuantityModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<ChangeQuantityModel> call, Response<ChangeQuantityModel> response) {
                    pd.dismiss();




                    try {
                        if (response.isSuccessful()) {

                            String message = response.body().getMessage();
                            String success = response.body().getSuccess();

                            if (success.equals("true") || success.equals("True")) {
                               // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                apiFlag = true;
                                Double totalAmount = 0.0;
                                try {
                                    holder.quantity_text.setText(productQuantity);
                                    Double price = Double.valueOf(productPrice);
                                    Double quantity = Double.valueOf(productQuantity);
                                    totalAmount = price * quantity;

                                    cartListModelDataList.get(position).setQuantity(Integer.valueOf(productQuantity));


                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "Something went wrong.. while calculating price.", Toast.LENGTH_SHORT).show();
                                    Log.e("exception", String.valueOf(e));
                                }

                               /* for(int i=0; i < cartListModelDataList.size(); i++){
                                    totalAmount = totalAmount + cartListModelDataList.get(i).getTotalAmount();

                                }*/

                                holder.product_price_cart.setText(String.valueOf(totalAmount));

                                pd.dismiss();




                            } else {
                                apiFlag = false;
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
                public void onFailure(Call<ChangeQuantityModel> call, Throwable t) {

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
    public void removeItem(int position) {
        try {
            cartListModelDataList.remove(position);
            notifyDataSetChanged();     // Update data in adapter.... Notify adapter for change data
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validation(CartListViewHolder holder, Context  context) {
        if(holder.quantity_text.getText().toString().equals("0")){
            Toast.makeText(context, "Please select item first.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return cartListModelDataList.size();
    }
}

class CartListViewHolder extends RecyclerView.ViewHolder{
    AppCompatImageView add,remove;
    AppCompatTextView quantity_text,priduct_id_cart,product_weight_cart,product_price_cart,product_price_cart_ind;
    ShapeableImageView item_image_cart;
    AppCompatButton delete_cart_item_button;

    public CartListViewHolder(@NonNull View itemView) {
        super(itemView);
        add = itemView.findViewById(R.id.add);
        delete_cart_item_button = itemView.findViewById(R.id.delete_cart_item_button);
        remove = itemView.findViewById(R.id.remove);
        quantity_text = itemView.findViewById(R.id.quantity_text);
        priduct_id_cart = itemView.findViewById(R.id.priduct_id_cart);
        product_weight_cart = itemView.findViewById(R.id.product_weight_cart);
        product_price_cart = itemView.findViewById(R.id.product_price_cart);
        item_image_cart = itemView.findViewById(R.id.item_image_cart);
        product_price_cart_ind = itemView.findViewById(R.id.product_price_cart_ind);
    }
}
