package com.inger.tisch.data;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.inger.tisch.data.event.ShoppingCartChangeEvent;
import com.inger.tisch.utils.EventUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private static final String TAG = "ShoppingCart";

    private String shopId;
    private volatile Shop shop = new Shop();
    private Map<String, ShoppingEntity> mShoppingList;

    private static ShoppingCart instance;

    public static ShoppingCart getInstance(){
        if (instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    private ShoppingCart(){
        mShoppingList = new HashMap<>();
    }

    private void sendChangeEvent() {
        EventUtil.sendEvent(new ShoppingCartChangeEvent());
    }


    /* 往购物车中添加商品 */
    public boolean push(Food food){
        String id = food.getObjectId();
        if (mShoppingList.isEmpty()){
            //第一次添加需要记录饭店id
            shopId = food.getShopId();
            setShopInfo(shopId);
            ShoppingEntity entity = ShoppingEntity.initWithFood(food);
            mShoppingList.put(id, entity);
            sendChangeEvent();
            return true;
        }
        else if (shopId.equals(food.getShopId())){
            ShoppingEntity entity = mShoppingList.containsKey(id) ? mShoppingList.get(id) : null;
            if (entity == null)
            {
                entity = ShoppingEntity.initWithFood(food);
            }else {
                int originalQuantity = entity.getQuantity();
                entity.setQuantity(++originalQuantity);
            }
            mShoppingList.put(id,entity);
            sendChangeEvent();
            return true;
        }
        return false;
    }

    public void setShopInfo(String id){
        Log.i(TAG, "setShopInfo: " + id);
        AVQuery<AVObject> query = new AVQuery<>("Shop");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                if (e == null){

                    Log.i(TAG, "done: " + object.toString());
                    shop.setObjectId(id);
                    shop.setHostId(object.get("hostId").toString());
                    shop.setName(object.get("name").toString());
                    shop.setLatitude(Double.parseDouble(object.get("latitude").toString()));
                    shop.setDescription(object.get("description").toString());
                    shop.setLongitude((Double) object.get("longitude"));
                    shop.setPosition(object.get("position").toString());
                    shop.setMonthSale(Integer.parseInt(object.get("monthSale").toString()));
                }else
                {
                    Log.i(TAG, "done: can't get shop info");
                }

            }
        });
    }

    public boolean pop(Food food){
        String id = food.getObjectId();
        if (mShoppingList.containsKey(id)){
            ShoppingEntity entity = mShoppingList.get(id);
            int originQuantity = entity.getQuantity();
            if (originQuantity > 1){
                entity.setQuantity(--originQuantity);
                mShoppingList.put(id, entity);
                sendChangeEvent();
                return true;
            }
            else if (originQuantity == 1){
                mShoppingList.remove(id);
                sendChangeEvent();
                return true;
            }
        }
        return false;
    }

    /**
     * 清空购物车里的所有数据
     */
    public void clearAll() {
        mShoppingList.clear();
        sendChangeEvent();
    }
    /* 获取商家店铺 Id*/
    public String getShopId(){
        return shopId;
    }

    public double getTotalPrice(){
        double totalPrice = 0.0d;
        for (ShoppingEntity e : mShoppingList.values()) {
            Log.i(TAG, "getTotalPrice: " + e.getQuantity() + " " + e.getUnitPrice() + " " + e.getTotalPrice());
            totalPrice += e.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * 获取购物车里所有商品的数量
     * @return 商品数量
     */
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (ShoppingEntity entry : mShoppingList.values()) {
            totalQuantity += entry.getQuantity();
        }

        return totalQuantity;
    }

    /**
     * 获取购物车的选购列表
     * @return 选购列表
     */
    public List<ShoppingEntity> getShoppingList() {
        List<ShoppingEntity> entities = new ArrayList<>();
        for (ShoppingEntity entry : mShoppingList.values()) {
            entities.add(entry);
        }

        return entities;
    }

    /* 获取购物车中菜品的商品种类 */
    public int getShoppingItemCount(){
        return mShoppingList.size();
    }

    /**
     * 获取购物车里指定商品的数量
     * @param product 指定的商品
     * @return 商品数量
     */
    public int getQuantityForProduct(Food product) {
        String id = product.getObjectId();
        if (mShoppingList.containsKey(id)) {
            return mShoppingList.get(id).getQuantity();
        }

        return 0;
    }


}
