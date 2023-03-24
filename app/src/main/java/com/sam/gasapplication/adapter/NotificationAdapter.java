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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sam.gasapplication.R;
import com.sam.gasapplication.RetrofitApi.API_Client;
import com.sam.gasapplication.model.CommonSuccessMessageModel;
import com.sam.gasapplication.model.data.NotificationModelData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private Context context;
    List<NotificationModelData> notificationModelDataList = new ArrayList<>();
    private String user_id;


    public NotificationAdapter(Context context, List<NotificationModelData> notificationModelDataList,String user_id) {
        this.context = context;
        this.notificationModelDataList = notificationModelDataList;
        this.user_id = user_id;
    }



    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_design, parent, false);
        NotificationViewHolder myViewHolder = new NotificationViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            holder.notification_heading.setText(notificationModelDataList.get(position).getSubject());
            holder.notificatin_subheading.setText(notificationModelDataList.get(position).getMessage());
            holder.notification_date.setText(notificationModelDataList.get(position).getDate());
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(context, "Something went wrong... while loading data form an API", Toast.LENGTH_SHORT).show();
        }

        holder.notification_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String notificationId = notificationModelDataList.get(position).getNotificationId();
                notification_delete_api(notificationId,position);

            }
        });


    }

    private void notification_delete_api(String notificationId, int position) {


            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();


            Call<CommonSuccessMessageModel> call = API_Client.getClient().DELETE_NOTIFICATION_ITEM_COMMON_SUCCESS_MESSAGE_MODEL_CALL(user_id,notificationId);

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

    public void removeItem(int position) {
        try {
            notificationModelDataList.remove(position);
            notifyDataSetChanged();     // Update data in adapter.... Notify adapter for change data
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return notificationModelDataList.size();
    }
}

class NotificationViewHolder extends RecyclerView.ViewHolder {

    AppCompatTextView notification_heading , notificatin_subheading,notification_date;
    AppCompatImageView notification_delete_image;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        notification_heading = itemView.findViewById(R.id.event_heading);
        notificatin_subheading = itemView.findViewById(R.id.notificatin_subheading);
        notification_date = itemView.findViewById(R.id.notification_date);
        notification_delete_image = itemView.findViewById(R.id.notification_delete_image);
    }
}