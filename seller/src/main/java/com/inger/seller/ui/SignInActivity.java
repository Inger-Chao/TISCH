package com.inger.seller.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.inger.seller.MainActivity;
import com.inger.seller.R;
import com.inger.seller.base.BaseActivity;
import com.inger.seller.data.Shop;
import com.inger.seller.utils.config.CurrentShopInfo;

import java.util.List;

public class SignInActivity extends BaseActivity {
    private static final String TAG = "SignInActivity";

    EditText username;

    EditText password;

    Button signIn;
    private Shop curShop;

    private final Integer SIGN_IN_SUCCESS = 2100;
    private final Integer GO_MAIN_ACTIVITY = 1890;



    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SIGN_IN_SUCCESS) {
                searchShop();
            }
            if (msg.what == GO_MAIN_ACTIVITY){
                goMainActivity();

            }
        }
    };

    private void searchShop(){
        final String userId = AVUser.getCurrentUser().getObjectId();
        if(AVUser.getCurrentUser().get("status").equals("1")) {
            Log.i(TAG, "handleMessage:  current user id ；" + AVUser.getCurrentUser().getObjectId());
            AVQuery<Shop> query = new AVQuery<>("Shop");
            query.findInBackground(new FindCallback<Shop>() {
                @Override
                public void done(List<Shop> avObjects, AVException avException) {
                    if (avException == null && !avObjects.isEmpty()) {
                        for (AVObject o : avObjects) {
                            if (o.get("hostId").toString().equals(userId)) {
                                curShop = new Shop(o.getObjectId(), o.get("name").toString(), o.get("description").toString(),
                                        o.get("hostId").toString(),
                                        o.get("position").toString(),
                                        Double.parseDouble(o.get("longitude").toString()),
                                        Double.parseDouble(o.get("latitude").toString()),
                                        Integer.parseInt(o.get("monthSale").toString()));
                                CurrentShopInfo.id = o.getObjectId();
                                CurrentShopInfo.avatar = o.get("avatar").toString();
                                CurrentShopInfo.name = o.get("name").toString();
                                CurrentShopInfo.position =  o.get("position").toString();
                                CurrentShopInfo.description = o.get("description").toString();
                                CurrentShopInfo.latitude = Double.parseDouble(o.get("latitude").toString());
                                CurrentShopInfo.longitude = Double.parseDouble(o.get("longitude").toString());
                                CurrentShopInfo.monthSale = Integer.parseInt(o.get("monthSale").toString());
                                Log.i(TAG, "done: cur shop:" + curShop);
                                Log.i(TAG, "done: cur shop:" + curShop);
                                handler.sendEmptyMessage(GO_MAIN_ACTIVITY);
                            }
                        }
                        if (curShop == null) {
                            Toast.makeText(SignInActivity.this, "无法查询到店铺信息，请使用买家端", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "无法查询到店铺信息", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(SignInActivity.this, "无法查询到店铺信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("test", "this is a string");
        intent.putExtra("shop", curShop);
        Log.i(TAG, "handleMessage: cur shop" + curShop);
        Log.i(TAG, "goMainActivity: " + intent);
        Toast.makeText(SignInActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        setContentView(R.layout.activity_seller_sign_in);
        username = findViewById(R.id.seller_edit_username_login);
        password = findViewById(R.id.seller_edit_password_login);
        signIn = findViewById(R.id.seller_sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInShop();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_sign_in;
    }

    public void signInShop(){
        Log.i(TAG, "signInShop: ");
        String name = username.getText().toString();
        String pwd = password.getText().toString();

        AVUser.logInInBackground(name, pwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if (e == null){
                    handler.sendEmptyMessage(SIGN_IN_SUCCESS);
                    Log.i(TAG, "done: get current user" + AVUser.getCurrentUser().getObjectId());
                }
                else {
                    Toast.makeText(SignInActivity.this, "用户名或密码输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
