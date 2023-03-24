package com.sam.gasapplication.delivery.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsModelDataList {

@SerializedName("product_id")
@Expose
public Integer productId;
@SerializedName("weight")
@Expose
public String weight;
@SerializedName("quantity")
@Expose
public Integer quantity;
@SerializedName("total_amount")
@Expose
public String totalAmount;

    public Integer getProductId() {
        return productId;
    }

    public String getWeight() {
        return weight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}