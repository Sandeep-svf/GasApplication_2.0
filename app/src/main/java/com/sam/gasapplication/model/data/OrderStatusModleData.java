package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderStatusModleData {



    @SerializedName("order_number")
    @Expose
    public String orderNumber;
    @SerializedName("ordered_date")
    @Expose
    public String orderedDate;
    @SerializedName("order_status")
    @Expose
    public String orderStatus;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("total_amount")
    @Expose
    public Integer totalAmount;

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }
}