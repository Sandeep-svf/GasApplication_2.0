package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModelData {

    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("notification_id")
    @Expose
    public String notificationId;
    @SerializedName("order_number")
    @Expose
    public String orderNumber;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("subject")
    @Expose
    public String subject;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("date")
    @Expose
    public String date;

    public String getUserId() {
        return userId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getUserType() {
        return userType;
    }

    public String getDate() {
        return date;
    }
}