package com.inger.tisch.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.inger.tisch.MainActivity;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.base.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

// 登录界面
public class SignInActivity extends BaseActivity {

    private static final String TAG = "SignInActivity";

    @BindView(R.id.register_btn)
    TextView mRegisterBtn;
    @BindView(R.id.edit_username_login)
    EditText mEditUsernameLogin;
    @BindView(R.id.edit_password_login)
    EditText mEditPasswordLogin;

    @BindView(R.id.sign_in)
    Button mSignInBtn;


    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @OnClick(R.id.sign_in)
    public void onViewClicked() {
        String username = mEditUsernameLogin.getText().toString();
        String password = mEditPasswordLogin.getText().toString();
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null){
                    toast("登陆成功",0);
                    MyApplication.isLogIn = true;
                    Log.i(TAG, "done: get current user" + AVUser.getCurrentUser());
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                }else{
                    toast("登录失败",0);
                    Log.i(TAG, "登录失败:" + e);
                }
            }
        });
    }
}
