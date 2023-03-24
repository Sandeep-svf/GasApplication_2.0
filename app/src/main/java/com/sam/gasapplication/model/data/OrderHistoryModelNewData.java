package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryModelNewData {

@SerializedName("order_number")
@Expose
public String orderNumber;
@SerializedName("name")
@Expose
public String name;
@SerializedName("weight")
@Expose
public String weight;
@SerializedName("quantity")
@Expose
public Integer quantity;
@SerializedName("phone")
@Expose
public String phone;
@SerializedName("price")
@Expose
public String price;
@SerializedName("email")
@Expose
public String email;
@SerializedName("delivery_charge")
@Expose
public String deliveryCharge;
@SerializedName("total_amount")
@Expose
public String totalAmount;
@SerializedName("date")
@Expose
public Object date;
@SerializedName("address")
@Expose
public String address;
@SerializedName("order_status")
@Expose
public String orderStatus;
@SerializedName("cancel_reason")
@Expose
public Object cancelReason;

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }

    public String getEmail() {
        return email;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public Object getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Object getCancelReason() {
        return cancelReason;
    }
}