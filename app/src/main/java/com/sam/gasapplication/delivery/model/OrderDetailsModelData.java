package com.sam.gasapplication.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sam.gasapplication.delivery.model.data.OrderDetailsModelDataList;

import java.util.List;

public class OrderDetailsModelData {

@SerializedName("order_number")
@Expose
public String orderNumber;
@SerializedName("ordered_date")
@Expose
public String orderedDate;
@SerializedName("order_status")
@Expose
public String orderStatus;
@SerializedName("total_amount")
@Expose
public Integer totalAmount;
@SerializedName("name")
@Expose
public String name;
@SerializedName("phone")
@Expose
public String phone;
@SerializedName("address")
@Expose
public String address;
@SerializedName("productData")
@Expose
public List<OrderDetailsModelDataList> productData = null;

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderDetailsModelDataList> getProductData() {
        return productData;
    }
}