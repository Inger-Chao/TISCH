package com.inger.tisch.ui.profiles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.inger.tisch.MainActivity;
import com.inger.tisch.R;
import com.inger.tisch.base.BaseActivity;
import com.inger.tisch.base.MyApplication;
import com.inger.tisch.utils.FileUtils;
import com.inger.tisch.utils.config.LCConfig;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyUserActivity extends BaseActivity {

    private static final String TAG = "ModifyUserActivity";

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.iv_avatar)
    CircleImageView avatar;

    @BindView(R.id.tv_username)
    EditText username;

    @BindView(R.id.tv_description_profile)
    EditText description;

    @BindView(R.id.iv_modify_desc)
    ImageView modifyDesc;

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {

    }

    @OnClick({R.id.iv_modify_desc, R.id.log_out_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_modify_desc:
                description.setFocusable(true);
                break;
            case R.id.log_out_btn:
                mAVUserFinal = null;
                MyApplication.isLogIn = false;
                AVUser.getCurrentUser().logOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
        }
    }

    @OnClick(R.id.iv_avatar)
    public void selectImage() {
        Matisse.from(this.mActivity)
                .choose(MimeType.ofAll(), false)
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(1)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
        Log.i(TAG, "selectImage: ------------");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: -------- ");
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK){
            Log.i(TAG, "onActivityResult: +++++++++++++ ");
            Uri uri = Matisse.obtainResult(data).get(0);
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .into(avatar);

                String path = FileUtils.getFilePathFromUri(this, uri);
                if (path != null) {
                    try {
                        final AVFile file = AVFile.withAbsoluteLocalPath(FileUtils.getFileName(path), path);
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    mAVUserFinal.put(LCConfig.getUSER_AVATAR(), file.getUrl());
                                    mAVUserFinal.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            Log.i(TAG, "done: update avatar success");
                                            Snackbar.make(avatar, "更新头像成功", Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Snackbar.make(avatar, "上传头像失败", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAVUserFinal != null) {
            username.setText(mAVUserFinal.getUsername());
            description.setText(mAVUserFinal.get("description").toString());
            String url = (String) mAVUserFinal.get(LCConfig.getUSER_AVATAR());
            if (url != null) {
                Glide.with(this)
                        .load(url)
                        .into(avatar);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_user;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
