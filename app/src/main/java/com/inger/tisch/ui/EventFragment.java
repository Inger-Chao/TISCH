package com.inger.tisch.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseFragment;
import com.inger.tisch.utils.LocationManager;

import butterknife.BindView;

public class EventFragment extends BaseFragment {
    private static final String TAG = "EventFragment";
    @BindView(R.id.map)
    MapView mapView;

    AMap aMap;

    UiSettings uiSettings;

    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {
        mapView.onCreate(mSavedInstanceState);
        aMap = mapView.getMap(); //初始化地图控制器对象

        //初始化地图图层配置
        initMap();
        //初始化位置信息
        initLocation();
        //服务器所有节点查找
        //queryAllLatlng();
        //设置mark点击事件
        //clickerListener();
        }

    private void initLocation() {
        Log.i(TAG, "initLocation: ");
        LocationManager locationManager = new LocationManager(this.getContext());
        Log.i(TAG, "initLocation: open location");
        locationManager.openLocation();
    }

    private void initMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0F));
        //定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.radiusFillColor(resources.getColor(R.color.colorTheme)) // 圆框颜色
        //myLocationStyle.strokeColor(resources.getColor(R.color.colorTheme))
        myLocationStyle.interval(20000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected void logic() {

    }
}
