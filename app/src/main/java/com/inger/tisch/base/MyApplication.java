package com.inger.tisch.base;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class MyApplication extends Application {

    private static MyApplication INSTANCE = null;

    public static final Boolean isDebug = true;

    public static Boolean isLogIn = false;

    public static Application getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"1RJtT98kLpWomN73WaJAEMk9-gzGzoHsz","thYHJbNMDkRi6vcOThuV3LGN");
        AVOSCloud.setDebugLogEnabled(isDebug);
    }
}
