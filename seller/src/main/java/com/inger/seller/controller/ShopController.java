package com.inger.seller.controller;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.inger.seller.data.Shop;

import java.util.List;

public class ShopController {

    private static final String TAG = "ShopController";

    private Shop curShop;

    public Shop findCurShop() throws AVException {

        final String userId = AVUser.getCurrentUser().getObjectId();
        Log.i(TAG, "handleMessage:  current user id ；" + AVUser.getCurrentUser().getObjectId());
        AVQuery<Shop> query = new AVQuery<>("Shop");
        query.findInBackground(new FindCallback<Shop>() {
                @Override
                public void done(List<Shop> avObjects, AVException avException) {
                    if (avException == null && !avObjects.isEmpty()) {
                        for (AVObject o : avObjects) {
                            if (o.get("hostId").toString().equals(userId)) {
                                curShop = new Shop(o.getObjectId(), o.get("name").toString(),
                                        o.get("description").toString(),
                                        o.get("hostId").toString(),
                                        o.get("position").toString(),
                                        Double.parseDouble(o.get("longitude").toString()),
                                        Double.parseDouble(o.get("latitude").toString()),
                                        Integer.parseInt(o.get("monthSale").toString()));
                                Log.i(TAG, "done: cur shop:" + curShop);
                            }
                        }
                    }
                    else {
                        Log.i(TAG, "done: cur shop init fail");
                        avException.printStackTrace();
                    }
                }
            });

        return curShop;
    }

}
