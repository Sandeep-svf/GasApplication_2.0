package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.model.data.ChangeQuantityModelData;

public class ChangeQuantityModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("Data")
@Expose
public ChangeQuantityModelData changeQuantityModelData;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ChangeQuantityModelData getData() {
        return changeQuantityModelData;
    }
}