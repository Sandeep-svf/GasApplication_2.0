package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddRemoveitemtoCartModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("cartCount")
@Expose
public Integer cartCount;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCartCount() {
        return cartCount;
    }
}