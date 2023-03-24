package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryData {

@SerializedName("order_number")
@Expose
public String orderNumber;
@SerializedName("total_amount")
@Expose
public String totalAmount;
@SerializedName("date")
@Expose
public String date;
@SerializedName("order_status")
@Expose
public String orderStatus;


    public String getOrderNumber() {
        return orderNumber;
    }
    public String getTotalAmount() {
        return totalAmount;
    }
    public String getDate() {
        return date;
    }
    public String getOrderStatus() {
        return orderStatus;
    }


}