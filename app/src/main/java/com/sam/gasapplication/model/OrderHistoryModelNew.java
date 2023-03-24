package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.model.data.OrderHistoryModelNewData;

import java.util.List;

public class OrderHistoryModelNew {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("Data")
@Expose
public List<OrderHistoryModelNewData> data = null;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderHistoryModelNewData> getData() {
        return data;
    }
}