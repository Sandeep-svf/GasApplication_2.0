package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPriceListData {


    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("weight")
    @Expose
    public Integer weight;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("total_quantity")
    @Expose
    public Integer totalQuantity;
    @SerializedName("remaining_quantity")
    @Expose
    public Integer remainingQuantity;
    @SerializedName("added")
    @Expose
    public Integer added;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public Integer getAdded() {
        return added;
    }
}