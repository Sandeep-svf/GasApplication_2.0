package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPriceModelData {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("weight")
@Expose
public Double weight;
@SerializedName("price")
@Expose
public Integer price;


    public Integer getId() {
        return id;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getPrice() {
        return price;
    }
}