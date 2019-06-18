package com.inger.seller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.inger.seller.base.BaseActivity;
import com.inger.seller.ui.food.FoodFragment;
import com.inger.seller.ui.shop.ShopFragment;
import com.inger.seller.ui.trade.TradeFragment;
import com.inger.seller.utils.ActivityUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.bottom_menu)
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.food_item:
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), new FoodFragment(), R.id.content_main);
                    return true;
                case R.id.trade_item:
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), new TradeFragment(), R.id.content_main);
                    return true;
                case R.id.shop_item:
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),new ShopFragment(), R.id.content_main);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {

        if (mSavedInstanceState == null) {
            Log.i(TAG, "logicActivity: replace fragment");
            ActivityUtils.replaceFragmentToActivity(mFragmentManager, new FoodFragment(), R.id.content_main);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected int getLayoutId() {
        Log.i(TAG, "getLayoutId: set layout id" + R.layout.activity_main);
        return R.layout.activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("temp", bottomNavigationView.getSelectedItemId() + "");
    }


}
