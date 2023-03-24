package com.sam.gasapplication.delivery.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderListModelDataDP {

@SerializedName("order_number")
@Expose
public String orderNumber;
@SerializedName("name")
@Expose
public String name;
@SerializedName("phone")
@Expose
public String phone;
@SerializedName("total_amount")
@Expose
public Integer totalAmount;
@SerializedName("longitude")
@Expose
public Object longitude;
@SerializedName("latitude")
@Expose
public Object latitude;
@SerializedName("order_status")
@Expose
public String orderStatus;

    public String getOrderNumber() {
        return orderNumber;
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

    public Object getLongitude() {
        return longitude;
    }

    public Object getLatitude() {
        return latitude;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}