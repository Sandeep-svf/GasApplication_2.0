package com.sam.gasapplication.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartListModelData {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("weight")
@Expose
public Double weight;
@SerializedName("price")
@Expose
public Double price;
@SerializedName("quantity")
@Expose
public Integer quantity;
@SerializedName("total")
@Expose
public Double total;
@SerializedName("total_amount")
@Expose
public Double totalAmount;
    @SerializedName("image")
    @Expose
    public String image;

    public String getImage() {
        return image;
    }

    public Integer getId() {
        return id;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getTotal() {
        return total;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}