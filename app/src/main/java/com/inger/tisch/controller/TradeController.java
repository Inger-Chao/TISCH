package com.inger.tisch.controller;

import com.inger.tisch.data.Trade;

import java.util.ArrayList;

public class TradeController {
    private static final String TAG = "TradeController";

    public void addTrade(Trade trade){

        trade.put("tradeId",trade.getTradeId());
        trade.put("shopId",trade.getShopId());
        trade.put("buyerId",trade.getBuyerId());
        trade.put("foodId",trade.getFoodId());
        trade.put("status",trade.getStatus().toString());
        trade.put("number",trade.getNumber());
        trade.put("cookTime",trade.getCookTime());
        trade.put("payway",trade.getPayWay());
        trade.put("price",trade.getPrice().toString());
        trade.put("description",trade.getDescription());
        trade.saveInBackground();
    }

    public void addTrades(ArrayList<Trade> trades){
        for (int i = 0; i <trades.size() ; i++)
        {
            addTrade(trades.get(i));
        }
    }

}
