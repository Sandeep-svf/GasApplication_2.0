package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderModel {


    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("order_number")
    @Expose
    public String orderNumber;
    @SerializedName("total_amount")
    @Expose
    public Integer totalAmount;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("user_email")
    @Expose
    public String userEmail;
    @SerializedName("user_address")
    @Expose
    public String userAddress;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("cartCount")
    @Expose
    public Integer cartCount;


    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getCartCount() {
        return cartCount;
    }
}
