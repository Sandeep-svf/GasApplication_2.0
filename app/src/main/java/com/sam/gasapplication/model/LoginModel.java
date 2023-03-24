package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

@SerializedName("success")
@Expose
public String success;
@SerializedName("message")
@Expose
public String message;
@SerializedName("user_type")
@Expose
public String userType;
@SerializedName("user_id")
@Expose
public Integer userId;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUserType() {
        return userType;
    }

    public Integer getUserId() {
        return userId;
    }
}