package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.model.data.ItemListModelData;

import java.util.List;

public class ItemListModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("cartCount")
@Expose
public Integer cartCount;
@SerializedName("Data")
@Expose
public List<ItemListModelData> data = null;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public List<ItemListModelData> getData() {
        return data;
    }
}