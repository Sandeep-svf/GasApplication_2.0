package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeQuantityModelData {

@SerializedName("quantity")
@Expose
public String quantity;
@SerializedName("total_price")
@Expose
public Integer totalPrice;

    public String getQuantity() {
        return quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}