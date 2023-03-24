package com.sam.gasapplication.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.delivery.model.data.OrderListModelDataDP;

import java.util.List;

public class OrderListModelDP {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("Data")
@Expose
public List<OrderListModelDataDP> data = null;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderListModelDataDP> getData() {
        return data;
    }
}