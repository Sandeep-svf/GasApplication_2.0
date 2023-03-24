package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyProfileModelData {

@SerializedName("image")
@Expose
public String image;
@SerializedName("name")
@Expose
public String name;
@SerializedName("email")
@Expose
public String email;
@SerializedName("phone")
@Expose
public String phone;
@SerializedName("user_type")
@Expose
public String userType;
@SerializedName("address")
@Expose
public String address;
@SerializedName("landmark")
@Expose
public String landmark;

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserType() {
        return userType;
    }

    public String getAddress() {
        return address;
    }

    public String getLandmark() {
        return landmark;
    }
}