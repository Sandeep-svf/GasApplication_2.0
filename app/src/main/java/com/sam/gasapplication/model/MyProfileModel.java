package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.model.data.MyProfileModelData;

public class MyProfileModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("Data")
@Expose
public MyProfileModelData myProfileModelData;


    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public MyProfileModelData getData() {
        return myProfileModelData;
    }
}