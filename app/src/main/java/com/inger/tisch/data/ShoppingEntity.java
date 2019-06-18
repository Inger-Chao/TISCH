package com.inger.tisch.data;

import android.util.Log;

import java.io.Serializable;

public class ShoppingEntity implements Serializable {

    private static final String TAG = "ShoppingEntity";

    //foodId
    String id;

    String name;

    int quantity;

    double unitPrice;

    double totalPrice;

    Food food;

    public static ShoppingEntity initWithFood(Food food){
        ShoppingEntity entity = new ShoppingEntity();
        entity.id = food.getObjectId();
        entity.name = food.getName();
        entity.unitPrice = Double.parseDouble(food.getPrice());
        entity.setQuantity(1);
        Log.i(TAG, "initWithFood: food.price : " + food.getPrice());
        entity.food = food;
        return entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        Log.i(TAG, "setQuantity: this quantity ：" + this.quantity + " this unitPrice");
        this.totalPrice = this.quantity * this.unitPrice;
        Log.i(TAG, "setQuantity: this total price :" + this.totalPrice);
    }

    public Food getFood() {
        return food;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
