package com.inger.tisch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.base.MyApplication;
import com.inger.tisch.ui.EventFragment;
import com.inger.tisch.ui.home.SearchFragment;
import com.inger.tisch.ui.profiles.ProfilesFragment;
import com.inger.tisch.ui.sign.SignInActivity;
import com.inger.tisch.utils.ActivityUtils;

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
                case R.id.find_item:
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), new SearchFragment(), R.id.content_main);
                    return true;
                case R.id.event_item:
                    ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), new EventFragment(), R.id.content_main);
                    return true;
                case R.id.me_item:
                    if (MyApplication.isLogIn == false) {
                        toast("请先登录！", 0);
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    } else {
                        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(), new ProfilesFragment(), R.id.content_main);
                    }
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        if (mSavedInstanceState == null) {
            ActivityUtils.replaceFragmentToActivity(mFragmentManager, new SearchFragment(), R.id.content_main);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("temp", bottomNavigationView.getSelectedItemId() + "");
    }
}