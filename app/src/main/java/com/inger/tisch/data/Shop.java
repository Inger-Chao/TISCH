package com.inger.tisch.data;

import com.avos.avoscloud.AVClassName;

import java.io.Serializable;

@AVClassName("Shop")
public class Shop implements Serializable {

    private String objectId;
    private String name;
    private String hostId; //店铺用户id
    private String position;
    private String description;
    private double longitude;
    private double latitude;
    private int monthSale;
    private String avatar;

    public Shop(){

    }



    public Shop(String objectId,String name, String hostId, String position,String description, double longitude, double latitude, int monthSale) {
        this.objectId = objectId;
        this.name = name;
        this.hostId = hostId;
        this.description = description;
        this.position = position;
        this.longitude = longitude;
        this.latitude = latitude;
        this.monthSale = monthSale;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getMonthSale() {
        return monthSale;
    }

    public void setMonthSale(int monthSale) {
        this.monthSale = monthSale;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
