package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonSuccessMessageModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;


    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}