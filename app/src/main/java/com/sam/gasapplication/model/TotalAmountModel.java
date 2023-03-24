package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalAmountModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("totalAmount")
@Expose
public Double totalAmount;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}