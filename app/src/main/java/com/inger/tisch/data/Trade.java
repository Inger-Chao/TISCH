package com.inger.tisch.data;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Trade")
public class Trade extends AVObject {

    private String tradeId;
    private String shopId;
    private String buyerId;
    private String foodId;
    private Integer status;
    private Integer number;
    private String cookTime;
    private String payWay;
    private String description;
    private Double price;
    private String createAt;

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Trade(){}

    public Trade(String tradeId, String shopId, String cookTime, String payWay, String createAt) {
        this.tradeId = tradeId;
        this.shopId = shopId;
        this.cookTime = cookTime;
        this.payWay = payWay;
        this.createAt = createAt;
    }

    public Trade(String tradeId, String shopId, String buyerId, String foodId, Integer status, Integer number, String cookTime, String payway, String description) {
        this.tradeId = tradeId;
        this.shopId = shopId;
        this.buyerId = buyerId;
        this.foodId = foodId;
        this.status = status;
        this.number = number;
        this.cookTime = cookTime;
        this.payWay = payway;
        this.description = description;
    }

    public Trade(String theClassName, String tradeId, String shopId, String buyerId, String foodId, Integer status, Integer number,Double price, String cookTime, String payway, String description) {
        super(theClassName);
        this.tradeId = tradeId;
        this.shopId = shopId;
        this.buyerId = buyerId;
        this.foodId = foodId;
        this.status = status;
        this.price = price;
        this.number = number;
        this.cookTime = cookTime;
        this.payWay = payway;
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
