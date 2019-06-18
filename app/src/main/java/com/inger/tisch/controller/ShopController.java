package com.inger.tisch.controller;

import android.util.Log;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.inger.tisch.data.Shop;

public class ShopController {

    private static final String TAG = "ShopController";

    Shop shop = new Shop();

    public void addShop(Shop shop){
        String cql = "insert into Shop(name, description, position, hostId) values('"
                + shop.getName() +"','" + shop.getDescription()+ "','" + shop.getPosition()+ "','"+ shop.getHostId()+ "')";
        AVQuery.doCloudQueryInBackground(cql, new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult result, AVException avException) {
                if (avException == null){
                    Log.i(TAG, "done: insert shop successfully");
                }else
                {
                    Log.i(TAG, "done: insert shop failed : " + avException);
                }
            }
        });
    }


}
