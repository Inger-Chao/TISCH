package com.inger.tisch.ui.sign;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVSMS;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.base.MyApplication;
import com.inger.tisch.controller.ShopController;
import com.inger.tisch.data.Shop;
import com.inger.tisch.utils.LocationManager;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.username_register)
    EditText mUsernameRegister;
    @BindView(R.id.verify_code_register)
    EditText mCodeRegister;
    @BindView(R.id.password_register)
    EditText mPasswordRegister;
    @BindView(R.id.phone_register)
    EditText mPhoneRegister;
    @BindView(R.id.checkbox_status)
    CheckBox userStatus;
    @BindView(R.id.confirm_password_register)
    EditText mConfirmPassword;

    Shop shop = new Shop();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1800){
                ShopController shopController = new ShopController();
                shopController.addShop(shop);
            }
        }
    };

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.get_verify_code_btn)
    public void getVerifyCode(){
        AVSMSOption option = new AVSMSOption();
        option.setTtl(10);                     // 验证码有效时间为 10 分钟
        option.setApplicationName("Tisch");
        option.setOperation("用户注册");
            AVSMS.requestSMSCodeInBackground(mPhoneRegister.getText().toString(),option, new RequestMobileCodeCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        /* 请求成功 */
                        Log.i(TAG, "done: sms send success");
                    } else {
                        /* 请求失败 */
                        Log.e(TAG, "done: sms send failed" );

                    }
                }
            });
    }

    @OnClick(R.id.back_register_image)
    public void back(){
        finish();
    }

    @OnCheckedChanged(R.id.checkbox_status)
    public void changeStatus(){

        LinearLayout writeShopInfo = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_register_shop_info, null);

        final EditText shopName = writeShopInfo.findViewById(R.id.edt_register_shop_name);
        final EditText shopDescription = writeShopInfo.findViewById(R.id.edt_register_shop_description);
        final EditText shopPosition = writeShopInfo.findViewById(R.id.edt_register_shop_position);

        if (userStatus.isChecked()){
            new AlertDialog.Builder(this)
                    .setTitle("填写店铺信息")
                    .setView(writeShopInfo)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            shop.setName(shopName.getText().toString());
                            shop.setDescription(shopDescription.getText().toString());
                            shop.setPosition(shopPosition.getText().toString());
                            locationShop();
                            Toast.makeText(RegisterActivity.this, "商家信息填写完成", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //由于“取消”的button我们没有设置点击效果，直接设为null就可以了
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }
    }

    public void locationShop(){
        Log.i(TAG, "initLocation: ");
        LocationManager locationManager = new LocationManager(this);
        Log.i(TAG, "initLocation: open location");
        locationManager.openLocation();
        locationManager.setLocationListener(new LocationManager.LocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.i(TAG, "onLocationChanged: longitude: " + aMapLocation.getLongitude()
                        + " latitude: " + aMapLocation.getLatitude());
                shop.setLatitude(aMapLocation.getLatitude());
                shop.setLongitude(aMapLocation.getLongitude());
            }
        });
    }

    @OnClick(R.id.register_btn)
    public void register(){
        String phone = mPhoneRegister.getText().toString();
        String code = mCodeRegister.getText().toString();

        if (MyApplication.isDebug){
                uploadInfo();


        }else {
            AVSMS.verifySMSCodeInBackground(code,phone, new AVMobilePhoneVerifyCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        /* 验证成功 */
                        snackBar(mUsernameRegister,"验证码正确",0);
                        uploadInfo();
                    } else {
                        /* 验证失败 */
                        snackBar(mUsernameRegister, "获取验证码失败", 0);
                        Log.i(TAG, "获取验证码失败："+e.toString());
                    }
                }
            });
        }

    }

    private void uploadInfo() {
        final AVUser mAVUser = new AVUser();
        shop.setHostId(mAVUser.getObjectId());
        mAVUser.setUsername(mUsernameRegister.getText().toString());
        mAVUser.setPassword(mPasswordRegister.getText().toString());

        mAVUser.setMobilePhoneNumber(mPhoneRegister.getText().toString());
        if(userStatus.isChecked()){
            mAVUser.put("status","1");
        }
        mAVUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException mE) {
                if (mE == null){
                    toast("注册成功", 0);
                    handler.sendEmptyMessage(1800);
                    startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                    finish();
                }else {
                    if (mE.getCode() == 214){
                        toast("手机号已经注册",0);
                    }
                    if (mE.getCode() == 217){
                        toast("无效的手机号码",0);
                    }
                    if (mE.getCode() == 202){
                        toast("用户名已经存在",0);
                    }
                }
            }
        });
    }

}
