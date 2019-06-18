package com.inger.tisch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.data.Food;
import com.inger.tisch.data.Shop;
import com.inger.tisch.ui.shop.ShopActivity;
import com.inger.tisch.utils.config.LCConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodDetailsActivity extends BaseActivity {

    private static final String TAG = "FoodDetailsActivity";

    @BindView(R.id.food_detail_title)
    TextView foodTitle;

    @BindView(R.id.food_detail_image)
    ImageView foodImage;

    @BindView(R.id.food_detail_price)
    TextView foodPrice;

    @BindView(R.id.food_detail_description)
    TextView foodDescription;

    @BindView(R.id.shop_image_avatar)
    ImageView shopImage;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.shop_description)
    TextView shopDescription;
    @BindView(R.id.shop_position)
    TextView shopPosition;
    @BindView(R.id.layout_shop_info)
    RelativeLayout shopLayout;
    Button predetermine;

    Shop curShop = null;
    String hostAvatarUrl = null;

    private static final Integer FLUSH_SHOP_LAYOUT = 1987;
    private static final Integer FLUSH_SHOP_AVATAR = 1988;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLUSH_SHOP_LAYOUT){
                getHostAvatar(curShop);
            }
            if (msg.what == FLUSH_SHOP_AVATAR) {
                loadHostImage();
            }
        }
    };

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        Intent intent = getIntent();
        Food food = (Food) intent.getSerializableExtra("food");
        foodTitle.setText(food.getName());
        foodDescription.setText(food.getDescription());
        foodPrice.setText("￥" + food.getPrice());
        Glide.with(this)
                .load(food.getImgUrl())
                .into(foodImage);
        predetermine = findViewById(R.id.btn_food_predetermine);
        predetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predetermine(food,curShop);
            }
        });
        getShopById(food.getShopId());
    }

    public void predetermine(Food food, Shop shop){

    }

    private void getShopById(String id){
        AVQuery<AVObject> query = new AVQuery<>("Shop");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                    for (AVObject s:avObjects) {
                        Log.i(TAG, "done: shop list in avobjects:" + s);
                        Log.i(TAG, "done: string id: " + id);
                        if (s.getObjectId().equals(id)){
                            curShop = new Shop(s.getObjectId(),s.get("name").toString(),
                                    s.get("hostId").toString(),s.get("position").toString(),
                                    s.get("description").toString(),Double.parseDouble(s.get("longitude").toString()),
                                    Double.parseDouble(s.get("latitude").toString()),0);

                            Log.i(TAG, "done: curshop info:" + s);
                            handler.sendEmptyMessage(FLUSH_SHOP_LAYOUT);
                            break;
                        }
                }
            }
        });
    }

    @OnClick(R.id.layout_shop_info)
    public void goShopActivity(){
        Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("shop",curShop);
        startActivity(intent);
        finish();
    }

    private void getHostAvatar(Shop shop){
        AVQuery<AVObject> query = new AVQuery<>(LCConfig.getUSER_TABLE());
        query.getInBackground(shop.getHostId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                hostAvatarUrl = object.get("avatar").toString();
                handler.sendEmptyMessage(FLUSH_SHOP_AVATAR);
            }
        });
    }

    private void loadHostImage(){
        Log.i(TAG, "loadHostImage: ");
        shopDescription.setText(curShop.getDescription());
        shopName.setText(curShop.getName());
        shopPosition.setText(curShop.getPosition());
        Glide.with(this)
                .load(hostAvatarUrl)
                .into(shopImage);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_food_details;
    }
}
