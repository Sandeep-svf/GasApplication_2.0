package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.model.data.CheckPriceListData;

import java.util.List;

public class CheckPriceListModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("Data")
@Expose
public List<CheckPriceListData> data = null;
@SerializedName("cartCount")
@Expose
public Integer cartCount;

    public Integer getCartCount() {
        return cartCount;
    }



    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<CheckPriceListData> getData() {
        return data;
    }
}