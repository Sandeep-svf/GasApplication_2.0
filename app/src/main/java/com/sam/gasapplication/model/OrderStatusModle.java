package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.model.data.OrderStatusModleData;

import java.util.List;

public class OrderStatusModle {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("Data")
@Expose
public List<OrderStatusModleData> data = null;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderStatusModleData> getData() {
        return data;
    }
}