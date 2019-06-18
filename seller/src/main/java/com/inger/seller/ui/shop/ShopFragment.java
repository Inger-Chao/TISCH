package com.inger.seller.ui.shop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inger.seller.R;
import com.inger.seller.base.BaseFragment;
import com.inger.seller.utils.config.CurrentShopInfo;

import butterknife.BindView;

public class ShopFragment extends BaseFragment {

    private static final String TAG = "ShopFragment";


    @BindView(R.id.me_image_avatar)
    ImageView avatar;
    @BindView(R.id.shop_name)
    TextView title;

    @BindView(R.id.shop_description)
    TextView description;

    @BindView(R.id.shop_position)
    TextView position;

    @BindView(R.id.image_edit_shop_info)
    ImageView editShopInfo;

    private static final Integer FLUSH_SELLER_INFO = 7788;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLUSH_SELLER_INFO){
                updateSellerUi();
            }
        }
    };



    @Override
    protected void init(View mView, Bundle mSavedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void logic() {
        updateSellerUi();
    }
    private void updateSellerUi(){
        Glide.with(this)
                .load(CurrentShopInfo.avatar)
                .into(avatar);
        title.setText(CurrentShopInfo.name);
        description.setText(CurrentShopInfo.description);
        position.setText(CurrentShopInfo.position);
    }

}
