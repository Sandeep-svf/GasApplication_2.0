package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.model.data.CartListModelData;

import java.util.List;

public class CartListModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("cartList")
@Expose
public List<CartListModelData> cartListModelDataList = null;
@SerializedName("totalAmount")
@Expose
public Double totalAmount;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<CartListModelData> getCartList() {
        return cartListModelDataList;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}