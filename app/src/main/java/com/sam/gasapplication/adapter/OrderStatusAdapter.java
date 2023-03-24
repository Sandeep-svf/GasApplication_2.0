package com.sam.gasapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.sam.gasapplication.MainActivity;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.delivery.activity.OrderDetailsActivity;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.model.data.OrderStatusModleData;
import com.sam.gasapplication.view.LoginActivity;
import com.shuhart.stepview.StepView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusViewHolder> {

    private Context context;
    private String[] descriptionData = {"Processing", "On the way", "Delivered"};
    private List<OrderStatusModleData> orderStatusModleDataList = new ArrayList<>();
    private String user_id;


    public OrderStatusAdapter(Context context, List<OrderStatusModleData> orderStatusModleDataList, String user_id) {
        this.context = context;
        this.orderStatusModleDataList = orderStatusModleDataList;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_status, parent, false);
        OrderStatusViewHolder myViewHolder = new OrderStatusViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String orderStatus="";

        holder.stateProgressBar.setStateDescriptionData(descriptionData);
        orderStatus = String.valueOf(orderStatusModleDataList.get(position).getOrderStatus());

        Log.e("orderStatus","orderStatus :"+orderStatus);
        Log.e("orderStatus","user_id :"+user_id);
        try {
            if(orderStatusModleDataList.get(position).getOrderStatus().equals("0")){
                holder.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                holder.order_status.setText("Processing your order.");
            }else if(orderStatusModleDataList.get(position).getOrderStatus().equals("1")){
                holder.stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                holder.order_status.setText("Your order is on the way.");
            }else {
                Toast.makeText(context, "Fetching wrong value form APIs...", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            holder.order_status.setText("null");
            Toast.makeText(context, "Someting went wrong while setting up order status value", Toast.LENGTH_SHORT).show();
        }
        holder.name.setText(orderStatusModleDataList.get(position).getName());
       // holder.phone_number.setText(orderStatusModleDataList.get(position).getPhone());
       // holder.address_status.setText(orderStatusModleDataList.get(position).getAddress());
      //  holder.weight_status.setText(orderStatusModleDataList.get(position).getWeight());
        holder.pay_amount_status.setText(String.valueOf(orderStatusModleDataList.get(position).getTotalAmount()));
        holder.order_id_status.setText(orderStatusModleDataList.get(position).getOrderNumber());


        holder.product_detials_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderNumber = orderStatusModleDataList.get(position).getOrderNumber();

                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("orderNumnber",orderNumber);
                intent.putExtra("userType","0");
                context.startActivity(intent);
            }
        });

        holder.cancel_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderId = orderStatusModleDataList.get(position).getOrderNumber();


                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.cancel_item);
                LinearLayout noDialogLogout = dialog.findViewById(R.id.noDialogLogout);
                LinearLayout yesDialogLogout = dialog.findViewById(R.id.yesDialogLogout);
                AppCompatEditText reason_layout = dialog.findViewById(R.id.reason_layout);


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                yesDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel_order_api(orderId, position,reason_layout.getText().toString());
                        dialog.dismiss();
                    }
                });


                noDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });


    }

    private void cancel_order_api(String orderId, int position, String reason) {



            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();


            Call<CommonSuccessMessageModel> call = API_Client.getClient().CANCEL_ORDER_COMMON_SUCCESS_MESSAGE_MODEL_CALL(user_id,orderId,reason);

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

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                removeItem(position);

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

    private void removeItem(int position) {

            try {
                orderStatusModleDataList.remove(position);
                notifyDataSetChanged();     // Update data in adapter.... Notify adapter for change data
            } catch (IndexOutOfBoundsException index) {
                index.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    @Override
    public int getItemCount() {
        return orderStatusModleDataList.size();
    }
}
class OrderStatusViewHolder extends RecyclerView.ViewHolder{

    StateProgressBar stateProgressBar;
    AppCompatTextView order_id_status,name,phone_number,address_status,weight_status,pay_amount_status,order_status;

    AppCompatButton cancel_order_layout,product_detials_button;
    public OrderStatusViewHolder(@NonNull View itemView) {
        super(itemView);

        stateProgressBar = itemView.findViewById(R.id.your_state_progress_bar_id);
        cancel_order_layout = itemView.findViewById(R.id.cancel_order_layout);
        name = itemView.findViewById(R.id.name);
        order_id_status = itemView.findViewById(R.id.order_id_status);
        phone_number = itemView.findViewById(R.id.phone_number);
        address_status = itemView.findViewById(R.id.address_status);
        weight_status = itemView.findViewById(R.id.weight_status);
        pay_amount_status = itemView.findViewById(R.id.pay_amount_status);
        order_status = itemView.findViewById(R.id.order_status);
        product_detials_button = itemView.findViewById(R.id.product_detials_button);

    }
}