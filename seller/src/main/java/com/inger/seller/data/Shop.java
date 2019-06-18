package com.inger.seller.data;

import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("Shop")
public class Shop extends AVObject implements Parcelable {

    private String name;
    private String description;
    private String hostId; //店铺用户id
    private String position;
    private double longitude;
    private double latitude;
    private int monthSale;

    public Shop(){

    }

    public Shop(String objectId,String name, String description,
                String hostId, String position,double longitude, double latitude,
                int monthSale) {
        this.objectId = objectId;
        this.name = name;
        this.position = position;
        this.description = description;
        this.hostId = hostId;
        this.monthSale = monthSale;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
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

}
