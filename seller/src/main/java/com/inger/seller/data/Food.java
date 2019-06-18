package com.inger.seller.data;

import com.avos.avoscloud.AVClassName;

import java.io.Serializable;

@AVClassName("Food")
public class Food implements Serializable {
    private String objectId;
    private String name;
    private String price;
    private String imgUrl;
    private String like;
    private String shopId;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public Food() {
    }

    public Food(String name, String price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Food(String name, String price, String imgUrl, String love) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;

        this.like = love;
    }

    public Food(String name, String price, String imgUrl, String love, String shopId, String description) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;

        this.like = love;
        this.shopId = shopId;
        this.description = description;
    }

    public Food(String objectId, String name, String price, String imgUrl, String like, String shopId, String description) {
        this.objectId = objectId;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.like = like;
        this.shopId = shopId;
        this.description = description;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
