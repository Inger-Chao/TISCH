package com.inger.tisch.data;

public class TradeItemInfo {

    private Trade trade;

    private Shop shop;

    private Double price;


    public TradeItemInfo(){}

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String toString(){
        return shop.getName() + ", " + trade.getCookTime() +"," +  this.price;
    }
}
