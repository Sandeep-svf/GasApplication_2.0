package com.sam.gasapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUserAccountModel {
    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("user_id")
    @Expose
    public String user_id;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUser_id() {
        return user_id;
    }
}
