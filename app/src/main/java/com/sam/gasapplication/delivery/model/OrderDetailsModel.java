package com.sam.gasapplication.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("Data")
@Expose
public OrderDetailsModelData orderDetailsModelData;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public OrderDetailsModelData getData() {
        return orderDetailsModelData;
    }
}