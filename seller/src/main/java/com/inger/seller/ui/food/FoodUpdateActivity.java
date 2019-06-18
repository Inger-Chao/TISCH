package com.inger.seller.ui.food;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.inger.seller.R;
import com.inger.seller.base.BaseActivity;
import com.inger.seller.data.Food;
import com.inger.seller.utils.FileUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodUpdateActivity extends BaseActivity {

    private static final String TAG = "FoodUpdateActivity";
    private static final int REQUEST_CODE_CHOOSE = 23;


    @BindView(R.id.add_food_img)
    ImageView foodImage;

    @BindView(R.id.add_food_name)
    EditText foodName;

    @BindView(R.id.add_food_description)
    EditText foodDescription;

    @BindView(R.id.add_food_price)
    EditText foodPrice;

    @BindView(R.id.btn_submit_food_info)
    Button updateFoodInfo;

    private Food food = null;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 6541)
            {
                initView();
            }
        }
    };

    @Override
    protected void logicActivity(Bundle mSavedInstanceState) {
        foodImage.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("food");
        initView();
        updateFoodInfo.setVisibility(View.VISIBLE);
    }

    public void initView(){
        Glide.with(this)
                .load(food.getImgUrl())
                .into(foodImage);
        foodName.setText(food.getName());
        foodDescription.setText(food.getDescription());
        foodPrice.setText(food.getPrice());
    }

    @OnClick({R.id.add_food_img, R.id.btn_submit_food_info})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.add_food_img:
                Matisse.from(this.mActivity)
                        .choose(MimeType.ofAll(), false)
                        .theme(R.style.Matisse_Dracula)
                        .countable(false)
                        .maxSelectable(1)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
            case R.id.btn_submit_food_info:
                AVObject avObject = AVObject.createWithoutData("Food",food.getObjectId());
                avObject.put("imgUrl",food.getImgUrl());
                avObject.put("name",foodName.getText().toString());
                avObject.put("description",foodDescription.getText().toString());
                avObject.put("price",foodPrice.getText().toString());
                avObject.put("like",food.getLike());
                avObject.saveInBackground();
                Toast.makeText(this, "商品信息更新成功", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(6541);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: +++++++++++++ ");
            Uri uri = Matisse.obtainResult(data).get(0);
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .into(foodImage);
            }


            String path = FileUtils.getFilePathFromUri(this, uri);
            if (path != null) {
                try {
                    final AVFile file = AVFile.withAbsoluteLocalPath(FileUtils.getFileName(path), path);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                food.setImgUrl(file.getUrl());
                                Snackbar.make(foodImage, "上传食物图像成功", Snackbar.LENGTH_LONG).show();

                            } else {
                                Snackbar.make(foodImage, "上传食物图像失败", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_update_food_info; }

    @Override
    public void finish() {
        updateFoodInfo.setVisibility(View.GONE);
        foodImage.setVisibility(View.GONE);
        super.finish();
    }
}
